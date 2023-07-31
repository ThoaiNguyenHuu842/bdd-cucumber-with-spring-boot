Feature: send email

  Background:
    Given the below contacts exists
      | Name  | Email           | Unsubscribed |
      | Thoai | thoai@gmail.com | false        |
      | Ti    | ti@gmail.com    | false        |
      | Teo   | teo@gmail.com   | true         |

  Scenario: send an email to subscribed contacts
    When the email with title "hello" and content "how are you" is sent to below emails
      | thoai@gmail.com |
      | ti@gmail.com    |
    Then the below report of email with title "hello" exists
      | Title | Content     | Total Contacts | Total Sent | Total Error | Total Opened |
      | hello | how are you | 2              | 2          | 0           | 0            |

  Scenario: send an email to subscribed contacts, then two contacts open email
    When the email with title "hello" and content "how are you" is sent to below emails
      | thoai@gmail.com |
      | ti@gmail.com    |
    And contact "ti@gmail.com" clicks an email with title "hello"
    And contact "thoai@gmail.com" clicks an email with title "hello"
    Then the below report of email with title "hello" exists
      | Title | Content     | Total Contacts | Total Sent | Total Error | Total Opened |
      | hello | how are you | 2              | 0          | 0           | 2            |

  Scenario: send an email to unsubscribed contact
    When the email with title "hello" and content "how are you" is sent to below emails
      | thoai@gmail.com |
      | teo@gmail.com   |
    Then the below report of email with title "hello" exists
      | Title | Content     | Total Contacts | Total Sent | Total Error | Total Opened |
      | hello | how are you | 2              | 1          | 1           | 0            |