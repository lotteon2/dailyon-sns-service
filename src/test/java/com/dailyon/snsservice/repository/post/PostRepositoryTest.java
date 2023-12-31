package com.dailyon.snsservice.repository.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.dto.response.post.OOTDPostResponse;
import com.dailyon.snsservice.dto.response.post.PostDetailResponse;
import com.dailyon.snsservice.dto.response.post.PostResponse;
import com.dailyon.snsservice.entity.*;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import com.dailyon.snsservice.repository.member.MemberJpaRepository;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
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
  @PersistenceContext private EntityManager em;

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
    Page<Post> posts = postRepository.findAllWithPostLikeByMemberIdIn(memberId, pageRequest);

    // then
    assertThat(posts.hasNext()).isFalse();
    assertThat(posts.getTotalPages()).isSameAs(1);
    assertThat(posts.getContent().size()).isSameAs(1);
  }

  @Test
  @DisplayName("내 OOTD 게시글 목록 조회")
  void findMyPostsByMemberId() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<OOTDPostResponse> myOOTDPostResponses =
        postRepository.findMyPostsByMemberId(memberId, pageRequest);

    // then
    assertThat(myOOTDPostResponses.getTotalPages()).isSameAs(1);
    assertThat(myOOTDPostResponses.getTotalElements()).isSameAs(6L);
    assertThat(myOOTDPostResponses.getContent().size()).isSameAs(6);
    myOOTDPostResponses
        .getContent()
        .forEach(
            post -> {
              assertThat(post.getId()).isNotNull();
              assertThat(post.getThumbnailImgUrl()).isNotNull();
              assertThat(post.getLikeCount()).isNotNull();
              assertThat(post.getViewCount()).isNotNull();
              assertThat(post.getIsLike()).isNotNull();
            });
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

  @Test
  @DisplayName("게시글 삭제")
  void softDeleteById() {
    // given
    Long postId = 12L;
    Long memberId = 1L;

    // when
    postRepository.softDeleteById(postId, memberId);

    // then
    assertThrowsExactly(
        NoResultException.class,
        () ->
            em.createQuery(
                    "select p from Post p where p.id = :postId and p.member.id = :memberId and p.isDeleted = false",
                    Post.class)
                .setParameter("postId", postId)
                .setParameter("memberId", memberId)
                .getSingleResult());
  }

  @Test
  @DisplayName("게시글 상세 조회 - 인증")
  void findDetailByIdWithIsFollowingWithAuth() {
    // given
    Long postId = 1L;
    Long memberId = 2L;

    // when
    PostDetailResponse postDetailResponse =
        postRepository.findDetailByIdWithIsFollowingAndIsLike(postId, memberId);

    // then
    assertThat(postDetailResponse.getId()).isSameAs(postId);
    assertThat(postDetailResponse.getTitle()).isNotNull();
    assertThat(postDetailResponse.getDescription()).isNotNull();
    assertThat(postDetailResponse.getStature()).isNotNull();
    assertThat(postDetailResponse.getWeight()).isNotNull();
    assertThat(postDetailResponse.getImgUrl()).isNotNull();
    assertThat(postDetailResponse.getViewCount()).isNotNull();
    assertThat(postDetailResponse.getLikeCount()).isNotNull();
    assertThat(postDetailResponse.getCommentCount()).isNotNull();
    assertThat(postDetailResponse.getCreatedAt()).isNotNull();
    assertThat(postDetailResponse.getMember().getNickname()).isNotNull();
    assertThat(postDetailResponse.getMember().getProfileImgUrl()).isNotNull();
    assertThat(postDetailResponse.getMember().getCode()).isNotNull();
    assertThat(postDetailResponse.getMember().getIsFollowing()).isTrue();
    assertThat(postDetailResponse.getHashTags().size()).isSameAs(1);
    postDetailResponse
        .getHashTags()
        .forEach(
            hashTag -> {
              assertThat(hashTag.getId()).isNotNull();
              assertThat(hashTag.getName()).isNotNull();
            });
    assertThat(postDetailResponse.getPostImageProductDetails().size()).isSameAs(1);
    postDetailResponse
        .getPostImageProductDetails()
        .forEach(
            postImageProductDetailResponse -> {
              assertThat(postImageProductDetailResponse.getId()).isNotNull();
              assertThat(postImageProductDetailResponse.getSize()).isNotNull();
              assertThat(postImageProductDetailResponse.getLeftGapPercent()).isNotNull();
              assertThat(postImageProductDetailResponse.getTopGapPercent()).isNotNull();
              assertThat(postImageProductDetailResponse.getHasAvailableCoupon()).isNull();
            });
  }

  @Test
  @DisplayName("게시글 상세 조회 - 미인증")
  void findDetailByIdWithIsFollowingWithoutAuth() {
    // given
    Long postId = 1L;
    Long memberId = null;

    // when
    PostDetailResponse postDetailResponse =
        postRepository.findDetailByIdWithIsFollowingAndIsLike(postId, memberId);

    // then
    assertThat(postDetailResponse.getId()).isSameAs(postId);
    assertThat(postDetailResponse.getTitle()).isNotNull();
    assertThat(postDetailResponse.getDescription()).isNotNull();
    assertThat(postDetailResponse.getStature()).isNotNull();
    assertThat(postDetailResponse.getWeight()).isNotNull();
    assertThat(postDetailResponse.getImgUrl()).isNotNull();
    assertThat(postDetailResponse.getViewCount()).isNotNull();
    assertThat(postDetailResponse.getLikeCount()).isNotNull();
    assertThat(postDetailResponse.getCommentCount()).isNotNull();
    assertThat(postDetailResponse.getCreatedAt()).isNotNull();
    assertThat(postDetailResponse.getMember().getNickname()).isNotNull();
    assertThat(postDetailResponse.getMember().getProfileImgUrl()).isNotNull();
    assertThat(postDetailResponse.getMember().getCode()).isNotNull();
    assertThat(postDetailResponse.getMember().getIsFollowing()).isNull();
    assertThat(postDetailResponse.getHashTags().size()).isSameAs(1);
    postDetailResponse
        .getHashTags()
        .forEach(
            hashTag -> {
              assertThat(hashTag.getId()).isNotNull();
              assertThat(hashTag.getName()).isNotNull();
            });
    assertThat(postDetailResponse.getPostImageProductDetails().size()).isSameAs(1);
    postDetailResponse
        .getPostImageProductDetails()
        .forEach(
            postImageProductDetailResponse -> {
              assertThat(postImageProductDetailResponse.getId()).isNotNull();
              assertThat(postImageProductDetailResponse.getSize()).isNotNull();
              assertThat(postImageProductDetailResponse.getLeftGapPercent()).isNotNull();
              assertThat(postImageProductDetailResponse.getTopGapPercent()).isNotNull();
              assertThat(postImageProductDetailResponse.getHasAvailableCoupon()).isNull();
            });
  }
}
