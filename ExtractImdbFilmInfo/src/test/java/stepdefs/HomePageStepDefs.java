package stepdefs;


import config.PropertiesReader;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HomePageStepDefs {

    private HomePage homePage;
    private PropertiesReader props;


    public HomePageStepDefs(SharedDriver webDriver) {
        homePage = new HomePage(webDriver);
        //props = new PropertiesReader();
        props = webDriver.getPropertiesReader();
    }

    /****************************************************
     GIVEN
     ***************************************************/

    /****************************************************
     WHEN
     ***************************************************/

    @When("^I navigate to the Signin page$")
    public void I_navigate_to_the_Signin_page() throws Throwable {
        homePage.navigateToSignInPage();
    }


    @When("^enter valid logon details and submit$")
    public void I_enter_valid_logon_details() throws Throwable {
        homePage.enterLoginDetails();
        homePage.clickLoginButton();
    }

    @When("^I enter the ([^\"]*) into Create an Account$")
    public void I_enter_the_emailAddress_into_Create_an_Account(String emailAddress) throws Throwable {
        homePage.enterEmailAddressAndSubmit(emailAddress);
    }
    @When("^I navigate to Create an Account Details form ([^\"]*)$")
    public void I_navigate_to_Create_an_Account_Details_form(String emailAddress) throws Throwable {
        homePage.navigateToSignInPage();
        homePage.enterEmailAddressAndSubmit(emailAddress);
        homePage.submitCreateAnAccount();
    }

    @When("^I click Register Button$")
    public void I_click_Register_Button() throws Throwable {
        homePage.clickRegister();
    }

    @When("^I enter valid details$")
    public void I_enter_valid_details() throws Throwable {
        homePage.clickRegister();
    }

    /****************************************************
     THEN
     ***************************************************/
    @Then("^error message is displayed$")
    public void Error_message_is_displayed() throws Throwable {
        homePage.checkErrorMessageDisplayed();
    }

    @Then("^I click Create an Account without email address$")
    public void I_click_Create_an_Account_without_email_address() throws Throwable {
        homePage.submitCreateAnAccount();
    }

    @Then("^the Create an Account Details form is displayed$")
    public void the_Create_an_Account_Details_frame_is_displayed() throws Throwable {
        homePage.checkAccountDetailsFormIsDisplayed();
    }

    @Then("^message is displayed showing all Mandatory fields required$")
    public void message_is_displayed_showing_all_Mandatory_fields_required() throws Throwable {
        homePage.checkMandatoryFields();
    }


    @Then("^the correct account will be logged in$")
    public void the_correct_account_will_be_logged_in() throws Throwable {
        homePage.checkAccountLoggedIn();
    }
}




