package com.thoainguyen;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@CucumberContextConfiguration
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.DEFINED_PORT)
public class SpringIntegrationTest {
  @When("^the client calls /baeldung$")
  public void the_client_issues_POST_hello() throws Throwable {
    System.out.println("the_client_issues_POST_hello");
  }

  @Given("^the client calls /hello$")
  public void the_client_issues_GET_hello() throws Throwable {
    System.out.println("the_client_issues_GET_hello");
  }

  @When("^the client calls /version$")
  public void the_client_issues_GET_version() throws Throwable {
    System.out.println("the_client_issues_GET_version");
  }

  @Then("^the client receives status code of (\\d+)$")
  public void the_client_receives_status_code_of(int statusCode) {
    System.out.println("the_client_issues_GET_version" + statusCode);
  }

  @And("^the client receives server version (.+)$")
  public void the_client_receives_server_version_body(String version) {
    System.out.println("the_client_receives_server_version_body" + version);
  }
}
