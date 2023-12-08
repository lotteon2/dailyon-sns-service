package com.dailyon.snsservice.exception.feign;

public class PromotionServiceOutOfServiceException extends RuntimeException {

  private static final String MESSAGE = "프로모션 서비스에 문제가 있습니다.";

  public PromotionServiceOutOfServiceException() {
    super(MESSAGE);
  }
}
