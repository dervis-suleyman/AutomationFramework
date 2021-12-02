Feature: Purchase SwagLabs merchandise
  In order to be considered Swag
  I need to be able to purchase my chosen Sauce labs items.

  Background:
    Given I am on the inventory page as the user "standard_user" with the password "secret_sauce"

  Scenario: Checkout and purchase my selected SwagLabs products
    When I select the following products:
      | Product               | cost  |
      | Sauce Labs Backpack   | 29.99 |
      | Sauce Labs Bike Light | 9.99  |
    And I open the basket
    And click on the checkout button
    And enter my information:
      | FirstName | LastName | postcode |
      | Swag      | Labs     | RGF123   |
    And click the continue button
    Then I should see my chosen products in the overview page
      | Product               | cost  |
      | Sauce Labs Backpack   | 29.99 |
      | Sauce Labs Bike Light | 9.99  |
    And complete checkout by clicking the finish button
