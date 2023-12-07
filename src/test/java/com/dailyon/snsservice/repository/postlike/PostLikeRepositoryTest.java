package com.dailyon.snsservice.repository.postlike;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.entity.PostLike;
import com.dailyon.snsservice.entity.ids.PostLikeId;
import com.dailyon.snsservice.service.member.MemberReader;
import com.dailyon.snsservice.service.post.PostReader;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
class PostLikeRepositoryTest {

  @Autowired private PostLikeRepository postLikeRepository;
  @Autowired private PostLikeJpaRepository postLikeJpaRepository;
  @Autowired private MemberReader memberReader;
  @Autowired private PostReader postReader;
  @PersistenceContext private EntityManager em;

  @Test
  @DisplayName("게시글 좋아요 벌크 연산")
  void deletePostLikeV2() {
    // given
    Long memberId = 1L;
    List<Long> postIds = List.of(1L, 2L, 3L, 4L);
    Member member = memberReader.read(memberId);
    List<Post> posts = postReader.readAll(postIds);
    // when
    Map<Long, Integer> countMap = postLikeRepository.togglePostLike(member, posts);

    em.flush();
    em.clear();

    // then
    for (Long postId : postIds) {
      Optional<PostLike> postLike =
          postLikeJpaRepository.findById(new PostLikeId(memberId, postId));
      if (postId.equals(2L)) {
        assertThat(postLike.orElse(null)).isNull();
        assertThat(countMap.get(postId)).isSameAs(-1);
      } else {
        assertThat(postLike.orElse(null)).isNotNull();
        assertThat(countMap.get(postId)).isSameAs(1);
      }
    }
  }
}
