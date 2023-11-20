package com.dailyon.snsservice.exception;

import com.dailyon.snsservice.exception.common.EntityNotFoundException;

public class CommentEntityNotFoundException extends EntityNotFoundException {

  private static final String message = "댓글 정보를 찾을 수 없습니다.";

  public CommentEntityNotFoundException() {
    super(message);
  }
}
