package com.thoainguyen.config;

import org.springframework.http.ResponseEntity;

public interface AppExceptionHandler {
  ResponseEntity<Error> handleException(Exception e);
}