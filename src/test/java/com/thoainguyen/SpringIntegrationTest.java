package com.thoainguyen;

import com.thoainguyen.domain.Contact;
import com.thoainguyen.dto.EmailReportDto;
import com.thoainguyen.dto.SendEmailDto;
import com.thoainguyen.repository.ContactRepository;
import com.thoainguyen.repository.EmailContactRepository;
import com.thoainguyen.repository.EmailRepository;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringIntegrationTest {

  @Autowired
  private ContactRepository contactRepository;
  @Autowired
  private EmailRepository emailRepository;
  @Autowired
  private EmailContactRepository emailContactRepository;

  @Autowired
  private RestTemplate restTemplate;

  private final String baseUrl = "http://localhost:8080";

  @Before
  public void initialization() {
    emailContactRepository.deleteAll();
    emailRepository.deleteAll();
    contactRepository.deleteAll();
  }

  @Given("^the below contacts exists")
  public void createContact(DataTable table) {
    List<List<String>> rows = table.asLists(String.class);
    List<Contact> contacts = new ArrayList<>();

    for (int i = 1; i < rows.size(); i++) {
      List<String> columns = rows.get(i);
      contacts.add(Contact.builder()
        .name(columns.get(0))
        .email(columns.get(1))
        .unsubscribed(Boolean.parseBoolean(columns.get(2)))
        .build());
    }
    contactRepository.saveAll(contacts);
  }

  @When("^the email with title \"([^\"]*)\" and content \"([^\"]*)\" is sent to below emails")
  public void sendEmail(String title, String content, DataTable table) {
    Set<String> emails = table.asLists(String.class).stream().map(columns -> columns.get(0)).collect(
      Collectors.toSet());
    SendEmailDto sendEmailDto = SendEmailDto.builder()
      .content(content)
      .title(title)
      .emails(emails)
      .build();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<SendEmailDto> request = new HttpEntity<>(sendEmailDto, headers);
    restTemplate.postForObject(baseUrl + "/emails/send", request, Void.class);
  }

  @When("^contact \"([^\"]*)\" clicks an email with title \"([^\"]*)\"")
  public void openEmail(String email, String emailTitle) {
    Long emailId = emailRepository.findByTitleIn(Set.of(emailTitle)).get(0).getId();
    Long contactId = contactRepository.findByEmailIn(Set.of(email)).iterator().next().getId();
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/emails/{emailId}/contacts/{contactId}/open").buildAndExpand(emailId, contactId).toUri();
    restTemplate.postForObject(uri, null, Void.class);
  }

  @When("^the below report of email with title \"([^\"]*)\" exists")
  public void getReport(String emailTitle, DataTable table) {
    Long emailId = emailRepository.findByTitleIn(Set.of(emailTitle)).get(0).getId();
    URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/emails/{emailId}/report").buildAndExpand(emailId).toUri();
    EmailReportDto actualData = restTemplate.getForObject(uri, EmailReportDto.class);

    EmailReportDto expectedData = new EmailReportDto();
    List<String> columns = table.asLists(String.class).get(1);
    expectedData.setTitle(columns.get(0));
    expectedData.setContent(columns.get(1));
    expectedData.setTotalContacts(Integer.parseInt(columns.get(2)));
    expectedData.setTotalSent(Integer.parseInt(columns.get(3)));
    expectedData.setTotalError(Integer.parseInt(columns.get(4)));
    expectedData.setTotalOpened(Integer.parseInt(columns.get(5)));

    Assertions.assertTrue(expectedData.compare(actualData));
  }

  private final class ContactDto {
    public String name;
    public String email;
    public boolean unsubscribed;
  }
}
