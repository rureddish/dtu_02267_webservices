Feature: Register merchant
  Scenario: Successful merchant register
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    When the merchant is registered to DTUPay
    Then merchant's account ID is returned
