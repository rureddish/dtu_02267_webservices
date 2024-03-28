Feature: ID's are unique
    Scenario: Two customers have unique IDs
        Given a customer registers 
        And a customer registers
        Then the IDs are unique
    Scenario: Two merchants have unique IDs
        Given a merchant registers 
        And a merchant registers
        Then the IDs are unique
    Scenario: A merchant and customer have unique IDs
        Given a merchant registers 
        And a customer registers
        Then the IDs are unique
