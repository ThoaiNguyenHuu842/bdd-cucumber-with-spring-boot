package com.thoainguyen.service;

import com.thoainguyen.dto.SendEmailDto;

public interface EmailService {
  void sendEmail(SendEmailDto sendEmailDto);

  void clickEmail(Long emailId, Long contactId);
}
