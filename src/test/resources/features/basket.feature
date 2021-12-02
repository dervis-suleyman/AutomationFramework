Feature: Sort, select and add Sauce labs products to the Basket
  In order to be considered Swag
  I need to be able to sort select
  and add Sauce labs products to my basket

  Background:
    Given I am on the inventory page as the user "standard_user" with the password "secret_sauce"

  Scenario: Sort the products by Price (high to low)
    When I sort items by high to low
    Then The products should be reorganised by high to low

  Scenario: Add the cheapest & costliest products to your basket
    When I select the following products:
      | Product                  | cost  |
      | Sauce Labs Fleece Jacket | 49.99 |
      | Sauce Labs Onesie        | 7.99  |
    And I open the basket
    Then I should see my chosen products in the basket
      | Product                  | cost  |
      | Sauce Labs Fleece Jacket | 49.99 |
      | Sauce Labs Onesie        | 7.99  |