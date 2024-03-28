Feature: Payment
  Scenario: payment successful
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 1 unused token
    When the merchant initiates a payment with the token and an amount 50
    Then the merchant's balance is 550
    And the customer's balance is 450

  Scenario: two payments successful
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 2 unused token
    When the merchant initiates a payment with the token and an amount 50
    Then the merchant's balance is 550
    And the customer's balance is 450
    When the merchant initiates a payment with the token and an amount 50
    Then the merchant's balance is 600
    And the customer's balance is 400

  Scenario: payment failed due to insufficient debtor funds
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "00000" and bank a account with balance 0
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 1 unused token
    When the merchant initiates a payment with the token and an amount 50
    Then an error occurs
    And the status is 400
    And the message is "Bank communication fail: Debtor balance will be negative"

  Scenario: payment failed due to non-existent debtor
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "00000" and non-existent bank account
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 1 unused token
    When the merchant initiates a payment with the token and an amount 50
    Then an error occurs
    And the status is 400
    And the message is "Bank communication fail: Debtor account does not exist"

  Scenario: payment failed due to non-existent creditor
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and non-existent bank account
    And a customer with first name "George" last name "Michael" CPR "00000" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 1 unused token
    When the merchant initiates a payment with the token and an amount 50
    Then an error occurs
    And the status is 400
    And the message is "Bank communication fail: Creditor account does not exist"

  Scenario: payment failed due to invalid token
    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
    And a customer with first name "George" last name "Michael" CPR "00000" and bank a account with balance 500
    And the merchant is registered to DTUPay
    And the customer is registered to DTUPay
    And the customer has 1 unused invalid token
    When the merchant initiates a payment with invalid token and an amount 50
    Then an error occurs
    And the status is 401
    And the message is "Token invalidToken not associated with a customer in DTU Pay"

#  Scenario: payment failed due to insufficient tokens
#    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
#    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
#    And the merchant is registered to DTUPay
#    And the customer is registered to DTUPay
#    And the customer has 0 unused token
#    When the merchant initiates a payment with the token and an amount 50
#    Then an error occurs
#    And the status is 403
#    And the message is "Token invalidToken not associated with a customer in DTU Pay"

#  Scenario: payment with negative amount
#    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
#    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
#    And the merchant is registered to DTUPay
#    And the customer is registered to DTUPay
#    And the customer has 1 unused token
#    When the merchant initiates a payment with the token and an amount -1
#    Then exception of type "InvalidAmountException" is thrown
#
#  Scenario: payment with invalid token
#    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
#    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
#    And the merchant is registered to DTUPay
#    And the customer is registered to DTUPay
#    And the customer has 1 unused invalid token
#    When the merchant initiates a payment with invalid token and an amount 50
#    Then exception of type "InvalidTokenException" is thrown
#
#  Scenario: payment failed due to token already used
#    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
#    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
#    And the merchant is registered to DTUPay
#    And the customer is registered to DTUPay
#    And the customer has 1 unused token
#    When the merchant initiates a payment with the previously used token
#    Then exception of type "TokenAlreadyUsedException" is thrown
#
#  Scenario: two payments with one token left: first one succeeds, second fails
#    Given a merchant with first name "Merchant" last name "Lastname" CPR "1234567890" and bank a account with balance 500
#    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
#    And the merchant is registered to DTUPay
#    And the customer is registered to DTUPay
#    And the customer has 1 unused token
#    When the merchant initiates a payment with the token and an amount 50
#    Then the merchant's balance is 550
#    And the customer's balance is 450
#    When the merchant initiates a payment with the token and an amount 50
#    Then exception of type "InsufficientTokensException" is thrown
#
#  Scenario: payment failed due to invalid account ID
#    Given a merchant with invalid account id "88888"
#    And a customer with first name "George" last name "Michael" CPR "1234567891" and bank a account with balance 500
#    And the customer is registered to DTUPay
#    And the customer has 1 unused token
#    When the merchant initiates a payment with the token and an amount 50
#    Then exception of type "InvalidAccountIdException" is thrown
#
#
