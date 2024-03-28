Feature: Get tokens
  Scenario: Successful token retrieval
    Given a customer with name "customer" and 4 tokens
    Then customer has 4 tokens

  Scenario: Successful token request after already having some tokens
    Given a customer with name "customer" and 1 tokens
    And customer requests 2 tokens
    Then customer has 3 tokens

#  Scenario: Unregistered customer unable to request tokens
#    Given an unregistered customer with name "customer1"
#    And customer requests 1 tokens
#    Then exception of type "UnknownCustomerException" is thrown
