package com.dailyon.snsservice.mapper.postimage;

import com.dailyon.snsservice.entity.PostImage;
import com.dailyon.snsservice.entity.PostImageProductDetail;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostImageMapper {

  public PostImage createPostImage(
      String thumbnailImgUrl, String imgUrl, Set<PostImageProductDetail> postImageProductDetails) {
    return PostImage.createPostImage(thumbnailImgUrl, imgUrl, postImageProductDetails);
  }
}
