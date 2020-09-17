package pages;

import config.PropertiesReader;
import org.openqa.selenium.NoSuchElementException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import stepdefs.SharedDriver;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public abstract class Page {

    public Page(SharedDriver webDriver) {
        Page.webDriver = webDriver;
        WAIT_3 = new WebDriverWait(webDriver, 3);
        WAIT_10 = (WebDriverWait) new WebDriverWait(webDriver, 10).pollingEvery(2, TimeUnit.SECONDS);
        WAIT_20 = (WebDriverWait) new WebDriverWait(webDriver, 20).pollingEvery(2,TimeUnit.SECONDS);
        WAIT_30 = (WebDriverWait) new WebDriverWait(webDriver, 30).pollingEvery(2,TimeUnit.SECONDS);
        WAIT_FLUENT = new FluentWait<WebDriver>(webDriver)
                .withTimeout(30, TimeUnit.SECONDS)
                .pollingEvery(5, TimeUnit.SECONDS)
                .ignoring(ElementNotVisibleException.class)
                .ignoring(NoSuchElementException.class)
                .ignoring(WebDriverException.class);

        propertiesReader = webDriver.getPropertiesReader();
        ACTIONS = new Actions(webDriver);
        JSEXECUTE = webDriver;

    }

    public static SharedDriver webDriver;

    private static final CharSequence URL = null;
    private static final By BUTTON_RS_INVITE_CLOSE = By.cssSelector("button#fsrFocusFirst");

    public static WebDriverWait WAIT_3;
    public static WebDriverWait WAIT_10;
    public static WebDriverWait WAIT_20;
    public static WebDriverWait WAIT_30;
    public static Actions ACTIONS;

    public static JavascriptExecutor JSEXECUTE;
    public static Wait<WebDriver> WAIT_FLUENT;
    public PropertiesReader propertiesReader = null;


    //*********************************************************************************************************
    //*** ACTIONS
    //*********************************************************************************************************
    public void hover(By locator){
        ACTIONS.moveToElement(webDriver.findElement(locator)).build().perform();
    }

    //*********************************************************************************************************
    //*** ALERT METHODS
    //*********************************************************************************************************

    public boolean isAlertPresent() {
        try {
            new WebDriverWait(webDriver, 2).ignoring(NoAlertPresentException.class)
                    .until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException ignored) {
            return false;
        }
    }

    public void acceptAlert(){
        webDriver.switchTo().alert().accept();
    }

    public String getAlertText ()throws NoSuchElementException{

        String value = "";
        try {
            value = webDriver.switchTo().alert().getText();

        } catch (NoAlertPresentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return value ;
    }

    public void switchToAlert() {
        webDriver.switchTo().alert();
    }

    //*********************************************************************************************************
    //*** CHECKS
    //*********************************************************************************************************
    public void checkForRsInvite() {

        try {
            clickWithoutWait(BUTTON_RS_INVITE_CLOSE);
        } catch (NoSuchElementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Loops through a list of Web elements and checks if the http response code of any of the 'href'
     * attributes is valid or not via 'getResponseCode' method in Base Page. It will also fail the test
     * if no links are found at all.
     */
    public void checkValidLinks(List<WebElement> elements)throws IOException, InterruptedException {
        if (elements.isEmpty()) {
            fail("No links where found");
        } else {
            for (int index = 0; index < elements.size(); index++) {
                assertTrue(getResponseCode(elements.get(index).getAttribute("href")));
            }
        }
    }

    //*********************************************************************************************************
    //*** CLEAR METHODS
    //*********************************************************************************************************
    public void clear(By locator) {
        WAIT_10.until(
                ExpectedConditions.presenceOfElementLocated(locator)).clear();
    }

    public void clear(WebElement element) {
        WAIT_10.until(
                ExpectedConditions.elementToBeClickable(element)).clear();
    }

    //*********************************************************************************************************
    //*** CLICK METHODS
    //*********************************************************************************************************
    /**
     * Returns true when element is clickable
     * Returns false when element is not clickable
     * <p>
     *
     * @param locator The By locator for the element
     * @return boolean based on elements clickable state
     */
    public void click(By locator) {
        //waitForPageToLoad();
        scrollIntoView(locator);
        WAIT_FLUENT.until(
                ExpectedConditions.elementToBeClickable(locator)).click();
    }

    public void click(WebElement element) {
        //waitForPageToLoad();
        WAIT_FLUENT.until(
                ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public static void clickFromList(By locator, int increment) {
        List<WebElement> menuOptions = webDriver.findElements(locator);
        WaitAndClickElement(menuOptions.get(increment));
    }

    public boolean elementClickable(By locator) {
        try{
            WAIT_3.until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        }catch(TimeoutException ignored){
            return false;
        }
    }

    public void clickNoWait(By locator){
        webDriver.findElement(locator).click();
    }

    public void clickNoWait(WebElement element){
        element.click();
    }

    public void clickMvtChange(By locator) {
        //waitForPageToLoad();
        try{
            WAIT_FLUENT.until(
                    ExpectedConditions.elementToBeClickable(locator)).click();
        }catch(Exception e){
            scrollToBottomOfPage();
            WAIT_FLUENT.until(
                    ExpectedConditions.elementToBeClickable(locator)).click();
        }
    }

    public void clickOnLinkByPartialText(String text) {
        webDriver.findElement(By.partialLinkText(text)).click();
    }

    public void clickToGoBack() {
        webDriver.navigate().back();
    }

    public void clickWithoutWait(By locator) throws NoSuchElementException {
        try {
            WebElement element = webDriver.findElement(locator);
            element.click();
        } catch (NoSuchElementException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
    }

    public void doubleClick(WebElement element){
        ACTIONS.doubleClick(element).build().perform();
    }

    public void doubleClickClearAndSendKeys(WebElement elem, String keys) {
        ACTIONS.doubleClick(elem);
        clear(elem);
        enterText(elem, keys);
    }

    public void doubleClickClearAndSendKeys(By locator, String keys) {
        doubleClickClearAndSendKeys(webDriver.findElement(locator),keys);
    }

    //*********************************************************************************************************
    //*** DOCUMENT BUILDER FACTORY METHODS
    //*********************************************************************************************************
    // if want to use - see ECommerceTestingWeb.Page
   /*
    public void valueFromXMLMatchesExpected(String tagName, String xmlString, String expected) throws ParserConfigurationException, IOException, SAXException {
        assertEquals(getXMLValue(tagName,xmlString ), expected);
    }
    */

    // if want to use - see ECommerceTestingWeb.Page
   /*
    public String getXMLValue(String tagName, String xmlString)throws ParserConfigurationException, IOException, SAXException{
        if (xmlString.contains(tagName)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = factory.newDocumentBuilder();
            disableDtdValidation(db);
            InputSource is = new InputSource();
            String xmlFormatted = cleanXML(xmlString);
            is.setCharacterStream(new StringReader(xmlFormatted));
            Document doc = null;
            doc = db.parse(is);
            NodeList nodes = doc.getElementsByTagName(tagName);
            Element element = (Element) nodes.item(0);
            return getCharacterDataFromElement(element);
        } else return "";
    }
    */


    //*********************************************************************************************************
    //*** FIND / SELECT ELEMENT METHODS
    //*********************************************************************************************************
    public boolean elementPresent(By locator) {
        setImplicitTimeout(1);
        List<WebElement> s = webDriver.findElements(locator);
        setImplicitTimeout(Long.parseLong(propertiesReader.getProperty("driver.implicit.wait")));
        if (s.size() >0){
            if (webDriver.findElement(locator).isDisplayed()){
                return true;
            }
        }
        return false;
    }

    public List<WebElement> getElementsList(By locator) {
        return webDriver.findElements(locator);
    }

    public WebElement getElementFromListByIndex(By locator, int index) {
        List<WebElement> elements = webDriver.findElements(locator);
        return elements.get(index);
    }

    //*** Select Elements / DropDowns

    public List<WebElement> getAllDropDownOptions(WebElement element) {
        Select dropDown = new Select(element);
        return dropDown.getOptions();
    }

    public List<WebElement> getAllDropDownOptions(By locator) {
        Select dropDown = new Select(webDriver.findElement(locator));
        return dropDown.getOptions();
    }

    public void selectFromListByIndex(By locator, int index) {
        List<WebElement> menuOptions = webDriver.findElements(locator);
        click(menuOptions.get(index));
    }

    public void selectDropdownByIndex(By locator, int index) {
        WAIT_20.until(
                ExpectedConditions.elementToBeClickable(locator));
        Select dropdown = new Select(webDriver.findElement(locator));
        dropdown.selectByIndex(index);
    }

    public void selectDropdownByValue(By locator, String value) {
        Select dropDown = new Select(webDriver.findElement(locator));
        dropDown.selectByValue(value);
    }

    public void selectDropdownByValue(WebElement element, String value) {
        Select dropDown = new Select(element);
        dropDown.selectByValue(value);
    }

    public String selectDropdownByText(By locator, String optionText) {
        WebElement element = webDriver.findElement(locator);
        selectDropdownByText(element, optionText);
        return element.getText();
    }

    public void selectDropdownByText(WebElement element, String optionText) {
        Select dropDown = new Select((WAIT_10).until(ExpectedConditions.elementToBeClickable(element)));
        dropDown.selectByVisibleText(optionText);
    }

    public void selectDropdownByText(Select select, String optionText) {
        select.deselectByVisibleText(optionText);
    }

    public void selectDropdownByRandomIndex(Select select) {
        Random random = new Random();
        int selectSize = select.getOptions().size();
        int selectIndex = random.nextInt(selectSize);
        select.selectByIndex(selectIndex);
    }

    public void selectDropdownByRandomIndex(By locator) {
        Select select = new Select((WAIT_10).until(ExpectedConditions.visibilityOfElementLocated(locator)));
        selectDropdownByRandomIndex(select);
    }

    public void selectDropdownByRandomIndex(WebElement element) {
        Select select = new Select((WAIT_10).until(ExpectedConditions.elementToBeClickable(element)));
        selectDropdownByRandomIndex(select);
    }

    //*********************************************************************************************************
    //*** GET METHODS
    //*********************************************************************************************************

    //*** getText
    public String getText(By locator) {
        return WAIT_3.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
        //String value = webDriver.findElement(locator).getText();
        //return value;
    }

    public boolean textFromElementContains(By locator, String substr){
        return getTextFromElement(locator).contains(substr);
    }

    public String getTextFromElement(WebElement element){
        return WAIT_3.until(ExpectedConditions.visibilityOf(element)).getText().trim();
    }

    public String getTextFromElement(By locator){
        return WAIT_3.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim();
    }

    //*** getAttribute
    public String getAttributeFromElement(WebElement element, String attribute){
        return WAIT_3.until(ExpectedConditions.visibilityOf(element)).getAttribute(attribute);
    }

    public String getAttributeFromElement(By locator, String attribute){
        return WAIT_3.until(ExpectedConditions.visibilityOfElementLocated(locator)).getAttribute(attribute);
    }

    public ArrayList<String> getAttributeListFromElementList(By locator, String attribute){
        ArrayList<String> elemList = new ArrayList<>();
        webDriver.findElements(locator)
                .forEach(element -> elemList.add(element.getAttribute(attribute)));
        return elemList;
    }

    public List<WebElement> getAllWebElements(By locator){
        return webDriver.findElements(locator);
    }

    public int getElementsListSize(By locator){
        return (webDriver.findElements(locator)).size();
    }

    public String getUrl() {
        return webDriver.getCurrentUrl();
    }

    // This gets the option that is currently displayed
    public String getTextFromFirstOption(WebElement element){
        return new Select(element).getFirstSelectedOption().getText().trim();
    }

    // This gets the option that is currently displayed
    public String getTextFromFirstOption(By locator){
        return new Select(webDriver.findElement(locator)).getFirstSelectedOption().getText().trim();
    }

    public boolean textFromElementContainsIgnoreCase(By locator, String string) {

        return getLowercaseTextFromElement(locator).contains(string.toLowerCase());
    }

    public String getLowercaseTextFromElement(By locator){

        return WAIT_3.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText().trim().toLowerCase();
    }

    private static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
            CharacterData cd = (CharacterData) child;
            return cd.getData();
        }
        return "";
    }

    public String getPageSource(){
        return webDriver.getPageSource();
    }

    public void isInPageSource(CharSequence searchString) {
        assertTrue(webDriver.getPageSource().contains(searchString));
    }

    public void urlContainsString(String expected){
        assertTrue(webDriver.getCurrentUrl().contains(expected.toLowerCase()));
    }

    public boolean containsIgnoreCase(String stringToSearch, String searchString){
        return  StringUtils.containsIgnoreCase(stringToSearch,searchString);
    }

    public String getServerNumber() {
        String serverNo = webDriver.getPageSource();
        Pattern pattern = Pattern.compile("<!--\\sBuild\\sDate\\s*:\\s\\d*-\\d*.(\\d*)");
        Matcher matcher = pattern.matcher(serverNo);

        if (matcher.find()) {
            System.out.println("Server number : " + matcher.group(1));
            return matcher.group(1);
        } else {
            return "unknown";
        }

    }
    public String getPropertyFromPageData(String tagName) {
        String tagNameProperty, scriptData;
        scriptData = webDriver.findElement(By.xpath("//script[contains((),'pageData')]")).getAttribute("innerHTML");
        Pattern pattern = Pattern.compile(tagName + "\":\"*(.*?)\"");
        Matcher matcher = pattern.matcher(scriptData);
        if (matcher.find()) {
            tagNameProperty = matcher.group(1);
        } else {
            tagNameProperty = "Script Tag Was Not Found";
        }
        return tagNameProperty;
    }

    public String getPropertyFromPageDataLayer(String tagName, String page) {
        String tagNameProperty, scriptData;
        if (page.equals("terminal node") || page.equals("category")){
            scriptData = webDriver.findElement(By.xpath("//script[contains(text(),'pageData')]")).getAttribute("innerHTML"); //For NEW Terminal-Node page
        }else{
            scriptData = webDriver.findElement(By.xpath("//script[contains(text(),'var rs')]")).getAttribute("innerHTML"); //ORIGINAL - For OTHER Pages
        }
        Pattern pattern = Pattern.compile(tagName + "\":\"*(.*?)\"");
        Matcher matcher = pattern.matcher(scriptData);
        if (matcher.find()) {
            tagNameProperty = matcher.group(1);
        } else {
            tagNameProperty = "Script Tag Was Not Found";
        }
        return tagNameProperty;
    }

    /**
     * Takes a url string as a parameter and uses Http connection to validate
     * whether the url is valid by checking whether it has a 404 response code
     * or not. It then returns a boolean value whether this condition is true
     * or false. It will also print out the response code as well as the urlString
     * it is checking for debugging.
     */
    public static boolean getResponseCode(String urlString) {
        boolean isValid = false;
        try {
            java.net.URL u = new java.net.URL(urlString);
            HttpURLConnection h = (HttpURLConnection) u.openConnection();
            h.setRequestMethod("GET");
            h.connect();
            System.out.println(h.getResponseCode() + " - " + urlString);
            if (h.getResponseCode() != 404) {
                isValid = true;
            }
        } catch (Exception e) {

        }
        return isValid;
    }

    //*********************************************************************************************************
    //*** IS APHANUMERIC METHODS
    //*********************************************************************************************************
    public boolean isAlphaNumeric(List<WebElement> aList) {
        List<String> list1 = new ArrayList<String>();
        List<String> list2 = new ArrayList<String>();
        for (int i = 0; i < aList.size(); i++) {
            list1.add(aList.get(i).getText());
            list2.add(aList.get(i).getText());
        }
        //Needs to ignore case - if not then sorts on uppercase precedence
        Collections.sort(list2, String.CASE_INSENSITIVE_ORDER);
        return list1.equals(list2);
    }


    //*********************************************************************************************************
    //*** IS DISPLAYED / IS ENABLED / IS SELECTED METHODS
    //*********************************************************************************************************
    public boolean isDisplayed() {
        return webDriver.getCurrentUrl().contains(URL);
    }

    public boolean elementDisplayed(By locator)
    { return webDriver.findElement(locator).isDisplayed(); }

    public boolean elementDisplayed(WebElement element){
        return WAIT_3.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
    }

    public boolean elementEnabled(By locator){
        try{
            WAIT_FLUENT.until((webDriver1) -> webDriver1.findElement(locator).isEnabled());
            return true;
        }catch(TimeoutException ignored){
            return false;
        }
    }

    public boolean elementSelected(By locator){
        try{
            WAIT_3.until(ExpectedConditions.elementToBeSelected(locator));
            return true;
        }catch(TimeoutException ignored){
            return false;
        }
    }

    public boolean elementIsSelected(final By locator){

        return webDriver.findElement(locator).isSelected();
    }

    //*********************************************************************************************************
    //*** JAVA CODE METHODS
    //*********************************************************************************************************
    public void listContainsStringsThatContainsStringFromList(List<String> originalLis, List<String> searchStrList){
        searchStrList.forEach(searchedTerm -> {
            long i = originalLis.stream().filter(link -> link.contains(searchedTerm)).count();
            assertTrue(i > 0);
        });
    }

    public String replaceWithFigures(By locator) {
        if (getText(locator).replaceAll("[^0-9]", "").isEmpty()) {
            return getText(locator).replaceAll("[^0-9]", "0");
        } else

            return getText(locator).replaceAll("[^0-9]", "");
    }

    public int value(By locator) {
        return (Integer.parseInt(replaceWithFigures(locator)));
    }

    public void puts(String string) {
        System.out.println(string);
    }


    //*********************************************************************************************************
    //*** JSEXECUTE METHODS
    //*********************************************************************************************************
    public boolean resultsHeaderIsDisplayed() {
        boolean headerVisible;
        headerVisible = (boolean) JSEXECUTE.executeScript("var elemTop = el.getBoundingClientRect().top;\n" +
                "    var elemBottom = el.getBoundingClientRect().bottom;\n" +
                "\n" +
                "    var isVisible = (elemTop >= 0) && (elemBottom <= window.innerHeight);\n" +
                "    return isVisible;");
        return headerVisible;
    }

    public boolean resultsHeaderHorizontalScroll() {
        boolean headerScrollable;
        headerScrollable = (boolean) JSEXECUTE.executeScript("var elemTop = el.getBoundingClientRect().top;\n" +
                "    var elemBottom = el.getBoundingClientRect().bottom;\n" +
                "\n" +
                "    var isVisible = (elemTop >= 0) && (elemBottom <= window.innerHeight);\n" +
                "    return isVisible;");
        return headerScrollable;
    }

    public void scrollTo(int width, int height) {
        ((JavascriptExecutor) webDriver).executeScript("scroll(" + width + "," + height + ")");
    }

    public void scrollToBottomOfPage() {
        Dimension windowSize = webDriver.manage().window().getSize();
        scrollTo(0, windowSize.getHeight());
    }

    public void scrollIntoView(WebElement element) {
        JavascriptExecutor je = (JavascriptExecutor) webDriver;
        je.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollIntoView(By locator) {
        JavascriptExecutor je = (JavascriptExecutor) webDriver;
        je.executeScript("arguments[0].scrollIntoView(true);", webDriver.findElement(locator));
    }

    //*********************************************************************************************************
    //*** PROPERTIES METHODS
    //*********************************************************************************************************
    public String getProperty(String property){
        return propertiesReader.getProperty(property);
    }

    public List<String> getPropertyAsList(String property) {
        String country = System.getProperty("country");
        String data = new PropertiesReader(country).getProperty(property);
        return Arrays.asList(data.split(","));
    }

    //*********************************************************************************************************
    //*** SEND METHODS
    //*********************************************************************************************************
    public void enterText(WebElement element, String text) {
        element.sendKeys(text);
    }

    public void enterText(By locator, String text) {
        new WebDriverWait(webDriver, 20).until(
                ExpectedConditions.elementToBeClickable(locator));
        clear(locator);
        webDriver.findElement(locator).sendKeys(text);
    }

    public void clearAndEnterText(By locator, String text){
        WebElement e = WAIT_3.until(ExpectedConditions.elementToBeClickable(locator));
        e.clear();
        e.sendKeys(text);
    }

    public void clearAndEnterText(WebElement element, String text){
        element.clear();
        element.sendKeys(text);
    }

    public void populateField(By locator, String data) {
        webDriver.findElement(locator).clear();
        webDriver.findElement(locator).sendKeys(data);
    }

     //* To be used when entering text into input elements underneath a button element
     //* Or if the text box is not 'clickable'
     //* (Example being quote upload, text is sent to the input element within the span button)
    public void inputSendKeys(By locator, String keys){
        webDriver.findElement(locator).sendKeys(keys);
    }

    //*********************************************************************************************************
    //*** SHARED DRIVER (get data) METHODS
    //*********************************************************************************************************
    public String getSharedData(String key) {
        return webDriver.getSharedData(key);
    }

    /* needs fixing...
    public void saveSharedData(String key, Object value) {
        webDriver.setSharedData(key, value);
    }
    */

    //*********************************************************************************************************
    //*** SWITCH TO METHODS
    //*********************************************************************************************************
    public void switchToActiveElement() {
        webDriver.switchTo().activeElement();
    }

    public void switchToTabByIndex(int tab){
        sleep(3);
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tab));
    }

    public void switchToDefaultContent() {
        webDriver.switchTo().defaultContent();
    }

    //*********************************************************************************************************
    //*** TABS METHODS
    //*********************************************************************************************************
    public void closeTabByIndex(int tab){
        ArrayList<String> tabs = new ArrayList<String>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tab)).close();
    }

    //*********************************************************************************************************
    //*** WAIT METHODS
    //*********************************************************************************************************

    public void setImplicitTimeout(long implicitTimeout) {
        webDriver.manage().timeouts().implicitlyWait(implicitTimeout, TimeUnit.SECONDS);
    }

    public WebElement waitforElementClickable(By locator){
        return WAIT_10.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitforElementClickable(WebElement element){
        return WAIT_10.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForPageToLoad() {
        String pageLoadStatus;
        do {

            //get the current document ready state - i.e. is the page loaded
            pageLoadStatus = (String) JSEXECUTE.executeScript("return document.readyState");

            //Once the page loaded status is complete
        } while (!pageLoadStatus.equals("complete")&&!pageLoadStatus.equals("interactive"));
    }

    /*
    public void waitForPageToLoad(int maxSeconds, String page) {
        String url = getProperty(page);
        Long startTime = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - startTime < maxSeconds * 1000 && !webDriver.getCurrentUrl().contains(url)) {
            }
        } catch (StaleElementReferenceException e) {
            return;
        }
    }
    */

    public void waitForElementToDisappear(By locator, int maxSeconds) {
        Long startTime = System.currentTimeMillis();
        try {
            while (System.currentTimeMillis() - startTime < maxSeconds * 1000 && webDriver.findElement(locator).isDisplayed()) {
            }
        } catch (StaleElementReferenceException e) {
            return;
        }
    }

    public boolean waitForElementToDisappear(WebElement element, int maxSeconds) {
        int timer = 0;

        boolean present = element.isDisplayed();
        while(present && timer <=maxSeconds){
            try {
                present = element.isDisplayed();
                sleep(1);
                timer++;
            }catch (Exception e) {
                return true;
            }
        }
        return timer < maxSeconds;
    }

    public WebElement waitForElementVisible(WebElement element){
        return WAIT_10.until(ExpectedConditions.visibilityOf(element));
    }

    public WebElement waitForElementVisible(By locator){
        return WAIT_20.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitUntilElementIsVisible(By locator) {

        WAIT_FLUENT.until(visibilityOfElementLocated(locator));
    }

    public static void WaitAndClickElement(WebElement element) {
        new WebDriverWait(webDriver, 20).until(ExpectedConditions.elementToBeClickable(element));
        element.click();
    }

    public void sleep(int i) {
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
