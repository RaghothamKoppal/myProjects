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

import com.relevantcodes.extentreports.LogStatus;

public class TenantBranding extends API_Header{
	
	
	static String testDataSheetName = "TenantBranding"; // This test data table name is going to be same as this class name and respective test data sheet name.
	static String testDataTableName = "";
	static String methodStatus = "";
	static String classStatus = "";
	static long cMilliSeconds = 0;
	
	
	
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
		testDataTableName= "TC1-TenantBrandingRequests";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP1", priority=1)
	public void TC1_TenantBrandingRequests(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);logger.info("Execuing the method -- TC1-TenantBrandingRequests");
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
	public void storeResultFile()
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
		logger.info("ADCDemo class status after the execution is : " + classStatus);
	}
	
}
