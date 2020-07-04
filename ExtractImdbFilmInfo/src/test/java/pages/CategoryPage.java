package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import stepdefs.SharedDriver;

import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class CategoryPage extends Page {

    public CategoryPage(SharedDriver webDriver) {
        super(webDriver);
        driver = webDriver;
    }

    private WebDriver driver;

    // BY Locators
    private static final By CATEGORY_HEADER = By.cssSelector("h1[class='header']");


    //
    //*** IS DISPLAYED / IS ENABLED / IS SELECTED
    //


    //
    //*** SEND
    //


    //
    //*** CLICK
    //


    //
    //*** SELECT
    //

    //
    //*** TESTS / CHECKS
    //

    public void checkCategoryPageIsDisplayed(String categoryOption) {
        String temp1 = getText(CATEGORY_HEADER);
        String temp2 = categoryOption;
        switch (categoryOption) {
            case "Top Rated Movies":
                assertEquals(getText(CATEGORY_HEADER), categoryOption);
                break;
            case "Release Calendar":
                assertEquals(getText(CATEGORY_HEADER), "Upcoming Releases for United States");
                break;
        }

    }

}

