package com.dailyon.snsservice.exception;

import com.dailyon.snsservice.exception.common.CustomException;
import org.springframework.http.HttpStatus;

public class HashTagDuplicatedException extends CustomException {

  private static final String message = "중복된 해시태그 이름입니다.";

  public HashTagDuplicatedException() {
    super(message);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.BAD_REQUEST;
  }
}
