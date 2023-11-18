package com.dailyon.snsservice.dto.response.post;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreatePostResponse {

    private String thumbnailImgPreSignedUrl;
    private String imgPreSignedUrl;
}
