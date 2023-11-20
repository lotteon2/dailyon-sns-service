package com.dailyon.snsservice.repository.comment;

import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.exception.CommentEntityNotFoundException;
import com.dailyon.snsservice.repository.member.MemberJpaRepository;
import com.dailyon.snsservice.repository.post.PostJpaRepository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class CommentRepositoryTest {

  @PersistenceContext private EntityManager em;

  @Autowired private PostJpaRepository postJpaRepository;
  @Autowired private MemberJpaRepository memberJpaRepository;
  @Autowired private CommentRepository commentRepository;

  @Test
  @DisplayName("댓글 등록")
  void save() {
    // given
    Long memberId = 1L;
    Long postId = 1L;
    Member member = memberJpaRepository.findById(memberId).get();
    Post post = postJpaRepository.findById(postId).get();

    // when
    Comment comment = Comment.createComment(member, post, "댓글 123");
    Comment savedComment = commentRepository.save(comment);

    // then
    assertSame(comment.getDescription(), savedComment.getDescription());
  }

  @Test
  @DisplayName("댓글 삭제")
  void deleteById() {
    // given
    Long parentCommentId = 1L;
    Long childCommentId = 3L;

    // when
    commentRepository.deleteById(parentCommentId);

    // then
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentRepository.findById(parentCommentId));
    assertThrowsExactly(
        CommentEntityNotFoundException.class, () -> commentRepository.findById(childCommentId));
  }
}
