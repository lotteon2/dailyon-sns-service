package com.dailyon.snsservice.dto.request.post;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreatePostRequest {

  @NotBlank(message = "제목을 입력해주세요.")
  @Size(min = 5, max = 50, message = "제목은 최소 5글자 이상 최대 50글자 이하로 입력 가능합니다.")
  private String title;

  @Size(max = 300, message = "본문은 최대 300글자 이하로 입력 가능합니다.")
  private String description;

  private Double stature;
  private Double weight;

  @NotEmpty(message = "해시태그는 최소 1개 이상 입력해야 합니다.")
  private List<String> hashTagNames;

  @NotBlank(message = "썸네일 이미지를 등록해주세요.")
  private String postThumbnailImgName;

  @NotBlank(message = "이미지를 등록해주세요.")
  private String postImgName;

  @Valid
  @Size(max = 5, message = "상품 상세는 최대 5개까지 태그 가능합니다.")
  private List<CreatePostImageProductDetailRequest> postImageProductDetails;
}
