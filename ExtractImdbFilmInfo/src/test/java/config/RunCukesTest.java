package config;

import cucumber.api.CucumberOptions;

import cucumber.api.junit.Cucumber;

import org.apache.commons.lang3.StringUtils;

import org.json.simple.JSONArray;

import org.json.simple.JSONObject;

import org.junit.BeforeClass;

import org.junit.runner.RunWith;



import java.io.IOException;

@RunWith(Cucumber.class)

@CucumberOptions(plugin = {"pretty", "html:target/cucumber", "json:target/cucumber.json"},

        features = "src/test/resources/cucumber/",

        glue = {"stepdefs"},

        tags = {"@dont"} )



public class RunCukesTest {


    public static boolean testRailOutput = false;

    public static String testRailTestPlan = "8145";



    public static boolean useBrowserStack = false ;

    public static String browserStackBrowser = "Firefox";

    public static String browserStackBrowserVersion = "50";

    public static String browserStackOS = "Windows";

    public static String browserStackOSVersion = "8.1";



    public static boolean useAndroid = true;

    public static boolean useIOS = false;



    public static String testRailSuiteID = "" ;

    public static String testRailRunID = "";

    public static String mileStoneID = "";

    public static String testRailProjectID = "1";

}