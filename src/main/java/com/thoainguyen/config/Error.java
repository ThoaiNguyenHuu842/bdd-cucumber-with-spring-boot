package com.thoainguyen.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Error {
  private String errorCode;
  private String errorMessage;
}