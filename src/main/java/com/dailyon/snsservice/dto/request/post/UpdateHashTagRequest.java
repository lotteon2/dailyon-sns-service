package com.dailyon.snsservice.dto.request.post;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateHashTagRequest {

  @NotNull(message = "해시태그의 실제 id를 입력해주세요.")
  private Long id;

  @NotBlank(message = "해시태그를 입력해주세요.")
  @Size(min = 1, max = 20, message = "해시태그 이름은 최소 1글자 이상 최대 20글자 이하로 입력 가능합니다.")
  private String name;
}
