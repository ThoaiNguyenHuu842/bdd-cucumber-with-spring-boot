package com.thoainguyen.repository;

import com.thoainguyen.domain.Contact;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
  Set<Contact> findByEmailIn(Set<String> emails);
}
