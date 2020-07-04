@Feature_Title
Feature: Title page

  Background:
    Given I go to the home page

  ## this scenario is used to retrieve the film details
  @titlePage1 @regression
  Scenario Outline: Search by <filmName>
    When I search by <filmName>
    Then the <filmName> title page is displayed
    And the <filmName> details are displayed
    Examples:
      | filmName                    |
      | The Call of the Wild (2020) |
      | The Gentlemen (2019) |