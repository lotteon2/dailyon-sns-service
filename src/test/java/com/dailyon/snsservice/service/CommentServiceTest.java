package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.request.post.CreateCommentRequest;
import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.repository.comment.CommentJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class CommentServiceTest {

  @Autowired
  private CommentService commentService;

  @Test
  @DisplayName("댓글 등록")
  void createComment() {
    // given
    Long memberId = 1L;
    Long postId = 1L;

    // when
    CreateCommentRequest createCommentRequest = CreateCommentRequest.builder()
            .description("댓글 123")
            .build();
    Comment savedComment = commentService.createComment(memberId, postId, createCommentRequest);

    // then
    assertSame(createCommentRequest.getDescription(), savedComment.getDescription());
  }
}
