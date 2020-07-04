@Feature_Mob_Homepage
Feature: Home page


  Background:
    Given I go to the home page

  ## select category from menu and check the category page is displayed
  @homePage1 @regression
  Scenario Outline: Select category via menu dropdown
    When I navigate to <categoryOption> category via menu dropdown
    Then the <categoryOption> category page is displayed
    ##And the Urls are correct
  Examples:
  | categoryOption              |
  | Top Rated Movies            |
  | Release Calendar            |

