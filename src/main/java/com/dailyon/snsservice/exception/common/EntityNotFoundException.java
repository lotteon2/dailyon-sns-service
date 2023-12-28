package com.dailyon.snsservice.exception.common;

import javax.servlet.http.HttpServletResponse;

public class EntityNotFoundException extends DomainException {

  public EntityNotFoundException(String message) {
    super(message);
  }

  @Override
  public int getStatusCode() {
    return HttpServletResponse.SC_NOT_FOUND;
  }
}
