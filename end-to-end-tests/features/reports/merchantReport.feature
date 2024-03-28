Feature: Merchant report generation
  Scenario: Merchant non empty report
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 50
    And the merchant initiates a payment with the token and an amount 25
    And the merchant generates a report
    Then a merchant report is returned with the following amounts 
        | 50 |
        | 25 |

  Scenario: Empty report for non existing Merchant
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 50
    And the merchant initiates a payment with the token and an amount 25
    And the merchant generates a report
    Then a merchant report is returned with the following amounts 
        | 50 |
        | 25 |
    Given a merchant with account id "nosuchmerchant"
    When the merchant generates a report
    Then an empty merchant report is returned

