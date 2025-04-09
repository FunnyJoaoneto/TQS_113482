Feature: Make a Reservation at Guimarães Tavern

  Scenario: User successfully makes a reservation
    Given I open the restaurant page
    When I click on "Guimarães Tavern"
    Then I should see the "Make a Reservation" page
    And the restaurant name should be "Guimarães Tavern"
    When I select a meal and specify the quantity
    And I choose a time slot
    And I set the number of people to 3
    And I click "Reserve"
    Then I should see the reservation confirmation
    When I click go back to the restaurant page
    Then I should see the restaurant page
    When I search for the reservation using the code
    Then I should see the reservation details
    When I check-in the reservation
