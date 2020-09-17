package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DriverFactory {

    private PropertiesReader properties;

    public static DesiredCapabilities caps = new DesiredCapabilities();
    public static RemoteWebDriver driver;

    public DriverFactory() {
        properties = new PropertiesReader();
    }

    public WebDriver getDriver() {

        if(RunCukesTest.useBrowserStack || Boolean.parseBoolean(System.getProperty("runFromCMD"))) {

            String USERNAME = "anthonysoares1";
            String AUTOMATE_KEY = "Twvhpt3wzSFpBsujeQDi";
            String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";



            String mobileDevice;
            String mobileBrowser;
            //mobile device and browser found in general properties

            if(RunCukesTest.useAndroid && !RunCukesTest.useIOS) {
                mobileDevice = properties.getProperty("browserstack.mobileDevice.android");
                mobileBrowser = properties.getProperty("browserstack.mobileBrowser.android");
                caps.setCapability("browserName", mobileBrowser);
                caps.setCapability("device", mobileDevice);
                caps.setCapability("orientation", "Portrait");
            } else if (!RunCukesTest.useAndroid && RunCukesTest.useIOS) {
                mobileDevice = properties.getProperty("browserstack.mobileDevice.iPhone");
                mobileBrowser = properties.getProperty("browserstack.mobileBrowser.iPhone");
                caps.setCapability("browserName", mobileBrowser);
                caps.setCapability("device", mobileDevice);
                caps.setCapability("orientation", "Portrait");
            } else {
                caps.setCapability("browser", RunCukesTest.browserStackBrowser);
                caps.setCapability("browser_version", RunCukesTest.browserStackBrowserVersion);
                caps.setCapability("os", RunCukesTest.browserStackOS);
                caps.setCapability("os_version", RunCukesTest.browserStackOSVersion);
            }

            caps.setCapability("browserstack.debug", "true");

            try {
                driver = new RemoteWebDriver(new URL(URL), caps);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            return driver;
        }else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("start-maximized");
            if(Boolean.parseBoolean(System.getProperty("linux"))){
                System.setProperty("webdriver.chrome.driver", "../chromedriver");
            }
            else {


                /*
                Map<String, String> mobileEmulation = new HashMap<>();
                //mobileEmulation.put("deviceName", "Galaxy S5");
                mobileEmulation.put("deviceName", "iPhone 8");
                options.setExperimentalOption("mobileEmulation", mobileEmulation);
                */

                System.setProperty("webdriver.chrome.driver", "../chromedriver.exe");
            }

            WebDriver driver = new ChromeDriver(options);

            driver.manage().timeouts().implicitlyWait(Long.parseLong(properties.getProperty("driver.implicit.wait")), TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(Long.parseLong(properties.getProperty("driver.page.load.timeout")), TimeUnit.SECONDS);

            return driver;
        }

    }

}
