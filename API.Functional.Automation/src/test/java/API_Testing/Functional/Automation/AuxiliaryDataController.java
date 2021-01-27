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
import com.relevantcodes.extentreports.LogSettings;
import com.relevantcodes.extentreports.LogStatus;

public class AuxiliaryDataController extends API_Header {
	
	static String testDataSheetName = "AuxiliaryDataController";
	static String testDataTableName = "";
	static String methodStatus = "";
	static String classStatus = "";
	
	static ExtentTest test ;
	static LinkedHashMap<String, ExtentTest> dpFailedTestsMap = new LinkedHashMap<String, ExtentTest>();
	static LinkedHashMap<String, String> testStatus = new LinkedHashMap<String, String>();
	
	
	@BeforeClass
	public void checkRunmode() throws Exception
	{
		moduleClassExtentMap.put(testDataSheetName, extent.startTest(testDataSheetName)); 
		logger.info("@BeforeClass started executing  with the class name - " + this.getClass().getSimpleName());
		//moduleStatus.put(testDataSheetName, "Skipped");
		if(! (ReadExternalFiles.isGroupRunmodeSet(testDataSheetName))){
			classStatus = "Skipped";
			throw new SkipException(testDataSheetName + " runmode is not set to Yes in the RunModes file");
		}
		jsonBodyTextfilePathAndName = jsonBodyTextfilePath+testDataSheetName+" "+Date_Time.getCurrentDate()+"_"+Date_Time.getCurrentTime().replace(":", "-");
		ReadExternalFiles.createTextFile(jsonBodyTextfilePathAndName);
		logger.info("@BeforeClass executed  successfully ");
	}
	
	
	
	private void executeTestCase(LinkedHashMap<String,String> data) throws ExecutionException, Exception
	{
		test = extent.startTest(testDataTableName+"__"+data.get("Step_No"));
		logger.info("Method execution started for the step : " + data.get("Step_No") );
		testData = data;
		putDataIntoOutputExcelMap();
		if(executeIfDependentStepPassed(testDataTableName)){
			doExecuteRequest(testDataTableName);
			getMethodStatus();}
		else{
			methodStatus = "Not Run";
			test.log(LogStatus.FATAL, "Did not run this step becasue of previous dependent steps failures");
		}logger.info("Method execution end for the step : " + data.get("Step_No") );
	}
	
	
	
	
	@DataProvider(name = "DP1")
	public Object[][] createData1() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC1-AuxilaryDataController";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	
	//@Test(dataProvider = "DP1", priority=1)
	public void runTC1_AuxilaryDataController(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	
	@DataProvider(name = "DP5")
	public Object[][] createData5() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC5-AuxilaryData_Resources_Teams";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP5", priority=5)
	public void TC5_AuxilaryData_Resources_Teams(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	@DataProvider(name = "DP6")
	public Object[][] createData6() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC6-AuxilaryData_Resources_Sites";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP6", priority=6)
	public void TC6_AuxilaryData_Resources_Sites(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	

	@DataProvider(name = "DP7")
	public Object[][] createData7() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC7-AuxilaryData_Resources_VirtualTeams";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP7", priority=7)
	public void TC7_AuxilaryData_Resources_VirtualTeams(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	
	@DataProvider(name = "DP8")
	public Object[][] createData8() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC8-AuxilaryData_Userdata_password-history";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP8", priority=8)
	public void TC8_AuxilaryData_Userdata_password_history(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP9")
	public Object[][] createData9() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC9-AuxilaryData_Userdata_role";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP9", priority=9)
	public void TC9_AuxilaryData_Userdata_role(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	

	@DataProvider(name = "DP10")
	public Object[][] createData10() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC10-AuxilaryData_Userdata_user-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP10", priority=10)
	public void TC10_AuxilaryData_Userdata_user_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP11")
	public Object[][] createData11() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC11-AuxilaryData_Userdata_module";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP11", priority=11)
	public void TC11_AuxilaryData_Userdata_module(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	

	
	@DataProvider(name = "DP12")
	public Object[][] createData12() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC12-AuxilaryData_Userdata_password-policy";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP12", priority=12)
	public void TC12_AuxilaryData_Userdata_password_policy(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	

	@DataProvider(name = "DP13")
	public Object[][] createData13() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC13-AuxilaryData_Userdata_rule";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP13", priority=13)
	public void TC13_AuxilaryData_Userdata_rule(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	@DataProvider(name = "DP14")
	public Object[][] createData14() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC14-AuxilaryData_Userdata_permission";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP14", priority=14)
	public void TC14_AuxilaryData_Userdata_permission(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	@DataProvider(name = "DP15")
	public Object[][] createData15() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC15-AuxilaryData_Userdata_attribute";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP15", priority=15)
	public void TC15_AuxilaryData_Userdata_attribute(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	@DataProvider(name = "DP16")
	public Object[][] createData16() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC16-AuxilaryData_Userdata_user";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP16", priority=16)
	public void TC16_AuxilaryData_Userdata_user(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP17")
	public Object[][] createData17() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC17-AuxilaryData_Userdata_policy-set";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP17", priority=17)
	public void TC17_AuxilaryData_Userdata_policy_set(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	@DataProvider(name = "DP18")
	public Object[][] createData18() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC18-AuxilaryData_Userdata_policy";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP18", priority=18)
	public void TC18_AuxilaryData_Userdata_policy(LinkedHashMap<String,String> data) throws Exception
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
	
	@AfterTest
	public void storeResultFile() throws Exception
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
	
	@AfterClass
	public void getClassStatus()
	{
		moduleStatus.put(testDataSheetName, classStatus);
		// Status will be either Passed/Warn/Skipped
		logger.info("ADCDemo class status after the execution is : " + classStatus);
	}
	
	
	
}
