Feature: payment
    Scenario: interleaving payment requests
        When a payment is requested
        And a second payment is requested
        And the customer and merchant bank numbers are assigned to the second payment
        Then a bank transaction is initiated for the second payment
        When the customer and merchant bank numbers are assigned to the first payment
        Then a bank transaction is initiated for the first payment
