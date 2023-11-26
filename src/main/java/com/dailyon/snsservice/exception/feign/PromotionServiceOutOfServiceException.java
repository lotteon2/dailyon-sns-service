package com.dailyon.snsservice.exception.feign;

import com.dailyon.snsservice.exception.common.CustomException;
import org.springframework.http.HttpStatus;

public class PromotionServiceOutOfServiceException extends CustomException {

  private static final String message = "프로모션 서비스에 문제가 있습니다.";

  public PromotionServiceOutOfServiceException() {
    super(message);
  }

  @Override
  public HttpStatus getStatusCode() {
    return HttpStatus.INTERNAL_SERVER_ERROR;
  }
}
