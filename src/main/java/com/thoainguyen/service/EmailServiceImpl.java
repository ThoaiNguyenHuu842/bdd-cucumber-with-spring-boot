package com.thoainguyen.service;

import com.thoainguyen.domain.Contact;
import com.thoainguyen.domain.Email;
import com.thoainguyen.domain.EmailContact;
import com.thoainguyen.domain.SendEmailStatus;
import com.thoainguyen.dto.SendEmailDto;
import com.thoainguyen.repository.ContactRepository;
import com.thoainguyen.repository.EmailContactRepository;
import com.thoainguyen.repository.EmailRepository;
import java.time.ZonedDateTime;
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
  public void clickEmail(Long emailId, Long contactId) {
    EmailContact emailContact = emailContactRepository.findOneByEmailIdAndContactId(emailId, contactId);
    if (emailContact == null) {
      throw new IllegalArgumentException("Invalid emailId and contactId");
    }
    emailContact.setSendEmailStatus(SendEmailStatus.OPENED);
    emailContact.setOpenedDate(ZonedDateTime.now());
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
}
