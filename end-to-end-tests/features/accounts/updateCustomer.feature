Feature: Update customer
  Scenario: Customer updated succesfully
    Given a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the customer is registered to DTUPay
    When the customer updates his name to "Geo"
    And the customer gets his DTUPay account
    Then the customers first name is "Geo"

