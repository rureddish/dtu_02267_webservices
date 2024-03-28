Feature: Manager report generation
  Scenario: Manager non empty report
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 50
    And the merchant initiates a payment with the token and an amount 25
    Given a merchant with first name "Merchant2" last name "Lastname" CPR "12345678902" and bank a account with balance 500
    And a customer with first name "George2" last name "Michael" CPR "12345678912" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 75
    And the merchant initiates a payment with the token and an amount 100
    And the manager generates a report
    Then a manager report with atleast 4 payments are returned
