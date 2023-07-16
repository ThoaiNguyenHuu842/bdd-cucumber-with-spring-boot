package com.thoainguyen.domain;

import java.time.ZonedDateTime;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "email")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "title")
  private String title;
  @Column(name = "content")
  private String content;
  @OneToMany(mappedBy = "email", cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
  private Set<EmailContact> emailContacts;
  @Column(name = "created_date")
  private ZonedDateTime createdDate;
}
