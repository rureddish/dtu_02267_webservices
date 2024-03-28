Feature: get tokens
    Scenario: Tokens are unique
        Given there is a customer
        When the customer is creating 4 tokens
        Then the tokens are unique
