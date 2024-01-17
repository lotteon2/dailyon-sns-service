package com.dailyon.snsservice.exception.common;

import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;

public class EntityNotFoundException extends DomainException {

  public EntityNotFoundException(String message) {
    super(message);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.NOT_FOUND;
  }
}
