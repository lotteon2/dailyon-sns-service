package com.dailyon.snsservice.mapper.post;

import com.dailyon.snsservice.dto.request.post.CreatePostRequest;
import com.dailyon.snsservice.entity.HashTag;
import com.dailyon.snsservice.entity.Member;
import com.dailyon.snsservice.entity.Post;
import com.dailyon.snsservice.entity.PostImage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

  public Post createPost(
      CreatePostRequest createPostRequest,
      Member member,
      PostImage postImage,
      List<HashTag> hashTags) {
    return Post.createPost(
        member,
        createPostRequest.getTitle(),
        createPostRequest.getDescription(),
        createPostRequest.getStature(),
        createPostRequest.getWeight(),
        postImage,
        hashTags);
  }
}
