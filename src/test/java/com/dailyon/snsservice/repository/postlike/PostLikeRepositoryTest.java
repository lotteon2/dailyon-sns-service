package com.dailyon.snsservice.repository.postlike;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.entity.PostLike;
import com.dailyon.snsservice.entity.ids.PostLikeId;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.post.PostReader;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles(value = {"test"})
class PostLikeRepositoryTest {

  @Autowired private PostLikeRepository postLikeRepository;
  @Autowired private PostLikeJpaRepository postLikeJpaRepository;
  @Autowired private MemberReader memberReader;
  @Autowired private PostReader postReader;

  @Test
  @DisplayName("게시글 좋아요 추가")
  void createPostLike() {
    // given
    Long memberId = 1L;
    Long postId = 4L;
    Member member = memberReader.read(memberId);
    Post post = postReader.read(postId);

    // when
    int todoCalcLikeCount = postLikeRepository.togglePostLike(member, post);

    // then
    Optional<PostLike> postLike = postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
    assertThat(postLike.orElse(null)).isNotNull();
    assertThat(todoCalcLikeCount).isSameAs(1);
    // 처음 저장시 캐시에만 저장하고 배치로 DB에 반영
    assertThat(post.getLikeCount()).isSameAs(40);
  }

  @Test
  @DisplayName("게시글 좋아요 삭제")
  void deletePostLike() {
    // given
    Long memberId = 1L;
    Long postId = 2L;
    Member member = memberReader.read(memberId);
    Post post = postReader.read(postId);

    // when
    int todoCalcLikeCount = postLikeRepository.togglePostLike(member, post);

    // then
    Optional<PostLike> postLike = postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
    assertThat(postLike.orElse(null)).isNull();
    assertThat(todoCalcLikeCount).isSameAs(-1);
    // 처음 저장시 캐시에만 저장하고 배치로 DB에 반영
    assertThat(post.getLikeCount()).isSameAs(45);
  }
}
