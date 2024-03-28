#Feature: deleting a merchant
#  Scenario: Delete merchant successfully
#    Given merchant with first name "George" last name "Michael" CPR "1234567890" and bank a account with balance 500
#    And the merchant is registered
#    When the merchant gets his account
#    Then no error occur
#    Then merchant is deleted
#
#  Scenario: Delete merchant unsuccessfully
#    Given merchant with first name "George" last name "Michael" CPR "1234567890" and bank a account with balance 500
#    And the merchant is registered
#    Given merchant with account id "nosuchmerchant"
#    When the merchant gets his account
#    Then an error occur
#    And status is 404

