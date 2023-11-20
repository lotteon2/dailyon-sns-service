package com.dailyon.snsservice.dto.request.post;


import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateCommentRequest {

    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(min = 5, max = 140, message = "댓글은 최소 5글자 이상 최대 140글자 이하로 입력 가능합니다.")
    private String description;
}
