package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.ids.FollowId;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@IdClass(FollowId.class)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

  @Id
  @ManyToOne
  @JoinColumn(name = "follower_id")
  private Member follower;

  @Id
  @ManyToOne
  @JoinColumn(name = "following_id")
  private Member following;

  public static Follow createFollow(Member follower, Member following) {
    return Follow.builder().following(following).follower(follower).build();
  }
}
