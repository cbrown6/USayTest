//
//****
//**** SharedDriver HAS TO BE IN STEPDEFS
//****
//
package stepdefs;

import config.*;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.runtime.model.CucumberScenario;
import edu.emory.mathcs.backport.java.util.Collections;
import gherkin.formatter.model.Step;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import static pages.Page.webDriver;

public class SharedDriver extends EventFiringWebDriver {

	private static final WebDriver REAL_DRIVER = new DriverFactory().getDriver();
	private CucumberScenario cucumberScenario;
	private String server;
	private static Boolean ignoredRetest = false, notInScope=false;
	public static String testRunID, testSuiteID, testSuiteName, testSection, testCaseID, status;
	public static JSONArray cases, sections;
	public static Config runConfig = new Config(RunCukesTest.testRailOutput, RunCukesTest.testRailProjectID, RunCukesTest.testRailSuiteID, RunCukesTest.testRailRunID, RunCukesTest.mileStoneID);
	public static TestRailAPI testRailAPI = new TestRailAPI();
	private static boolean apiSetUp;
	private PropertiesReader propertiesReader = new PropertiesReader();

	private Map<String, String> sharedData = Collections.synchronizedMap(new HashMap());

	private static final Thread CLOSE_THREAD = new Thread() {
		@Override
		public void run() {
			REAL_DRIVER.quit();
		}
	};

	static {
		Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
	}

	public void setServer(String server) {
		this.server = server;
	}

	public SharedDriver() throws IOException, APIException {
		super(REAL_DRIVER);

		// Set EventListener to check for certain Exceptions
		IAmTheEventListener eventListener = new IAmTheEventListener();
		register(eventListener);
		//navigate().to("https://uk.rs-online.com/web/");

	}

	@Override
	public void close() {
		if (Thread.currentThread() != CLOSE_THREAD) {
			throw new UnsupportedOperationException("Driver shared and will close when the JVM exits.");
		}
		super.close();
	}

	@Before
	public synchronized void setUp(Scenario scenario) throws Exception {

		apiSetUp(scenario);



		manage().deleteAllCookies();
		notInScope=false;


		if (runConfig.getTestRailOutput()) {

			cucumberScenario = testRailAPI.reflectCucumberScenarioField(scenario);
			if (testInScope(scenario)){
				notInScope = true;
				org.junit.Assume.assumeTrue(false);

			}
			processSectionName(getFeatureTagName(scenario));
			processTestCase(scenario);
		}
		setSharedData("orderRefNum", "");
	}

	private void processSectionName(String sectionName) throws Exception {
		long sectionID = getSectionID("Automated Regression");
		if (sectionID <= 0) {
			JSONObject newSection = testRailAPI.newTestSection("Automated Regression", testSuiteID);
			sections.add(newSection);
			testSection = newSection.get("id").toString();
		} else {
			testSection = String.valueOf(sectionID);
		}
	}

	private void processTestCase(Scenario scenario) throws Exception {
		JSONObject thisCase = getTestCase(scenario.getName(), cases);
		Boolean incomplete = getIncompleteFeatureTagName(scenario);

		if (thisCase == null) {
			JSONArray suiteCases = testRailAPI.getSuiteCases(runConfig.getTestRailProjectID(), testSuiteID);
			thisCase = getTestCase(scenario.getName(), suiteCases);

			if (thisCase == null) {
				JSONObject newTestCase = testRailAPI.newTestCase(testSuiteID, scenario.getName());
				cases.add(newTestCase);
				testCaseID = newTestCase.get("id").toString();
				cases = testRailAPI.getTestsFromTestRun(testRunID);

				thisCase = getTestCase(scenario.getName(), cases);

				testCaseID = thisCase.get("id").toString();

			} else {
				testCaseID = thisCase.get("id").toString();
				ignoredRetest = false;


				String thisCaseResult = thisCase.get("status_id").toString();

				if (thisCaseResult.equals("1") || thisCaseResult.equals("6")) {
					ignoredRetest = true;
					org.junit.Assume.assumeTrue(false);
				}

			}
		} else {
			testCaseID = thisCase.get("id").toString();
			ignoredRetest = false;


			String thisCaseResult = thisCase.get("status_id").toString();

			if (thisCaseResult.equals("1") || thisCaseResult.equals("6")) {
				ignoredRetest = true;
				org.junit.Assume.assumeTrue(false);
			}

		}
		if (incomplete) {
			org.junit.Assume.assumeTrue(false);
		}
	}

