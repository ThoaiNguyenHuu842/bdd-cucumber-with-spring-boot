package com.thoainguyen.service;

import com.thoainguyen.domain.Contact;
import com.thoainguyen.domain.Email;
import com.thoainguyen.domain.EmailContact;
import com.thoainguyen.domain.SendEmailStatus;
import com.thoainguyen.dto.EmailReportDto;
import com.thoainguyen.dto.SendEmailDto;
import com.thoainguyen.repository.ContactRepository;
import com.thoainguyen.repository.EmailContactRepository;
import com.thoainguyen.repository.EmailRepository;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {

  private final ContactRepository contactRepository;
  private final EmailRepository emailRepository;
  private final EmailContactRepository emailContactRepository;

  @Override
  public void sendEmail(SendEmailDto sendEmailDto) {
    validateSendEmailDto(sendEmailDto);

    Set<String> emails = sendEmailDto.getEmails();
    Set<Contact> contacts = contactRepository.findByEmailIn(emails);
    if (contacts.size() != emails.size()) {
      emails.removeAll(contacts.stream().map(Contact::getEmail).collect(
        Collectors.toSet()));
      throw new IllegalArgumentException(
        "Some email(s) can not be found: " + String.join(",", emails));
    }

    Email email = Email.builder()
      .title(sendEmailDto.getTitle())
      .content(sendEmailDto.getContent())
      .createdDate(ZonedDateTime.now())
      .build();

    Set<EmailContact> emailContacts = contacts.stream().map(contact -> EmailContact.builder()
      .contact(contact)
      .email(email)
      .createdDate(ZonedDateTime.now())
      .sendEmailStatus(contact.isUnsubscribed() ? SendEmailStatus.ERROR : SendEmailStatus.SENT)
      .build())
      .collect(Collectors.toSet());

    email.setEmailContacts(emailContacts);
    emailRepository.save(email);
  }

  @Override
  public void openEmail(Long emailId, Long contactId) {
    EmailContact emailContact = emailContactRepository
      .findOneByEmailIdAndContactId(emailId, contactId);

    if (emailContact == null) {
      throw new IllegalArgumentException("Invalid emailId and contactId");
    }

    if (emailContact.getSendEmailStatus().equals(SendEmailStatus.ERROR)) {
      throw new IllegalArgumentException("The email was sent unsuccessfully to this contact");
    }

    emailContact.setSendEmailStatus(SendEmailStatus.OPENED);
    emailContact.setOpenedDate(ZonedDateTime.now());
  }

  @Override
  public EmailReportDto getEmailReport(Long emailId) {
    Email email = emailRepository.findById(emailId)
      .orElseThrow(() -> new IllegalArgumentException("Invalid emailId"));
    return convertEmailReportDto(email);
  }

  private EmailReportDto convertEmailReportDto(Email email) {
    Set<EmailContact> emailContacts = email.getEmailContacts();

    Map<SendEmailStatus, List<EmailContact>> sendEmailStatusListMap =
      emailContacts.stream()
        .collect(Collectors.groupingBy(EmailContact::getSendEmailStatus,
          Collectors.mapping(d -> d, Collectors.toList())));

    return EmailReportDto.builder()
      .title(email.getTitle())
      .content(email.getContent())
      .createdDate(email.getCreatedDate())
      .totalContacts(emailContacts.size())
      .totalError(getTotal(SendEmailStatus.ERROR, sendEmailStatusListMap))
      .totalSent(getTotal(SendEmailStatus.SENT, sendEmailStatusListMap))
      .totalOpened(getTotal(SendEmailStatus.OPENED, sendEmailStatusListMap))
      .build();
  }

  private int getTotal(SendEmailStatus status, Map<SendEmailStatus, List<EmailContact>> sendEmailStatusListMap) {
    List<EmailContact> emailContacts = sendEmailStatusListMap.get(status);
    if (emailContacts == null) {
      return 0;
    } else {
     return emailContacts.size();
    }
  }

  private void validateSendEmailDto(SendEmailDto sendEmailDto) {
    if (StringUtils.isEmpty(sendEmailDto.getTitle())) {
      throw new IllegalArgumentException("Title can not be null");
    }
    if (StringUtils.isEmpty(sendEmailDto.getContent())) {
      throw new IllegalArgumentException("Contact can not be null");
    }
    if (CollectionUtils.isEmpty(sendEmailDto.getEmails())) {
      throw new IllegalArgumentException("Emails can not be empty");
    }
  }

  public static void main(String[] args) {
    BigDecimal bigDecimal = new BigDecimal("100.1050000000000000");
    System.out.println(String.valueOf(bigDecimal));
    System.out.println(new DecimalFormat("#0").format(bigDecimal));
  }
}
