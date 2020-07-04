package config;

public class Config {

    private static String runMode = System.getProperty("runFromCMD");

    private boolean testRailOutput;
    private String testRailProjectID;

    private String testRailSuiteID ;

    private String testRailRunID;

    private String milestoneID;

    public void setTestRailOutput (boolean output){
        this.testRailOutput = output;
    }

    public void setTestRailProjectID (String projetID){
        this.testRailProjectID = projetID;
    }


    public void setTestRailSuiteID (String suiteID){
        this.testRailSuiteID = suiteID;
    }

    public void setTestRailRunID (String runID){
        this.testRailRunID = runID;
    }

    public void setMilestoneID (String mileStoneID){
        this.milestoneID = mileStoneID;
    }

    public boolean getTestRailOutput (){
        return testRailOutput;
    }

    public String getTestRailProjectID(){
        return testRailProjectID;
    }

    public String getTestRailSuiteID(){
        return testRailSuiteID;
    }

    public String getTestRailRunID (){
        return testRailRunID;
    }

    public String getMilestoneID(){
        return milestoneID;
    }

    public Config (boolean output,String projectID,String suiteID,String runID, String milestoneID) {
        this.testRailOutput = output;
        this.testRailProjectID = projectID;
        this.testRailSuiteID = suiteID;
        this.testRailRunID = runID;
        this.milestoneID = milestoneID;

    }





    /**
     * TestRail Specific configuration components
     * testRailOutput - true will output details to new test suite and run in TestRail
     * testRailProjectID - ID of project to output to in Testrail n.b. Automation project is 50, Test is 1
     * createNewTestSuite - when set to true, a new test suite will be created and all test cases added to it
     * testRailSuiteID - if createNewTestSuite is false, creates a run of this test suite and adds cases to it
     * testRailReTest - if true testRailRunId must be populated.  The test results will update the existing test run specified
     * testRailRunID - Run to be updated by retesting results
     */

//    public static void setConfig(){
//        if (!StringUtils.isEmpty(runMode)){
//            /*************************
//             *  DO NOT EDIT ANY OF THE VALUES BETWEEN HERE
//             *************************/
//            testRailOutput = true;
//            testRailProjectID = "87";
//
//            createNewTestSuite = false;
//            testRailSuiteID = System.getProperty("suiteId");
//
//            testRailReTest = true;
//            testRailRunID = System.getProperty("runId");
//            /********** AND HERE ************************/
//        }
//
//        else{
//            /** PLEASE EDIT ONLY THE VALUES BELOW TO OUTPUT TO TESTRAIL*/
//
//            testRailOutput = true;
//            testRailProjectID = "55";
//
//            createNewTestSuite = false;
//            testRailSuiteID = "2699";
//
//            testRailReTest = true;
//            testRailRunID = "4386";
//        }
//    }

//Test project id = 1
//NGSP project id = 81











}
