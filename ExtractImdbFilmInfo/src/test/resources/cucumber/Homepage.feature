@Feature_Mob_Homepage
Feature: Home page


  Background:
    Given I go to the home page

  ##
  @homePage1 @homepageRegression
  Scenario: Attempt to Create an Account with no Email Address
    When I navigate to the Signin page
    When I click Create an Account without email address
    Then error message is displayed

  ##
  @homePage2 @homepageRegression
  Scenario Outline: Navigate to Create an Account Details form
    When I navigate to the Signin page
    And I enter the <emailAddress> into Create an Account
    Then I click Create an Account without email address
    Then the Create an Account Details form is displayed
    ##And the Urls are correct
    Examples:
      | emailAddress                       |
      | aaa.bbb@gmail.com                  |

  @homePage3 @homepageRegression
  Scenario Outline: Validate Mandatory Fields in Create an Account Details form
    When I navigate to Create an Account Details form <emailAddress>
    And I click Register Button
    Then message is displayed showing all Mandatory fields required
    Examples:
      | emailAddress                       |
      | aaa.bbb@gmail.com                  |

  @homePage4 @homepageRegression
  Scenario: Validate login with a Created Account
    When I navigate to the Signin page
    And enter valid logon details and submit
    Then the correct account will be logged in
