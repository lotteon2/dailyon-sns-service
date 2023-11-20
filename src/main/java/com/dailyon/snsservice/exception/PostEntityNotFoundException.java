package com.dailyon.snsservice.exception;

import com.dailyon.snsservice.exception.common.EntityNotFoundException;

public class PostEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "게시글 정보를 찾을 수 없습니다.";

  public PostEntityNotFoundException() {
    super(message);
  }
}
