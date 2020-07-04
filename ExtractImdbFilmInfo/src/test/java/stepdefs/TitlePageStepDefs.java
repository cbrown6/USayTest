package stepdefs;

import config.PropertiesReader;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.HomePage;
import pages.TitlePage;

import static org.junit.Assert.assertTrue;

/**
 * Created by Colin on 27/05/2020.
 */
public class TitlePageStepDefs {

    private HomePage homePage;
    private TitlePage titlePage;
    private PropertiesReader props;



    public TitlePageStepDefs(SharedDriver webDriver) {
        homePage = new HomePage(webDriver);
        titlePage = new TitlePage(webDriver);
        props = new PropertiesReader();
    }


/*
    When I search by <filmName>
    Then the <filmName> title page is displayed
    And the <filmName> details are displayed
*/


    /****************************************************
     GIVEN
     ***************************************************/

    /****************************************************
     WHEN
     ***************************************************/

    /****************************************************
     THEN
     ***************************************************/
    @Then("^the ([^\"]*) title page is displayed$")
    public void the_filNameAndYear_title_page_is_displayed(String filmNameAndYear) throws Throwable {
        titlePage.checkFilmNameByYearIsDisplayed(filmNameAndYear);
    }

    @Then("^the ([^\"]*) details are displayed$")
    public void the_filNameAndYear_details_are_displayed(String filmNameAndYear) throws Throwable {
        titlePage.checkFilmNameByYearDetailsAreDisplayed(filmNameAndYear);
    }
}





