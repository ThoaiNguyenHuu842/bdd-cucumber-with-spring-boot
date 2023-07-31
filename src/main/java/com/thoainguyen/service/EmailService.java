package com.thoainguyen.service;

import com.thoainguyen.dto.EmailReportDto;
import com.thoainguyen.dto.SendEmailDto;

public interface EmailService {
  void sendEmail(SendEmailDto sendEmailDto);

  void openEmail(Long emailId, Long contactId);

  EmailReportDto getEmailReport(Long emailId);
}
