Feature: Search functionality on the online library

  Scenario: Search for a book by title
    Given I am on the library homepage
    When I enter "Harry Potter" in the search bar
    And I click the search button
    Then I should see a list of 1 book
    And the book should have the title "Harry Potter and the Sorcerer's Stone"

  Scenario: Search for a book by author
    Given I am on the library homepage
    When I enter "Rick Riordan" in the search bar
    And I click the search button
    Then I should see a list of 2 books
    And the book should have the title "The Tower of Nero"
    And the book should have the title "The Lightning Thief"

  Scenario: Search for a book with no results
    Given I am on the library homepage
    When I enter "Unknown Book Title" in the search bar
    And I click the search button
    Then I should see a list of 0 books

  Scenario: Filter books by category
    Given I am on the library homepage
    When I click on "Fiction" in the category filter
    Then I should see the "Fiction" header
