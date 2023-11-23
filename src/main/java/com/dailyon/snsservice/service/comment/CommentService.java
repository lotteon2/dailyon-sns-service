package com.dailyon.snsservice.service.comment;

import com.dailyon.snsservice.cache.PostCountRedisRepository;
import com.dailyon.snsservice.dto.request.comment.CreateCommentRequest;
import com.dailyon.snsservice.dto.request.comment.CreateReplyCommentRequest;
import com.dailyon.snsservice.dto.response.comment.CommentPageResponse;
import com.dailyon.snsservice.entity.Comment;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.repository.comment.CommentRepository;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.post.PostReader;
import com.dailyon.snsservice.vo.PostCountVO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

  private final MemberReader memberReader;
  private final PostReader postReader;
  private final CommentReader commentReader;
  private final CommentRepository commentRepository;
  private final PostCountRedisRepository postCountRedisRepository;

  @Transactional
  public Comment createComment(
      Long memberId, Long postId, CreateCommentRequest createCommentRequest) {
    Member member = memberReader.read(memberId);
    Post post = postReader.read(postId);

    Comment comment = Comment.createComment(member, post, createCommentRequest.getDescription());
    try {
      // cache hit: 기존의 캐시에 들어있는 count 반환
      // cache miss: count + 1 해서 캐시에 넣은 후 반환
      PostCountVO postCountVO =
          postCountRedisRepository.findOrPutPostCountVO(
              String.valueOf(postId),
              new PostCountVO(
                  post.getViewCount(), post.getLikeCount(), post.getCommentCount() + 1));
      // 이미 캐시에 존재하는 값이라면 업데이트
      if (postCountVO.getCommentCount().equals(post.getCommentCount() + 1)) {
        postCountVO.addCommentCount();
        postCountRedisRepository.modifyPostCountVOAboutLikeCount(
            String.valueOf(postId), postCountVO);
      }
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }

    return commentRepository.save(comment);
  }

  @Transactional
  public Comment createReplyComment(
      Long memberId,
      Long postId,
      Long commentId,
      CreateReplyCommentRequest createReplyCommentRequest) {
    Member member = memberReader.read(memberId);
    Post post = postReader.read(postId);
    Comment comment = commentReader.read(commentId);

    Comment replyComment =
        Comment.createReplyComment(
            comment, member, post, createReplyCommentRequest.getDescription());
    return commentRepository.save(replyComment);
  }

  @Transactional
  public void deleteCommentById(Long commentId) {
    commentRepository.deleteById(commentId);
  }

  public CommentPageResponse getComments(Long postId, Pageable pageable) {
    Page<Comment> comments = commentRepository.findAllByPostId(postId, pageable);
    return CommentPageResponse.fromEntity(comments);
  }
}
