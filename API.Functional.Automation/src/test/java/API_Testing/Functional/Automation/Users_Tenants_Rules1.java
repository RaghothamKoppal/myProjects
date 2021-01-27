package API_Testing.Functional.Automation;

import java.io.IOException;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Users_Tenants_Rules1 extends API_Header{
	
	static String testDataSheetName = "AuxiliaryDataController";
	static String testDataTableName = "";
	static String methodStatus = "";
	static long cMilliSeconds = 0;
	
	@BeforeClass
	public void checkRunmode() throws Exception
	{
		if(! (ReadExternalFiles.isGroupRunmodeSet(testDataSheetName)))
			throw new SkipException(testDataSheetName + " runmode is not set to Yes in the RunModes file");
		jsonBodyTextfilePathAndName = jsonBodyTextfilePath+testDataSheetName+" "+Date_Time.getCurrentDate()+"_"+Date_Time.getCurrentTime().replace(":", "-");
		ReadExternalFiles.createTextFile(jsonBodyTextfilePathAndName);
	}
	
	
	
	@DataProvider(name = "DP1")
	public Object[][] createData1() throws IllegalFormatException, IOException, Exception {
		cMilliSeconds = System.currentTimeMillis();System.out.println("cMilliSeconds : " + cMilliSeconds);
		testDataTableName= "Resources_Sites";
		Object[][] data = null;
		cMilliSeconds = System.currentTimeMillis() - cMilliSeconds;
		System.out.println("Data read time : " + cMilliSeconds);		
		Object[][] jsonDataObj=ReadExternalFiles.getJsonData(testDataSheetName,testDataTableName);		
		prepareJsonBody1(testDataSheetName, testDataTableName, jsonDataObj, ReadExternalFiles.getJsonAttributes(testDataSheetName, testDataTableName));
		System.out.println("ipJsonMap is \n" + ipJsonMap);
		return(data);
	}
	
	@Test(dataProvider = "DP1")
	public void Test_Users_ValidAndDependents(LinkedHashMap<String,String> data) throws Exception
	{
		System.out.println("Method execution started for the step : " + data.get("Step_No") );
		//testData = data;
		System.out.println("Test data is : " + data);
		//doExecuteRequest(testDataTableName);
		if(failureMsg.length() > 1){
			methodStatus = "FAILED";
			throw new Exception("TEST FAILED , AND THE REASON IS : " + failureMsg + "\n");
		}
		else
			methodStatus = "Passed";
		System.out.println("Method execution end for the step : " + data.get("Step_No") );	
	}
	
	
	
	
	
	
	//@AfterMethod
	public void storeResultsData() throws Exception
	{
		storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, methodStatus);
		methodStatus = ""; failureMsg = "";testData.clear();
	}
	
	
	
	
	
}
