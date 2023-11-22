package com.dailyon.snsservice.exceptionhandler.advice;

import com.dailyon.snsservice.exception.common.CustomException;
import com.dailyon.snsservice.exceptionhandler.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> customException(CustomException e) {
    HttpStatus statusCode = e.getStatusCode();

    ErrorResponse errorResponse =
        ErrorResponse.builder()
            .code(statusCode)
            .message(e.getMessage())
            .validation(e.getValidation())
            .build();

    return ResponseEntity.status(statusCode).body(errorResponse);
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ResponseEntity<ErrorResponse> missingRequestHeaderException(
      MissingRequestHeaderException e) {
    ErrorResponse errorResponse;
    if (e.getHeaderName().equals("memberId")) {
      errorResponse =
          ErrorResponse.builder().code(HttpStatus.UNAUTHORIZED).message("로그인이 필요합니다.").build();
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    } else {
      errorResponse =
          ErrorResponse.builder().code(HttpStatus.BAD_REQUEST).message("잘못된 요청입니다.").build();
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> methodArgumentNotValidException(
      MethodArgumentNotValidException e) {
    ErrorResponse errorResponse =
        ErrorResponse.builder().code(HttpStatus.BAD_REQUEST).message("잘못된 요청입니다.").build();

    for (FieldError fieldError : e.getFieldErrors()) {
      errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
    }
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}
