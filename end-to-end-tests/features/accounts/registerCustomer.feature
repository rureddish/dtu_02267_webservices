Feature: Register customer
  Scenario: Successful customer register
    Given a customer with first name "George" last name "Michael" CPR "1234567890" and bank a account with balance 500
    When the customer is registered to DTUPay
    Then customer's account ID is returned
