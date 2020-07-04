package stepdefs;


import config.PropertiesReader;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HomePageStepDefs {

    private HomePage homePage;
    private CategoryPage categoryPage;
    private TitlePage titlePage;
    private PropertiesReader props;


    public HomePageStepDefs(SharedDriver webDriver) {
        homePage = new HomePage(webDriver);
        titlePage = new TitlePage(webDriver);
        categoryPage = new CategoryPage(webDriver);
        //props = new PropertiesReader();
        props = webDriver.getPropertiesReader();
    }

    /****************************************************
     GIVEN
     ***************************************************/

    /****************************************************
     WHEN
     ***************************************************/

    @When("^I navigate to ([^\"]*) category via menu dropdown$")
    public void I_navigate_to_the_category_via_menu_dropdown(String categoryOption) throws Throwable {
        homePage.clickMenuDropDown();
        homePage.selectCategoryByText(categoryOption.toLowerCase());
        // waitForPageToLoad method is in Page Class, I have used titlePage in the following line to
        // show what we are waiting for
        categoryPage.waitForPageToLoad();
    }

    @When("^I search by ([^\"]*)$")
    public void I_search_by_film_name(String filmName) throws Throwable {
        homePage.searchFilmName(filmName);
        titlePage.waitForPageToLoad();
    }

    /****************************************************
     THEN
     ***************************************************/

}




