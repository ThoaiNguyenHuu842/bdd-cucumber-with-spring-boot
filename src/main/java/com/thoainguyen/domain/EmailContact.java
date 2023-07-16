package com.thoainguyen.domain;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name= "email_contact")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailContact {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(name="email_id", nullable=false)
  private Email email;
  @ManyToOne
  @JoinColumn(name="contact_id", nullable=false)
  private Contact contact;
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private SendEmailStatus sendEmailStatus;
  @Column(name = "created_date")
  private ZonedDateTime createdDate;
  @Column(name = "opened_date")
  private ZonedDateTime openedDate;
}
