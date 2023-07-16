package com.thoainguyen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class BaseController {
  @Autowired
  private AppExceptionHandler appExceptionHandler;

  public ResponseEntity handleInternalServerError(Exception e) {
    return appExceptionHandler.handleException(e);
  }
}