	private long getSectionID(String sectionName) {
		if (sections == null || sections.isEmpty()) {
			return -1;
		} else {
			for (int i = 0; i < sections.size(); i++) {
				JSONObject thisSection = (JSONObject) sections.get(i);

				if (sectionName.equals(thisSection.get("name").toString())) {
					return (long) thisSection.get("id");
				}
			}
			return -1;
		}
	}

	private JSONObject getTestCase(String testCaseName, JSONArray cases) {
		if (cases == null) {
			return null;
		} else {
			if (cases.isEmpty()) {
				return null;
			} else {
				for (int i = 0; i < cases.size(); i++) {
					JSONObject thisCase = (JSONObject) cases.get(i);

					if (testCaseName.equals(thisCase.get("title").toString())) {
						return thisCase;
					}
				}
				return null;
			}
		}
	}

	private String getFeatureTagName(Scenario scenario) {
		for (String tag : scenario.getSourceTagNames()) {
			if (tag.matches("@Feature_(.*)")) {
				tag = tag.replace("@Feature_", "");
				return StringUtils.capitalize(tag);
			}
		}
		return "";
	}

	private Boolean getIncompleteFeatureTagName(Scenario scenario) {
		for (String tag : scenario.getSourceTagNames()) {
			if (tag.matches("@Incomplete")) {
				return true;
			}
		}
		return false;
	}

