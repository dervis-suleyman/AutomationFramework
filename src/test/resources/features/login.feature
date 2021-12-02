Feature: Login to SwagLabs
  In order to be considered Swag
  I need to be able to login to the SwagLabs website
  and get to the inventory page

  Scenario: Navigate and Login to the SwagLabs website
    Given I visit the SwagLabs website
    When I enter my user name "standard_user" into the username field
    And I enter my password "secret_sauce" into the password field
    And I click the login button
    Then I should successfully Login and see the inventory page