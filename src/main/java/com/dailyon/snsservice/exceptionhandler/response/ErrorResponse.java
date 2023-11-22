package com.dailyon.snsservice.exceptionhandler.response;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse {

  private final HttpStatus code;

  private final String message;

  private final Map<String, String> validation;

  @Builder
  public ErrorResponse(HttpStatus code, String message, Map<String, String> validation) {
    this.code = code;
    this.message = message;
    this.validation = (validation != null ? validation : new HashMap<>());
  }

  public void addValidation(String field, String errorMessage) {
    this.validation.put(field, errorMessage);
  }
}
