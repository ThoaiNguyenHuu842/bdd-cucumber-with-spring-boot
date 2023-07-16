package com.thoainguyen.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AppExceptionHandlerImpl implements AppExceptionHandler {

  @Override
  public ResponseEntity handleException(Exception e) {
    if (e instanceof IllegalArgumentException) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        Error.builder().errorCode(String.valueOf(HttpStatus.BAD_REQUEST.value()))
          .errorMessage(e.getMessage()).build());
    }
    e.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
      Error.builder().errorCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
        .errorMessage(e.getMessage()).build());
  }
}
