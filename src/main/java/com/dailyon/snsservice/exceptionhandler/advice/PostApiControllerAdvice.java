package com.dailyon.snsservice.exceptionhandler.advice;

import com.dailyon.snsservice.controller.rest.PostApiController;
import com.dailyon.snsservice.exception.HashTagDuplicatedException;
import com.dailyon.snsservice.exceptionhandler.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackageClasses = PostApiController.class)
public class PostApiControllerAdvice {

  @ExceptionHandler(HashTagDuplicatedException.class)
  public ResponseEntity<ErrorResponse> HashTagDuplicatedException(HashTagDuplicatedException e) {
    HttpStatus statusCode = e.getStatusCode();

    ErrorResponse errorResponse =
        ErrorResponse.builder().code(statusCode).message("잘못된 요청입니다.").build();

    errorResponse.addValidation("hashTags", e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
