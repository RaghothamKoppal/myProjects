package API_Testing.Functional.Automation;

import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class AuxiliaryDataController1 extends API_Header{
	
	static String testDataSheetName = "AuxiliaryDataController";
	static String testDataTableName = "";
	static String methodStatus = "";
	
	@BeforeClass
	public void checkRunmode() throws Exception
	{
		if(! (ReadExternalFiles.isGroupRunmodeSet(testDataSheetName)))
			throw new SkipException(testDataSheetName + " runmode is not set to Yes in the RunModes file");
		jsonBodyTextfilePathAndName = jsonBodyTextfilePath+testDataSheetName+" "+Date_Time.getCurrentDate()+"_"+Date_Time.getCurrentTime().replace(":", "-");
		ReadExternalFiles.createTextFile(jsonBodyTextfilePathAndName);
	}
	
	
	
	
	@DataProvider(name = "DP5")
	public Object[][] createData5() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC51-AuxilaryData_Resources_Teams";
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	//@Test(dataProvider = "DP5", priority=5)
	public void TC5_AuxilaryData_Resources_Teams(LinkedHashMap<String,String> data) throws Exception
	{
		System.out.println("Method execution started for the step : " + data.get("Step_No") );
		testData = data;
		if(testStepResultMap.get(testDataTableName+"_"+testData.get("Step_No")).equalsIgnoreCase("Passed")){
		doExecuteRequest(testDataTableName);
		if(failureMsg.length() > 1){
			methodStatus = "FAILED";
			throw new Exception("TEST FAILED , AND THE REASON IS : " + failureMsg + "\n");
		}
		else
			methodStatus = "Passed";
		}
		else
			
		System.out.println("Method execution end for the step : " + data.get("Step_No") );
	}
	
	
	
	
	@Test(dataProvider = "DP5", priority=5)
	public void TC51_AuxilaryData_Resources_Teams(LinkedHashMap<String,String> data) throws Exception
	{
		System.out.println("Method execution started for the step : " + data.get("Step_No") );
		testData = data;
		doExecuteRequest(testDataTableName);
		if(failureMsg.length() > 1){
			methodStatus = "FAILED";
			throw new Exception("TEST FAILED , AND THE REASON IS : " + failureMsg + "\n");
		}
		else
			methodStatus = "Passed";
		System.out.println("Method execution end for the step : " + data.get("Step_No") );
	}
	
	
	
	
	
	
	@AfterMethod
	public void storeResultsData() throws Exception
	{
		System.out.println("after test method execution");
		storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, methodStatus);
		methodStatus = ""; failureMsg = "";testData.clear();
	}
	
	
	
	
	
	
	
}
