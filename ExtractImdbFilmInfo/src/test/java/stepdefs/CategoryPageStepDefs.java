package stepdefs;


import config.PropertiesReader;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pages.CategoryPage;
import pages.HomePage;
import pages.TitlePage;


public class CategoryPageStepDefs {

    private CategoryPage categoryPage;
    private PropertiesReader props;


    public CategoryPageStepDefs(SharedDriver webDriver) {
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

    /****************************************************
     THEN
     ***************************************************/
    @Then("^the ([^\"]*) category page is displayed$")
    public void the_category_page_is_displayed(String categoryOption) throws Throwable {
        categoryPage.checkCategoryPageIsDisplayed(categoryOption);
    }

}




