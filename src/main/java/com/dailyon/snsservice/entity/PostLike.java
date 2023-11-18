package com.dailyon.snsservice.entity;

import com.dailyon.snsservice.entity.ids.PostLikeId;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;

@Getter
@Entity
@IdClass(PostLikeId.class)
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostLike {

  @Id
  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  @Id
  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post post;

  public static PostLike createPostLike(Member member, Post post) {
    PostLike postLike = PostLike.builder().member(member).post(post).build();
    post.getPostLikes().add(postLike);
    return postLike;
  }
}
