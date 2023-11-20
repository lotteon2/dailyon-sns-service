package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.request.post.CreateCommentRequest;
import com.dailyon.snsservice.dto.request.post.CreateReplyCommentRequest;
import com.dailyon.snsservice.dto.response.post.CommentPageResponse;
import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import com.dailyon.snsservice.repository.comment.CommentJpaRepository;
import com.dailyon.snsservice.repository.comment.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class CommentServiceTest {

  @Autowired private CommentService commentService;

  @Autowired private CommentRepository commentRepository;

  @Test
  @DisplayName("댓글 등록")
  void createComment() {
    // given
    Long memberId = 1L;
    Long postId = 1L;

    // when
    CreateCommentRequest createCommentRequest =
        CreateCommentRequest.builder().description("댓글 123").build();
    Comment savedComment = commentService.createComment(memberId, postId, createCommentRequest);

    // then
    assertSame(createCommentRequest.getDescription(), savedComment.getDescription());
  }

  @Test
  @DisplayName("답글 등록")
  void createReplyComment() {
    // given
    Long memberId = 1L;
    Long postId = 3L;
    Long commentId = 2L;

    // when
    CreateReplyCommentRequest createReplyCommentRequest =
        CreateReplyCommentRequest.builder().description("답글 123").build();
    Comment savedReplyComment =
        commentService.createReplyComment(memberId, postId, commentId, createReplyCommentRequest);

    // then
    assertSame(createReplyCommentRequest.getDescription(), savedReplyComment.getDescription());
    assertNotNull(savedReplyComment.getParent());
    assertSame(commentId, savedReplyComment.getParent().getId());
  }

  @Test
  @DisplayName("댓글 삭제")
  void deleteComment() {
    // given
    Long parentCommentId = 1L;
    Long childCommentId = 3L;

    // when
    commentService.deleteCommentById(parentCommentId);

    // then
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentRepository.findById(parentCommentId));
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentRepository.findById(childCommentId));
  }

  @Test
  @DisplayName("댓글 조회")
  void getComments() {
    // given
    Long postId = 3L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    CommentPageResponse commentPageResponse = commentService.getComments(postId, pageRequest);

    // then
    assertSame(2, commentPageResponse.getTotalPages());
    assertSame(5, commentPageResponse.getComments().size());
    assertSame(6L, commentPageResponse.getTotalElements());
  }
}
