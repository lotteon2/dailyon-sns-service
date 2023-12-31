package com.dailyon.snsservice.dto.request.comment;

import com.dailyon.snsservice.validator.CustomSize;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateReplyCommentRequest {

  @NotBlank(message = "답글을 입력해주세요.")
  @CustomSize(min = 5, max = 140, message = "답글은 최소 5글자 이상 최대 140글자 이하로 입력 가능합니다.")
  private String description;
}
