package stepdefs;

import config.PropertiesReader;
import cucumber.api.java.en.Given;
import pages.*;

import static java.lang.Thread.sleep;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertTrue;

/**
 * Created by C0952235 on 20/04/2017.
 */
public class WebGoTo {
    private HomePage homePage;

    private PropertiesReader props;

    public WebGoTo(SharedDriver webDriver) {
        homePage = new HomePage(webDriver);
        props = webDriver.getPropertiesReader();

    }

    @Given("I go to the ([^\"]*) page")
    public void i_go_to_the_page(String page) throws InterruptedException {

        switch (page.toUpperCase()){
            case "HOME":
                homePage.goTo();
                break;
            default: fail("That page does not have a goTo step def");
        }
    }

    private void i_go_to_the_category_page() throws InterruptedException {
        /*
        navBar.search(navBar.PROPS_READER.getProperty("default.search.term"));
        searchResultsPage.clickCategoryByBinCount(200);
        //searchResultsPage.clickPopularCategoryByIndex(1);
        CategoryPage.waitForPageToLoad();
        sleep(5);
        */
    }

    private void i_go_to_the_product_page() {
        /*
        navBar.search(navBar.PROPS_READER.getProperty("product.default"));
        */
    }

}
