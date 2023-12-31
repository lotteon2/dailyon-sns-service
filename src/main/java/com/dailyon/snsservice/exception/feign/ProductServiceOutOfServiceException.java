package com.dailyon.snsservice.exception.feign;

public class ProductServiceOutOfServiceException extends RuntimeException {

  private static final String MESSAGE = "상품 서비스에 문제가 있습니다.";

  public ProductServiceOutOfServiceException() {
    super(MESSAGE);
  }
}
