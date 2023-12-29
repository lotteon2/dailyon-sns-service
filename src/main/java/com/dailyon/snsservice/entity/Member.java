package com.dailyon.snsservice.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.*;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @Column(name = "id")
  private Long id;

  @OneToMany(mappedBy = "follower")
  private List<Follow> followers;

  @OneToMany(mappedBy = "following")
  private List<Follow> following;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(
      name = "profile_img_url",
      columnDefinition = "varchar default '/member/default-profile-img.png'")
  @Builder.Default
  private String profileImgUrl = "/member/default-profile-img.png";

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "following_count", nullable = false, columnDefinition = "integer default 0")
  @Builder.Default
  private Integer followingCount = 0;

  @Column(name = "follower_count", nullable = false, columnDefinition = "integer default 0")
  @Builder.Default
  private Integer followerCount = 0;

  public void increaseFollowingCount() {
    this.followingCount += 1;
  }

  public void increaseFollowerCount() {
    this.followerCount += 1;
  }

  public void decreaseFollowingCount() {
    this.followingCount -= 1;
  }

  public void decreaseFollowerCount() {
    this.followerCount -= 1;
  }

  public static Member createMember(String nickname, String profileImgUrl, String code) {
    return Member.builder().nickname(nickname).profileImgUrl(profileImgUrl).code(code).build();
  }

  public void updateMember(String nickname, String profileImgUrl) {
    this.nickname = nickname;
    this.profileImgUrl = profileImgUrl;
  }
}
