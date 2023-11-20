package com.dailyon.snsservice.dto.request.post;

import java.util.List;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdatePostRequest {

  @NotBlank(message = "제목을 입력해주세요.")
  @Size(min = 5, max = 50, message = "제목은 최소 5글자 이상 최대 50글자 이하로 입력 가능합니다.")
  private String title;

  @Size(max = 300, message = "본문은 최대 300글자 이하로 입력 가능합니다.")
  private String description;

  private Double stature;
  private Double weight;

  @NotEmpty(message = "해시태그는 최소 1개 이상 입력해야 합니다.")
  private List<String> hashTagNames;

  @AssertTrue(message = "썸네일 이미지를 등록해주세요.")
  private Boolean isPostThumbnailImgExists;

  @AssertTrue(message = "이미지를 등록해주세요.")
  private Boolean isPostImgExists;

  private List<UpdatePostImageProudctDetailRequest> postImageProductDetails;
}