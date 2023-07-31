package com.thoainguyen.repository;

import com.thoainguyen.domain.Email;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
  List<Email> findByTitleIn(Set<String> title);
}
