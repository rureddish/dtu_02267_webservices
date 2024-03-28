Feature: Customer report generation
  Scenario: Customer non empty report
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 50
    And the merchant initiates a payment with the token and an amount 25
    And the customer generates a report
    Then a customer report is returned with the following amounts 
        | 50 |
        | 25 |

  Scenario: empty report for non existing customer
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 50
    And the merchant initiates a payment with the token and an amount 25
    And the customer generates a report
    Then a customer report is returned with the following amounts 
        | 50 |
        | 25 |
    Given a customer with account id "nosuchcustomer"
    When the customer generates a report
    Then an empty customer report is returned

  Scenario: Customer non empty report with another customer
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 50
    And the merchant initiates a payment with the token and an amount 25
    Given a customer with first name "George2" last name "Michael" CPR "12345678912" and bank a account with balance 500
    And the customer is registered to DTUPay
    And the customer generates a report
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 75
    And the merchant initiates a payment with the token and an amount 100
    And the customer generates a report
    Then a customer report is returned with the following amounts 
        | 100 |
        | 75 |


