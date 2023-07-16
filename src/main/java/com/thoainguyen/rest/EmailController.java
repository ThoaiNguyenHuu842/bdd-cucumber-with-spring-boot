package com.thoainguyen.rest;

import com.thoainguyen.config.BaseController;
import com.thoainguyen.dto.SendEmailDto;
import com.thoainguyen.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController extends BaseController {

  private final EmailService emailService;

  @GetMapping("/version")
  public String getVersion() {
    return "1.0";
  }

  @PostMapping("/emails/send")
  public ResponseEntity sendEmail(@RequestBody SendEmailDto sendEmailDto) {
    try {
      emailService.sendEmail(sendEmailDto);
      return ResponseEntity.ok().build();
    }
    catch (Exception ex) {
      return handleInternalServerError(ex);
    }
  }

  @PostMapping("/emails/{emailId}/contacts/{contactId}/click")
  public ResponseEntity clickEmail(@PathVariable Long emailId, @PathVariable  Long contactId) {
    try {
      emailService.clickEmail(emailId,contactId);
      return ResponseEntity.ok().build();
    }
    catch (Exception ex) {
      return handleInternalServerError(ex);
    }
  }
}
