package pages;

import config.PropertiesReader;
import config.RunCukesTest;
import org.junit.Assert;
import org.openqa.selenium.*;
import stepdefs.SharedDriver;

import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class HomePage extends Page {

    public HomePage(SharedDriver webDriver) {
        super(webDriver);
        driver = webDriver;
    }

    private WebDriver driver;

    // BY Locators

    private static final By SEARCH_INPUT = By.cssSelector("input[name='q']");
    private static final By SEARCH_BUTTON = By.cssSelector("button[id='suggestion-search-button']");
    private static final By MENU_DROPDOWN = By.cssSelector("label[id='imdbHeader-navDrawerOpen--desktop']");
    private static final By MENU_ITEMS = By.cssSelector("a[role='menuitem']");


    //
    //*** NAVIGATE
    //
    public void goTo() {

        webDriver.navigate().to(getProperty("page.home.url"));

        webDriver.manage().window().maximize();
        webDriver.setServer(getServerNumber());
        assertEquals(getUrl(), getProperty("page.home.url"));

    }

    //
    //*** SEND
    //

    //
    //*** CLICK
    //
    public void clickMenuDropDown() {
        click(MENU_DROPDOWN);
    }

    public void searchFilmName(String filmName){
        doubleClickClearAndSendKeys(SEARCH_INPUT, filmName);
        click(SEARCH_BUTTON);
    }



    //
    //*** SELECT
    //

    public void selectCategoryByText(String category) {
        List<WebElement> menuItems = getElementsList(MENU_ITEMS);

        for (WebElement productLine : menuItems){
            if (category.equals(productLine.getText().toLowerCase())){
                productLine.click();
                break;
            }
        }
    }

    //
    //*** TESTS / CHECKS *** IS DISPLAYED / IS ENABLED / IS SELECTED
    //

}

