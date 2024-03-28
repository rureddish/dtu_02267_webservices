Feature: On payment request event
    Scenario: On payment request event successful  
        Given a customer has a token
        When a payment request event with the same token is published
        Then a token validated for customer event is published for the customer

