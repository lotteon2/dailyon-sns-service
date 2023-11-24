package com.dailyon.snsservice.repository.comment;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import com.dailyon.snsservice.service.comment.CommentReader;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.post.PostReader;
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
class CommentRepositoryTest {

  @Autowired private MemberReader memberReader;
  @Autowired private PostReader postReader;
  @Autowired private CommentReader commentReader;
  @Autowired private CommentRepository commentRepository;

  @Test
  @DisplayName("댓글 등록")
  void save() {
    // given
    Long memberId = 1L;
    Long postId = 1L;
    String commentDescription = "댓글 123";
    Member member = memberReader.read(memberId);
    Post post = postReader.read(postId);

    // when
    Comment comment = Comment.createComment(member, post, commentDescription);
    Comment savedComment = commentRepository.save(comment);

    // then
    assertThat(savedComment.getDescription()).isEqualTo(commentDescription);
    assertThat(savedComment.getPost().getId()).isEqualTo(postId);
    assertThat(savedComment.getMember().getId()).isEqualTo(memberId);
  }

  @Test
  @DisplayName("댓글 삭제")
  void softDeleteById() {
    // given
    Long postId = 3L;
    Long memberId = 2L;
    Long parentCommentId = 2L;
    Long childCommentId = 8L;

    // when
    commentRepository.softDeleteById(parentCommentId, postId, memberId);

    // then
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentReader.read(parentCommentId));
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentReader.read(childCommentId));
  }

  @Test
  @DisplayName("댓글 조회")
  void findAllByPostId() {
    // given
    Long postId = 3L;
    PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createdAt"));

    // when
    Page<Comment> comments = commentRepository.findAllByPostId(postId, pageRequest);

    // then
    assertSame(2, comments.getTotalPages());
    assertSame(5, comments.getContent().size());
    assertSame(6L, comments.getTotalElements());
    comments.getContent().stream().forEach(comment -> assertNull(comment.getParent()));
  }
}
