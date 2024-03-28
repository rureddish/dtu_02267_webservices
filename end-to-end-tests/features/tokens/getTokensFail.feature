Feature: Get tokens fail
  Scenario: Customer does not have 0 or 1 tokens and total count would be less than 6
    Given a customer with name "customer" and 3 tokens
    And customer requests 2 tokens
    Then an error occurs
    And the status is 403
    And the message is "Customer already has more than one token"
    Then customer has 3 tokens

  Scenario: Customer can request tokens but requests too many
    Given a customer with name "customer" and 1 tokens
    And customer requests 6 tokens
    Then customer has 1 tokens

  Scenario: Customer can request tokens but requests too many
    Given a customer with name "customer6" and 0 tokens
    And customer requests 6 tokens
    Then customer has 6 tokens

  # TODO find out why 0 doesn't work
#  Scenario: Failed token request when requesting too many
#    Given a customer with name "customer" and 0 tokens
#    And customer requests 7 tokens
#    Then customer has 0 tokens