	@After
	public void tearDown(Scenario scenario) throws IOException, APIException {
		/**
		 * Ouput the result of the test case to each test case
		 * 1 = Passed
		 * 4 = ReTest
		 * Other statuses are not currently required for use
		 * Add a comment to each completed test case containing the BDD scenario of the case
		 */


		if (runConfig.getTestRailOutput()) {
			Map Result = new HashMap();
			int status = 3;

			switch (scenario.getStatus()) {
				case "passed":
					status = 1;
					break;
				case "undefined":
					status = 2;
					break;
				case "pending":
					status = 2;
					break;
				case "skipped":
					status = 4;
					break;
				case "failed":
					status = 5;
					break;
			}

			if (notInScope){
				status = 6;
			}


			if (cucumberScenario != null && !notInScope) {
				Result.put("status_id", status);
				List<Step> steps = cucumberScenario.getSteps();
				String comment = "BDD";

				for (int i = 0; i < steps.size(); i++) {
					comment = comment + "\n" + steps.get(i).getKeyword() + steps.get(i).getName();
				}

				try {
					Field field = scenario.getClass().getDeclaredField("stepResults");
					field.setAccessible(true);
					List<gherkin.formatter.model.Result> myResults = (List<gherkin.formatter.model.Result>) field.get(scenario);
					String errorMessage = "";
					String orderRef = sharedData.get("orderRefNum");
					for (gherkin.formatter.model.Result myResult : myResults) {
						if (myResult.getErrorMessage() != null) {
							errorMessage = errorMessage + myResult.getErrorMessage();
						}
					}

					if(orderRef == null){
						comment = comment + "\n\n" + errorMessage;
					}else{
						comment = comment + "\n\n" + errorMessage + "\n\n Order Confirmation Number : " + orderRef;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

				Result.put("comment", comment);


				if (!notInScope) {
					testRailAPI.addResultRun(testCaseID, Result);
				} else {
					testRailAPI.addResultRun(testRunID, testCaseID, Result);
				}
			}
		}

		/**
		 * 	On failure, embed a screenshot in the test report
		 */
		if (scenario.isFailed()) {

			try {
				Thread.sleep(2000);
			}catch(InterruptedException e){
				System.err.println(e);
			}

			scenario.write("Server number: " + server);
			try {
				byte[] screenshot = getScreenshotAs(OutputType.BYTES);
				scenario.embed(screenshot, "image/png");
			} catch (WebDriverException somePlatformsDontSupportScreenshots) {
				System.err.println(somePlatformsDontSupportScreenshots.getMessage());
			}
		}

		if (isAlertPresent()) {
			System.out.print("An Alert Popped Up");
			webDriver.switchTo().alert();
			webDriver.switchTo().alert().accept();
			webDriver.switchTo().defaultContent();
		}



	}
	public boolean isAlertPresent() {
		try {
			webDriver.switchTo().alert();
			return true;
		} // try
		catch (Exception e) {
			return false;
		} // catch
	}

	public String getSharedData(String key) {
		return sharedData.get(key);
	}

	public void setSharedData(String key, String value) {
		sharedData.put(key, value);
	}



	public Boolean testInScope(Scenario scenario){
		for (String tag : scenario.getSourceTagNames()) {
			if (tag.contains("not" + System.getProperty("country"))) {
				return true;
			}
		}
		return false;
	}

	public void clickAndSendKeys(WebElement elem, String keys){
		elem.clear();
		elem.sendKeys(keys);
	}

	public synchronized void apiSetUp(Scenario scenario) throws IOException, APIException, InterruptedException {


		if (runConfig.getTestRailOutput()) {
			testRailAPI.setupAPI();

			String currentBrowser = RunCukesTest.browserStackBrowser+RunCukesTest.browserStackBrowserVersion;
			String currentCountry = System.getProperty("country").toUpperCase();

			if(Boolean.parseBoolean(System.getProperty("runFromCMD"))) {
				currentBrowser = System.getProperty("browser")+ System.getProperty("browserVersion");
			} else if (!RunCukesTest.useBrowserStack){
				currentBrowser = "localRun";
			}
			else{
				currentBrowser = RunCukesTest.browserStackBrowser + RunCukesTest.browserStackBrowserVersion;
			}

			JSONObject plan = testRailAPI.getTestPlan(RunCukesTest.testRailTestPlan);
			runConfig.setTestRailProjectID(String.valueOf(plan.get("project_id")));

			JSONArray planEntries = (JSONArray) plan.get("entries");
			JSONArray configs =  testRailAPI.getConfigs(plan.get("project_id").toString());
			HashMap<String, String> browserConfigOptions = getConfigHashMaps(configs, "Browser");
			HashMap<String, String> countryConfigOptions = getConfigHashMaps(configs, "Country");


			String currentBrowserConfigCode = browserConfigOptions.get(currentBrowser);
			if (currentBrowserConfigCode==null){
				addConfigToGroup(configs, "Browser", currentBrowser);
			}
			String currentCountryConfigCode = countryConfigOptions.get(currentCountry);
			if (currentCountryConfigCode==null){
				addConfigToGroup(configs, "Country", currentCountry);
			}





			String featureName = getFeatureTagName(scenario);

			JSONObject Entry = matchStringFromJsonArray(planEntries,"name",featureName );
			JSONArray runs;

			//if not null entry exists in plan, and can be set
			if (Entry != null){
				testSuiteID = Entry.get("suite_id").toString();
				runConfig.setTestRailSuiteID(testSuiteID);
				runs = (JSONArray) Entry.get("runs");
			} else {
				JSONArray projectSuites = (JSONArray) testRailAPI.getProjectSuites(plan.get("project_id").toString());
				JSONObject suiteNotAdded =  matchStringFromJsonArray(projectSuites,"name",featureName );
				if (suiteNotAdded !=null) {
					JSONObject newEntry = testRailAPI.addTestPlanEntry(RunCukesTest.testRailTestPlan, suiteNotAdded.get("id").toString(), suiteNotAdded.get("name").toString(), browserConfigOptions.get(currentBrowser),countryConfigOptions.get(currentCountry) );
					testSuiteID = newEntry.get("suite_id").toString();
					runConfig.setTestRailSuiteID(testSuiteID);
					runs = (JSONArray) newEntry.get("runs");
				} else {
					JSONObject newSuite = testRailAPI.newTestSuite(featureName, "Selenium Automation pack test suite");

					JSONObject newEntry2 = testRailAPI.addTestPlanEntry(RunCukesTest.testRailTestPlan, newSuite.get("id").toString(), newSuite.get("name").toString(), browserConfigOptions.get(currentBrowser),countryConfigOptions.get(currentCountry));
					testSuiteID = newEntry2.get("suite_id").toString();
					runConfig.setTestRailSuiteID(testSuiteID);
					runs = (JSONArray) newEntry2.get("runs");
				}
			}



			JSONObject Run = matchStringFromJsonArray(runs, "config_ids", currentBrowserConfigCode, currentCountryConfigCode);


			if (Run != null) {
				testRunID = Run.get("id").toString();
				runConfig.setTestRailRunID(testRunID);
			} else {
				JSONObject newEntry4 = testRailAPI.addTestPlanEntry(RunCukesTest.testRailTestPlan, testSuiteID, featureName, browserConfigOptions.get(currentBrowser),countryConfigOptions.get(currentCountry));
				runs = (JSONArray) newEntry4.get("runs");
				Run = matchStringFromJsonArray(runs,"config_ids",currentBrowserConfigCode,currentCountryConfigCode);
				testRunID = Run.get("id").toString();
				runConfig.setTestRailRunID(testRunID);
			}


//            if (runConfig.getCreateNewTestSuite()) {
//                JSONObject newSuite = testRailAPI.newTestSuite("Automated Regression - ", "Selenium Automation pack test suite");
//                testSuiteID = newSuite.get("id").toString();
//                testSuiteName = newSuite.get("name").toString();
//                testRunID = addTestRun(testSuiteName, runConfig);
//
//                cases = testRailAPI.getSuiteCases(runConfig.getTestRailProjectID(), testSuiteID);
//            } else {
//                testSuiteID = runConfig.getTestRailSuiteID();

//                if (runConfig.getTestRailReTest()) {
			testRunID = runConfig.getTestRailRunID();
			JSONObject suiteConfig = testRailAPI.getConfigFromTestRun(testRunID);
			cases = testRailAPI.getTestsFromTestRun(testRunID);

//                } else {
//                    testSuiteName = testRailAPI.getSuiteName(testSuiteID);
//                        if (runConfig.getTestRailRunID() == null) {
//                            testRunID = addTestRun(testSuiteName, runConfig);
//                            runConfig.setTestRailRunID(testRunID);
//                        } else {
//                            testRunID = runConfig.getTestRailRunID();
//                        }
//                        cases = testRailAPI.getSuiteCases(runConfig.getTestRailProjectID(), testSuiteID);


//                }


			sections = testRailAPI.getSuiteSections(runConfig.getTestRailProjectID(), testSuiteID);
		}

	}

	private JSONObject matchStringFromJsonArray(JSONArray jsonArray, String searchString, String featureName) {
		for (Object entry : jsonArray) {
			JSONObject Entry = (JSONObject) entry;
			String name = (String) Entry.get(searchString);
			if (name.equals(featureName)) {
				return Entry;
			}

		}
		return null;
	}

	private JSONObject matchStringFromJsonArray(JSONArray jsonArray, String searchString, String configOne, String configTwo) {
		for (Object entry : jsonArray) {
			JSONObject Entry = (JSONObject) entry;
			String config_ids =  Entry.get(searchString).toString();
			if (config_ids.contains(configOne) && config_ids.contains(configTwo)) {
				return Entry;
			}

		}
		return null;
	}

	private HashMap<String, String> getConfigHashMaps(JSONArray configs, String configName){
		HashMap<String, String> returnable = new HashMap<>();
		JSONObject browsers = matchStringFromJsonArray(configs,"name",configName);
		JSONArray browserConfigs = (JSONArray) browsers.get("configs");
		for (Object b: browserConfigs){
			JSONObject c = (JSONObject) b;
			returnable.put(c.get("name").toString(), c.get("id").toString());
		}
		return returnable;
	}

	private String addConfigToGroup (JSONArray configs,String configGroupName,String configName) throws IOException, APIException {
		JSONObject configGroup = matchStringFromJsonArray(configs,"name",configGroupName);
		String configGroupID = String.valueOf(configGroup.get("id"));
		JSONObject config = testRailAPI.addConfigToGroup(configGroupID, configName);
		return String.valueOf(config.get("id"));
	}

	private synchronized String addTestRun(String testRunName, Config runConfig) throws IOException, APIException {
		String runName = testRunName
				+ " - "
				+ StringUtils.upperCase(System.getProperty("country"))
				+ " Market";

		return testRailAPI.
				addTestRunToProject(testSuiteID, runName, runConfig.getTestRailProjectID()).
				get("id").toString();
	}

	public PropertiesReader getPropertiesReader() {
		return propertiesReader;
	}

	public void setPropertiesReader(PropertiesReader propertiesReader) {
		this.propertiesReader = propertiesReader;
	}

}