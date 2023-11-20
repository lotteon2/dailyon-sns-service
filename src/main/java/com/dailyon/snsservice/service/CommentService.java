package com.dailyon.snsservice.service;

import com.dailyon.snsservice.dto.request.post.CreateCommentRequest;
import com.dailyon.snsservice.dto.request.post.CreateReplyCommentRequest;
import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.exception.MemberEntityNotFoundException;
import com.dailyon.snsservice.exception.PostEntityNotFoundException;
import com.dailyon.snsservice.repository.comment.CommentRepository;
import com.dailyon.snsservice.repository.member.MemberJpaRepository;
import com.dailyon.snsservice.repository.post.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

  private final MemberJpaRepository memberJpaRepository;
  private final PostJpaRepository postJpaRepository;
  private final CommentRepository commentRepository;

  public Comment createComment(
      Long memberId, Long postId, CreateCommentRequest createCommentRequest) {
    Member member =
        memberJpaRepository.findById(memberId).orElseThrow(MemberEntityNotFoundException::new);
    Post post = postJpaRepository.findById(postId).orElseThrow(PostEntityNotFoundException::new);

    Comment comment = Comment.createComment(member, post, createCommentRequest.getDescription());
    return commentRepository.save(comment);
  }

  public Comment createReplyComment(
      Long memberId,
      Long postId,
      Long commentId,
      CreateReplyCommentRequest createReplyCommentRequest) {
    Member member =
        memberJpaRepository.findById(memberId).orElseThrow(MemberEntityNotFoundException::new);
    Post post = postJpaRepository.findById(postId).orElseThrow(PostEntityNotFoundException::new);
    Comment comment = commentRepository.findById(commentId);

    Comment replyComment =
        Comment.createReplyComment(
            comment, member, post, createReplyCommentRequest.getDescription());
    return commentRepository.save(replyComment);
  }
}
