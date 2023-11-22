package com.dailyon.snsservice.repository.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.post.PostResponse;
import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import com.dailyon.snsservice.repository.member.MemberJpaRepository;
import java.util.List;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostRepositoryTest {

  @Autowired private PostRepository postRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;

  @Test
  @DisplayName("게시글 목록 조회 - 미인증")
  void findAllWithoutAuth() {
    // given
    Long memberId = null;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));

    // when
    Page<PostResponse> posts = postRepository.findAllWithIsLike(memberId, pageRequest);

    // then
    assertThat(posts.getTotalElements()).isSameAs(16L);
    assertThat(posts.getTotalPages()).isSameAs(2);
    posts.getContent().forEach(p -> assertThat(p.getIsLike()).isNull());
  }

  @Test
  @DisplayName("게시글 목록 조회 - 인증")
  void findAllWithAuth() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));

    // when
    Page<PostResponse> posts = postRepository.findAllWithIsLike(memberId, pageRequest);

    // then
    assertSame(16L, posts.getTotalElements());
    assertSame(2, posts.getTotalPages());
    posts.getContent().forEach(p -> assertThat(p.getIsLike()).isNotNull());
  }

  @Test
  @DisplayName("게시글 등록")
  void save() {
    // given
    Long memberId = 1L;
    Member member =
        memberJpaRepository.findById(memberId).orElseThrow(MemberEntityNotFoundException::new);

    PostImageProductDetail postImageProductDetail =
        PostImageProductDetail.createPostImageProductDetail(1L, "size", 10.0, 10.0);
    Set<PostImageProductDetail> postImageProductDetails = Set.of(postImageProductDetail);
    PostImage postImage =
        PostImage.createPostImage(
            "/images/thumbnail.png", "/images/img.png", postImageProductDetails);
    List<HashTag> hashTags = List.of(HashTag.createHashTag("태그 1"));
    Post post = Post.createPost(member, "post 1", "post desc 1", 5.6, 150.0, postImage, hashTags);

    // when
    Post savedPost = postRepository.save(post);

    // then
    assertThat(savedPost.getId()).isNotNull();
    assertThat(savedPost.getTitle()).isEqualTo(post.getTitle());
    assertThat(savedPost.getDescription()).isEqualTo(post.getDescription());
    assertThat(savedPost.getPostImage().getThumbnailImgUrl())
        .isEqualTo(post.getPostImage().getThumbnailImgUrl());
    assertThat(savedPost.getPostImage().getImgUrl()).isEqualTo(post.getPostImage().getImgUrl());
    assertThat(savedPost.getPostImage().getPostImageProductDetails().size())
        .isSameAs(post.getPostImage().getPostImageProductDetails().size());
    assertThat(savedPost.getPostImage().getPostImageProductDetails())
        .isNotEmpty()
        .containsExactlyInAnyOrder(postImageProductDetail);
  }

  @Test
  @DisplayName("게시글 좋아요 목록 조회")
  void findAllWithPostLike() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<Post> posts = postRepository.findAllWithPostLike(memberId, pageRequest);

    // then
    assertFalse(posts.hasNext());
    assertSame(1, posts.getTotalPages());
    assertSame(1, posts.getContent().size());
  }

  @Test
  @DisplayName("사용자 게시글 목록 조회")
  void findAllByMemberId() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<Post> posts = postRepository.findAllByMemberId(memberId, pageRequest);

    // then
    assertFalse(posts.hasNext());
    assertSame(1, posts.getTotalPages());
    assertSame(6, posts.getContent().size());
  }

  @Test
  @DisplayName("OOTD top4 게시글 목록 조회")
  void findTop4() {
    // given
    Long productId = 101L;

    // given, when
    List<Post> posts = postRepository.findTop4ByOrderByLikeCountDesc(productId);

    // then
    assertSame(4, posts.size());
    posts.forEach(
        post ->
            post.getPostImage()
                .getPostImageProductDetails()
                .forEach(pipd -> assertSame(productId, pipd.getProductId())));
    assertTrue(posts.get(0).getLikeCount() >= posts.get(1).getLikeCount());
  }
}
