package API_Testing.Functional.Automation;

import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;


/**
 *@category Test scripts
 *@Description "Users_Tenants_Rules" class holds the test cases related to users/tenants/rules API. On every test case, a new test method is created with DataProvider method.
 *@DataProvider To fetch data for every test method from test data excel file, this annotation method been written
 * @author mshellagi
 */
public class UsersAndTenants extends API_Header{
	
	static String testDataSheetName = "UsersAndTenants"; // This test data table name is going to be same as this class name and respective test data sheet name.
	static String testDataTableName = "";
	static String methodStatus = "";
	static String classStatus = "";
	static long cMilliSeconds = 0;
	
	//static ExtentTest test ;
	//static LinkedHashMap<String, ExtentTest> dpFailedTestsMap = new LinkedHashMap<String, ExtentTest>();
	//static LinkedHashMap<String, String> testStatus = new LinkedHashMap<String, String>();
	
	/**
	 * @category Configurations
	 * @see Executes if the test sheet name given with runmode "Yes".
	 * @throws SkipException if run mode of the sheet is set to no in the "Runmodes.properties" file.
	 */
	@BeforeClass
	public void checkRunmode() throws Exception
	{
		moduleClassExtentMap.put(testDataSheetName, extent.startTest(testDataSheetName));
		//moduleStatus.put(testDataSheetName, "Skipped");
		if(! (ReadExternalFiles.isGroupRunmodeSet(testDataSheetName))){
			classStatus = "Skipped";
			throw new SkipException(testDataSheetName + " runmode is not set to Yes in the RunModes file");
		}
		// Below line prepares the file path in "project folder\\Test_Result\\JsonResponses\\" with the file name begins with respective test data sheet name and ends with timestamp.
		jsonBodyTextfilePathAndName = jsonBodyTextfilePath+testDataSheetName+" "+Date_Time.getCurrentDate()+"_"+Date_Time.getCurrentTime().replace(":", "-");
		ReadExternalFiles.createTextFile(jsonBodyTextfilePathAndName); // Prepared file with path is actually created in the system directory in this line.
	}
	
	
	/**
	 * @category Test_Executor
	 * @Description Complete execution of test case is done by calling this method in the test methods
	 * @param data - Data map retrieved from test data table, with one particular row.
	 * @throws ExecutionException
	 * @throws Exception
	 */
	private void executeTestCase(LinkedHashMap<String,String> data) throws ExecutionException, Exception
	{
		test = extent.startTest(testDataTableName+"__"+data.get("Step_No")); // Creating one more extent start test with test object, it holds a test with step number.
		logger.info("Method execution started for the step : " + data.get("Step_No") );
		testData = data; // In order to access the current running row's test data wherever we want, without passing arguments everywhere.
		putDataIntoOutputExcelMap(); // Whatever the test data by default we want to store in out put excel report files, that are taken out in the map.
		if(executeIfDependentStepPassed(testDataTableName)){
			doExecuteRequest(testDataTableName);
			getMethodStatus(); // This method will return Pass/Fail only. If both are not returned then it will by default methodStatus = "";
		}
		else{
			methodStatus = "Not Run"; // This method status is utilized for excel result files
			test.log(LogStatus.FATAL, "Did not run this step becasue of previous dependent steps failures"); // Use fatal for extent reporting, since not run or related options are not there in Extent reports.
		}logger.info("Method execution end for the step : " + data.get("Step_No") );
	}
	
	
	
	
	@DataProvider(name = "DP1")
	public Object[][] createData1() throws IllegalFormatException, IOException, Exception {
		cMilliSeconds = System.currentTimeMillis();logger.info("cMilliSeconds : " + cMilliSeconds);
		testDataTableName= "TC1-Users_Tenants_Rules_InvalidCases"; // This variable limited to this class. When the next test method is called for execution, the statement should be written over there also, in order to change the method name during execution.
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName)); // since extent is a object of extent test report, startTest method is used to start the execution in order to store the result in Extent report html file ie - customReport.html 
		testStatus.put(testDataTableName, "Skipped"); // Keeping the test data table full execution as skipped in the beginning. Later it changes accordingly based on execution statuses.
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	
	//@Test(dataProvider = "DP1")
	public void runTC1Users_Tenants_Rules(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);logger.info("Execuing the method -- runTC1Users_Tenants_Rules");
	}
	
	
	
	@DataProvider(name = "DP2")
	public Object[][] createData2() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC2-Users_ValidAndDependents";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	
	//@Test(dataProvider = "DP2")
	public void TC2_Users_ValidAndDependents(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);logger.info("Execuing the method -- TC2_Users_ValidAndDependents");
	}
	
	

	@DataProvider(name = "DP3")
	public Object[][] createData3() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC3-Tenants_ValidAndDependents";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	
	//@Test(dataProvider = "DP3")
	public void TC3_Tenants_ValidAndDependents(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);logger.info("Execuing the method -- TC3_Tenants_ValidAndDependents");
	}
	

	@DataProvider(name = "DP4")
	public Object[][] createData4() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC4-CreateTenantsUnderExistingSP";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	
	@Test(dataProvider = "DP4")
	public void TC4_CreateTenantsUnderExistingSP(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP5")
	public Object[][] createData5() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC5-GetTenantDetailsBasedOnId";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	
	//@Test(dataProvider = "DP5")
	public void TC5_GetTenantDetailsBasedOnId(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	
	
	
	
	
	
	@AfterMethod
	public void storeResultsData() throws Exception
	{
		logger.info("after test method execution");
		storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, methodStatus);
		if(!(methodStatus.length()>1)){
			test.log(LogStatus.ERROR, "Improper server response / data customization issue occurance");
			classStatus = "Warn";
		}
		methodStatus = ""; failureMsg = "";testData.clear();
		extent.endTest(test); 
	}
	
	
	
	private static void getMethodStatus() throws Exception
	{
		if(failureMsg.length() > 1){
			testStatus.put(testDataTableName, "FAILED");
			methodStatus = "FAILED";classStatus = "Warn";
			test.log(LogStatus.FAIL, "TEST FAILED , AND THE REASON IS : " + failureMsg);
			throw new Exception("TEST FAILED , AND THE REASON IS : " + failureMsg + "\n");
		}
		else{
			if(!(testStatus.get(testDataTableName).equalsIgnoreCase("Failed")))
				testStatus.put(testDataTableName, "Passed");
			methodStatus = "Passed";
			test.log(LogStatus.PASS, "Successfully executed the step");
		}
	}
	
	
	@AfterTest
	public void testResults()
	{
		logger.info("dpFailedTestsMap.keySet() is : " + dpFailedTestsMap.keySet());
		for(String temp : dpFailedTestsMap.keySet()){
			extent.endTest(dpFailedTestsMap.get(temp));
			
			if(testStatus.get(temp).equalsIgnoreCase("Skipped")){
				classStatus = "Warn";
				dpFailedTestsMap.get(temp).log(LogStatus.SKIP, "Test has been skipped because of it's runmode or DP data");
			}
			else if(testStatus.get(temp).equalsIgnoreCase("Passed"))
				dpFailedTestsMap.get(temp).log(LogStatus.PASS, "Test passed with all steps");
			else if(testStatus.get(temp).equalsIgnoreCase("Failed")){
				classStatus = "Warn";
				dpFailedTestsMap.get(temp).log(LogStatus.FAIL, "Some of the test steps have failed");
			}
		}
		dpFailedTestsMap.clear();
	}
	
	@AfterClass
	public void getClassStatus()
	{
		moduleStatus.put(testDataSheetName, classStatus);
		// Status will be either Passed/Warn/Skipped
		logger.info("Users_Tenants_Rules class status after the execution is : " + classStatus);
	}
	
	
}
