package com.dailyon.snsservice.mapper.postimageproductdetail;

import com.dailyon.snsservice.dto.request.post.CreatePostImageProductDetailRequest;
import com.dailyon.snsservice.entity.PostImageProductDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostImageProductDetailMapper {

  public Set<PostImageProductDetail> createPostImageProductDetails(
      List<CreatePostImageProductDetailRequest> createPostImageProductDetailRequests) {
    return createPostImageProductDetailRequests.stream()
        .map(
            pipdr ->
                PostImageProductDetail.createPostImageProductDetail(
                    pipdr.getProductId(),
                    pipdr.getProductSize(),
                    pipdr.getLeftGapPercent(),
                    pipdr.getTopGapPercent()))
        .collect(Collectors.toSet());
  }
}
