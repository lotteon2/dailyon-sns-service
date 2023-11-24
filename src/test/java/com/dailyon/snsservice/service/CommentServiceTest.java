package com.dailyon.snsservice.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.cache.PostCountRedisRepository;
import com.dailyon.snsservice.dto.request.comment.CreateCommentRequest;
import com.dailyon.snsservice.dto.request.comment.CreateReplyCommentRequest;
import com.dailyon.snsservice.dto.response.comment.CommentPageResponse;
import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import com.dailyon.snsservice.service.comment.CommentReader;
import com.dailyon.snsservice.service.comment.CommentService;
import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class CommentServiceTest {

  @Autowired private CommentService commentService;

  @Autowired private CommentReader commentReader;
  @Autowired private RedisTemplate<String, String> redisTemplate;
  @Autowired private ObjectMapper objectMapper;

  @Test
  @DisplayName("댓글 등록")
  void createComment() throws JsonProcessingException {
    // given
    Long memberId = 1L;
    Long postId = 1L;
    String commentDescription = "댓글 123";
    CreateCommentRequest createCommentRequest =
        CreateCommentRequest.builder().description(commentDescription).build();

    // when
    Comment savedComment = commentService.createComment(memberId, postId, createCommentRequest);

    // then
    PostCountVO afterPostCountVO =
        objectMapper.readValue(
            redisTemplate.opsForValue().get(String.format("postCount::%s", postId)),
            PostCountVO.class);

    assertThat(savedComment.getDescription()).isEqualTo(commentDescription);
    assertThat(savedComment.getPost().getId()).isEqualTo(postId);
    assertThat(savedComment.getMember().getId()).isEqualTo(memberId);
    assertThat(afterPostCountVO.getCommentCount())
        .isSameAs(13);
  }

  @Test
  @DisplayName("답글 등록")
  void createReplyComment() {
    // given
    Long memberId = 1L;
    Long postId = 3L;
    Long commentId = 2L;
    String replyCommentDescription = "댓글 123";
    CreateReplyCommentRequest createReplyCommentRequest =
        CreateReplyCommentRequest.builder().description(replyCommentDescription).build();

    // when
    Comment savedReplyComment =
        commentService.createReplyComment(memberId, postId, commentId, createReplyCommentRequest);

    // then
    assertThat(savedReplyComment.getDescription()).isEqualTo(replyCommentDescription);
    assertThat(savedReplyComment.getPost().getId()).isEqualTo(postId);
    assertThat(savedReplyComment.getMember().getId()).isEqualTo(memberId);
    assertThat(savedReplyComment.getParent()).isNotNull();
  }

  @Test
  @DisplayName("댓글 삭제")
  void deleteComment() {
    // given
    Long postId = 3L;
    Long memberId = 2L;
    Long parentCommentId = 2L;
    Long childCommentId = 8L;

    // when
    commentService.softDeleteComment(parentCommentId, postId, memberId);

    // then
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentReader.read(parentCommentId));
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentReader.read(childCommentId));
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
    assertThat(commentPageResponse.getTotalPages()).isSameAs(2);
    assertThat(commentPageResponse.getTotalElements()).isSameAs(6L);
    assertThat(commentPageResponse.getComments().size()).isSameAs(5);
  }
}
