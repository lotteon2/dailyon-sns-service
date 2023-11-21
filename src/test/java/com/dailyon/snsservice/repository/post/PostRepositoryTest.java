package com.dailyon.snsservice.repository.post;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.entity.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostRepositoryTest {

  @PersistenceContext private EntityManager em;
  @Autowired private PostRepository postRepository;

  @Test
  @DisplayName("게시글 목록 조회 - 미인증")
  void findAllWithoutAuth() {
    // given
    Long memberId = null;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));

    // when
    Page<Post> posts = postRepository.findAllWithIsLike(memberId, pageRequest);

    // then
    assertSame(16L, posts.getTotalElements());
    assertSame(2, posts.getTotalPages());
  }

  @Test
  @DisplayName("게시글 목록 조회 - 인증")
  void findAllWithAuth() {
    // given
    Long memberId = 1L;
    PageRequest pageRequest = PageRequest.of(0, 8, Sort.by(Sort.Direction.DESC, "viewCount"));

    // when
    Page<Post> posts = postRepository.findAllWithIsLike(memberId, pageRequest);

    // then
    assertSame(16L, posts.getTotalElements());
    assertSame(2, posts.getTotalPages());
  }

  @Test
  @DisplayName("게시글 등록")
  void save() {
    // given
    Long memberId = 1L;
    Member member =
        em.createQuery("select m from Member m where m.id = :memberId", Member.class)
            .setParameter("memberId", memberId)
            .getSingleResult();
    Set<PostImageProductDetail> postImageProductDetails =
        Set.of(PostImageProductDetail.createPostImageProductDetail(1L, "size", 10.0, 10.0));

    PostImage postImage =
        PostImage.createPostImage(
            "/images/thumbnail.png", "/images/img.png", postImageProductDetails);

    List<HashTag> hashTags = List.of(HashTag.createHashTag("태그 1"));

    Post post = Post.createPost(member, "post 1", "post desc 1", 5.6, 150.0, postImage, hashTags);

    // when
    Post savedPost = postRepository.save(post);

    // then
    assertEquals(post.getTitle(), savedPost.getTitle());
    assertEquals(post.getDescription(), savedPost.getDescription());
    assertEquals(
        post.getPostImage().getThumbnailImgUrl(), savedPost.getPostImage().getThumbnailImgUrl());
    assertEquals(post.getPostImage().getImgUrl(), savedPost.getPostImage().getImgUrl());
    assertEquals(
        post.getPostImage().getPostImageProductDetails().size(),
        savedPost.getPostImage().getPostImageProductDetails().size());
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
}
