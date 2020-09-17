package pages;

import config.PropertiesReader;
import config.RunCukesTest;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.*;
import stepdefs.SharedDriver;


import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class HomePage extends Page {

    public HomePage(SharedDriver webDriver) {
        super(webDriver);
        driver = webDriver;
        props = new PropertiesReader();

    }

    private WebDriver driver;
    private PropertiesReader props;


    // BY Locators

    private static final By SIGNIN = By.cssSelector("a[class=login]");
    private static final By CREATE_ACCOUNT = By.cssSelector("button[id='SubmitCreate']");
    private static final By ERROR_MESSAGE = By.cssSelector("div[id='create_account_error']");
    private static final By INPUT_EMAIL_CREATE = By.cssSelector("input[id='email_create']");
    private static final By CREATE_A_FORM = By.cssSelector("form[id='account-creation_form']");
    private static final By MANDATORY_FIELD_MESSAGES = By.cssSelector("div[class='alert alert-danger']>ol>li");
    private static final By REGISTER = By.cssSelector("button[id='submitAccount']");
    private static final By EMAIL = By.cssSelector("input[id='email']");
    private static final By PASSWD = By.cssSelector("input[id='passwd']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[id=SubmitLogin]");
    private static final By MY_PERSONAL_INFORMATION = By.cssSelector("a[title=Information]");
    private static final By MY_PERSONAL_INFORMATION_EMAIL = By.cssSelector("input[id=email]");










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

    public void navigateToSignInPage(){
        click(SIGNIN);
    }

    public void submitCreateAnAccount(){
        click(CREATE_ACCOUNT);
    }

    public void enterEmailAddressAndSubmit(String emailAddress){
        inputSendKeys(INPUT_EMAIL_CREATE, emailAddress);
    }

    public void clickRegister(){
        click(REGISTER);
    }

    public void enterLoginDetails(){
        enterText(EMAIL, "fff.fff@gmail.com");
        enterText(PASSWD, "admin");
    }

    public void clickLoginButton(){
        click(LOGIN_BUTTON);
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

    public void checkErrorMessageDisplayed(){
        assertTrue(elementDisplayed(ERROR_MESSAGE));
    }

    public void checkAccountDetailsFormIsDisplayed(){
        assertTrue(elementDisplayed(CREATE_A_FORM));
    }

    public void checkMandatoryFields(){
        List<WebElement> elements = getElementsList(MANDATORY_FIELD_MESSAGES);
        List<String> propMessages = new ArrayList<>();

        for (int i = 1; i < 9; i++) {
            String propMessage= "usay.mess" + i;
            propMessages.add(props.getProperty(propMessage));
        }

        int i=0;
        for (WebElement element: elements){
            assertEquals(element.getText(),propMessages.get(i));
            i++;
        }
    }

    public void checkAccountLoggedIn(){
        click(MY_PERSONAL_INFORMATION);
        String temp = getAttributeFromElement(MY_PERSONAL_INFORMATION_EMAIL, "value");
        assertEquals("fff.fff@gmail.com", getAttributeFromElement(MY_PERSONAL_INFORMATION_EMAIL, "value"));
        System.out.println();
    }

}
