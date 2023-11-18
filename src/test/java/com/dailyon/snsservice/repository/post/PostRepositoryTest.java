package com.dailyon.snsservice.repository.post;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.entity.*;
import java.util.List;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
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

  @PersistenceContext private EntityManager em;
  @Autowired private PostRepository postRepository;

  @BeforeEach
  void beforeEach() {
    Member member = Member.createMember(1L, "member1", UUID.randomUUID().toString());
    em.persist(member);

    for (int i = 1; i <= 16; i++) {
      List<PostImageProductDetail> postImageProductDetails =
          List.of(
              PostImageProductDetail.createPostImageProductDetail(
                  Integer.toUnsignedLong(i), "size", 10.0, 10.0));

      PostImage postImage =
          PostImage.createPostImage(
              String.format("/images/thumbnail%s.png", i),
              String.format("/images/image%s.png", i),
              postImageProductDetails);

      List<HashTag> hashTags = List.of(HashTag.createHashTag("태그" + i));

      Post post =
          Post.createPost(
              member,
              String.format("post %s", i),
              String.format("post %s desc", i),
              5.6,
              150.0,
              postImage,
              hashTags);
      em.persist(post);

      if (i % 2 == 0) {
        PostLike postLike = PostLike.createPostLike(member, post);
        post.addLikeCount(1);
        em.persist(postLike);
      }
    }
  }

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
    assertSame(0, posts.getContent().get(1).getPostLikes().size());
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
    assertSame(1, posts.getContent().get(0).getPostLikes().size());
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
    List<PostImageProductDetail> postImageProductDetails =
        List.of(PostImageProductDetail.createPostImageProductDetail(1L, "size", 10.0, 10.0));

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
}
