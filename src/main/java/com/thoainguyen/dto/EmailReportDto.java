package com.thoainguyen.dto;

import java.time.ZonedDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailReportDto {
  private Long id;
  private String title;
  private String content;
  private ZonedDateTime createdDate;
  private Integer totalContacts;
  private Integer totalSent;
  private Integer totalError;
  private Integer totalOpened;

  public boolean compare(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmailReportDto that = (EmailReportDto) o;
    return Objects.equals(title, that.title) && Objects
      .equals(content, that.content) && Objects.equals(totalContacts, that.totalContacts)
      && Objects.equals(totalSent, that.totalSent) && Objects
      .equals(totalError, that.totalError) && Objects.equals(totalOpened, that.totalOpened);
  }
}
