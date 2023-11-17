package com.dailyon.snsservice.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

  @Id
  @Column(name = "id")
  private Long id;

  @Column(name = "nickname", nullable = false)
  private String nickname;

  @Column(name = "profile_img_url", columnDefinition = "varchar default '/member/default-profile-img.png'")
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

  public static Member createMember(Long id, String nickname, String code) {
    return Member.builder().id(id).nickname(nickname).code(code).build();
  }
}
