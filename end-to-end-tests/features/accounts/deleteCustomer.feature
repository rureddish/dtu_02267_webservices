Feature: deleting a customer
  Scenario: Delete customer successfully
    Given a customer with first name "George" last name "Michael" CPR "1234567890" and bank a account with balance 500
    And the customer is registered to DTUPay
    When the customer gets his DTUPay account
    Then no error occurs
    Then the customer is deleted

  Scenario: Delete customer unsuccessfully
    Given a customer with first name "George" last name "Michael" CPR "1234567890" and bank a account with balance 500
    And the customer is registered to DTUPay
    Given a customer with account id "nosuchcustomer"
    When the customer gets his DTUPay account
    Then an error occurs
    And the status is 404
    And the message is "No customer associated with id"

