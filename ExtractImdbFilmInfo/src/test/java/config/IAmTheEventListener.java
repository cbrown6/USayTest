package config;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.events.WebDriverEventListener;
import pages.HomePage;
import pages.Page;

/**
 * Created by Colin on 14/05/2020.
 */
public class IAmTheEventListener implements WebDriverEventListener {

    private static final By BUTTON_RS_INVITE_CLOSE = By.cssSelector("button#fsrFocusFirst");


    public void beforeNavigateTo(String var1, WebDriver var2){

    };

    @Override
    public void afterNavigateTo(String var1, WebDriver var2){
        //System.out.println("hello");

    };

    public void beforeNavigateBack(WebDriver var1){

    };

    public void afterNavigateBack(WebDriver var1){

    };

    public void beforeNavigateForward(WebDriver var1) {

    };

    public void afterNavigateForward(WebDriver var1){

    };

    public void beforeNavigateRefresh(WebDriver var1) {

    };

    public void afterNavigateRefresh(WebDriver var1){

    };


    public void beforeFindBy(By var1, WebElement var2, WebDriver var3){

    };


    public void afterFindBy(By var1, WebElement var2, WebDriver var3){

    };

    public void beforeClickOn(WebElement var1, WebDriver var2){

    };

    public void afterClickOn(WebElement var1, WebDriver var2){

    };

    public void beforeChangeValueOf(WebElement var1, WebDriver var2){

    };

    public void afterChangeValueOf(WebElement var1, WebDriver var2){

    };

    public void beforeScript(String var1, WebDriver var2){

    };

    public void afterScript(String var1, WebDriver var2){

    };

    @Override
    public void onException(Throwable var1, WebDriver var2){
        //System.out.println("hello2");
        try{
            var2.findElement(BUTTON_RS_INVITE_CLOSE).click();

        }catch(NoSuchElementException e){

        }


        if (var1.equals("NoSuchElementException")){
            System.console();
            //System.out.println("got you!");
        }
    };

}
