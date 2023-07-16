package com.thoainguyen.repository;

import com.thoainguyen.domain.EmailContact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailContactRepository  extends JpaRepository<EmailContact, Long> {
  EmailContact findOneByEmailIdAndContactId(Long emailId, Long contactId);
}
