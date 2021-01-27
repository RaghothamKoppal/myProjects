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

public class AuxResources extends API_Header {
	
	static String testDataSheetName = "AuxResources";
	static String testDataTableName = "";
	static String methodStatus = "";
	static String classStatus = "";
	
	
	
	@BeforeClass
	public void checkRunmode() throws Exception
	{
		moduleClassExtentMap.put(testDataSheetName, extent.startTest(testDataSheetName)); 
		//moduleStatus.put(testDataSheetName, "Skipped");
		if(! (ReadExternalFiles.isGroupRunmodeSet(testDataSheetName))){
			classStatus = "Skipped";
			throw new SkipException(testDataSheetName + " runmode is not set to Yes in the RunModes file");
		}
		jsonBodyTextfilePathAndName = jsonBodyTextfilePath+testDataSheetName+" "+Date_Time.getCurrentDate()+"_"+Date_Time.getCurrentTime().replace(":", "-");
		ReadExternalFiles.createTextFile(jsonBodyTextfilePathAndName);
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
		testDataTableName= "TC1-AuxilaryData_Resources_pop-mapping";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	
	@Test(dataProvider = "DP1", priority=1)
	public void runTC1_AuxilaryDataController(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	
	@DataProvider(name = "DP2")
	public Object[][] createData2() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC2-AuxilaryData_Resources_server-mapping";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP2", priority=2)
	public void TC2_AuxilaryData_Resources_server_mapping(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	

	@DataProvider(name = "DP3")
	public Object[][] createData3() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC3-AuxilaryData_Resources_vteam-tfn-mapping";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP3", priority=3)
	public void TC3_AuxilaryData_Resources_vteam_tfn_mapping(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP4")
	public Object[][] createData4() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC4-AuxilaryData_Resources_bulk-upload-status";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP4", priority=4)
	public void TC4_AuxilaryData_Resources_bulk_upload_status(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP5")
	public Object[][] createData5() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC5-AuxilaryData_Resources_pwd-policy";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP5", priority=5)
	public void TC5_AuxilaryData_Resources_pwd_policy(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	
	@DataProvider(name = "DP6")
	public Object[][] createData6() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC6-AuxilaryData_Resources_outdial-ani-list-entry";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP6", priority=6)
	public void TC6_AuxilaryData_Resources_outdial_ani_list_entry(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	

	@DataProvider(name = "DP7")
	public Object[][] createData7() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC7-AuxilaryData_Resources_ccg-tenant";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP7", priority=7)
	public void TC7_AuxilaryData_Resources_ccg_tenant(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	

	@DataProvider(name = "DP8")
	public Object[][] createData8() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC8-AuxilaryData_Resources_media-server";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP8", priority=8)
	public void TC8_AuxilaryData_Resources_media_server(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	

	@DataProvider(name = "DP9")
	public Object[][] createData9() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC9-AuxilaryData_Resources_other-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP9", priority=9)
	public void TC9_AuxilaryData_Resources_other_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	

	@DataProvider(name = "DP10")
	public Object[][] createData10() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC10-AuxilaryData_Resources_adhoc-dial-properties";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP10", priority=10)
	public void TC10_AuxilaryData_Resources_adhoc_dial_properties(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	

	@DataProvider(name = "DP11")
	public Object[][] createData11() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC11-AuxilaryData_Resources_chat-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP11", priority=11)
	public void TC11_AuxilaryData_Resources_chat_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	@DataProvider(name = "DP12")
	public Object[][] createData12() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC12-AuxilaryData_Resources_email-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP12", priority=12)
	public void TC12_AuxilaryData_Resources_email_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	@DataProvider(name = "DP13")
	public Object[][] createData13() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC13-AuxilaryData_Resources_threshold-rule";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP13", priority=13)
	public void TC13_AuxilaryData_Resources_threshold_rule(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	@DataProvider(name = "DP14")
	public Object[][] createData14() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC14-AuxilaryData_Resources_vpop-ms-mapping";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP14", priority=14)
	public void TC14_AuxilaryData_Resources_vpop_ms_mapping(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	@DataProvider(name = "DP15")
	public Object[][] createData15() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC15-AuxilaryData_Resources_queue-dn-pool";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP15", priority=15)
	public void TC15_AuxilaryData_Resources_queue_dn_pool(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP16")
	public Object[][] createData16() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC16-AuxilaryData_Resources_gw-inventory";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP16", priority=16)
	public void TC16_AuxilaryData_Resources_gw_inventory(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	@DataProvider(name = "DP17")
	public Object[][] createData17() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC17_AuxilaryData_Resources_customer";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP17", priority=17)
	public void TC17_AuxilaryData_Resources_customer(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	@DataProvider(name = "DP18")
	public Object[][] createData18() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC18-AuxilaryData_Resources_agent-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP18", priority=18)
	public void TC18_AuxilaryData_Resources_agent_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP19")
	public Object[][] createData19() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC19-AuxilaryData_Resources_password-policy";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP19", priority=19)
	public void TC19_AuxilaryData_Resources_password_policy(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP20")
	public Object[][] createData20() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC20-AuxilaryData_Resources_media-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP20", priority=20)
	public void TC20_AuxilaryData_Resources_media_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	@DataProvider(name = "DP21")
	public Object[][] createData21() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC21-AuxilaryData_Resources_video-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP21", priority=21)
	public void TC21_AuxilaryData_Resources_video_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP22")
	public Object[][] createData22() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC22-AuxilaryData_Resources_module";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP22", priority=22)
	public void TC22_AuxilaryData_Resources_module(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP23")
	public Object[][] createData23() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC23-AuxilaryData_Resources_business-metric";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP23", priority=23)
	public void TC23_AuxilaryData_Resources_business_metric(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	

	@DataProvider(name = "DP24")
	public Object[][] createData24() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC24-AuxilaryData_Resources_vpop";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP24", priority=24)
	public void TC24_AuxilaryData_Resources_vpop(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP25")
	public Object[][] createData25() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC25-AuxilaryData_Resources_cad-variable";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP25", priority=25)
	public void TC25_AuxilaryData_Resources_cad_variable(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	
	@DataProvider(name = "DP26")
	public Object[][] createData26() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC26-AuxilaryData_Resources_aux-code";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP26", priority=26)
	public void TC26_AuxilaryData_Resources_aux_code(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP27")
	public Object[][] createData27() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC27-AuxilaryData_Resources_tfn";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP27", priority=27)
	public void TC27_AuxilaryData_Resources_tfn(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP28")
	public Object[][] createData28() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC28-AuxilaryData_Resources_speed-dial-list-entry";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP28", priority=28)
	public void TC28_AuxilaryData_Resources_speed_dial_list_entry(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP29")
	public Object[][] createData29() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC29-AuxilaryData_Resources_CCG";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP29", priority=29)
	public void TC29_AuxilaryData_Resources_CCG(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP30")
	public Object[][] createData30() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC30-AuxilaryData_Resources_speed-dial-list";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP30", priority=30)
	public void TC30_AuxilaryData_Resources_speed_dial_list(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP31")
	public Object[][] createData31() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC31-AuxilaryData_Resources_role";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP31", priority=31)
	public void TC31_AuxilaryData_Resources_role(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP32")
	public Object[][] createData32() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC32-AuxilaryData_Resources_work-type";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP32", priority=32)
	public void TC32_AuxilaryData_Resources_work_type(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP33")
	public Object[][] createData33() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC33-AuxilaryData_Resources_skill-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP33", priority=33)
	public void TC33_AuxilaryData_Resources_skill_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP34")
	public Object[][] createData34() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC34-AuxilaryData_Resources_outdial-ani-list";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP34", priority=34)
	public void TC34_AuxilaryData_Resources_outdial_ani_list(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP35")
	public Object[][] createData35() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC35-AuxilaryData_Resources_fax-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP35", priority=35)
	public void TC35_AuxilaryData_Resources_fax_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	
	@DataProvider(name = "DP36")
	public Object[][] createData36() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC36-AuxilaryData_Resources_third-party-profile";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP36", priority=36)
	public void TC36_AuxilaryData_Resources_third_party_profile(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP37")
	public Object[][] createData37() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC37-AuxilaryData_Resources_skill";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP37", priority=37)
	public void TC37_AuxilaryData_Resources_skill(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	

	@DataProvider(name = "DP38")
	public Object[][] createData38() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC38-AuxilaryData_Resources_third-party-vendor";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP38", priority=38)
	public void TC38_AuxilaryData_Resources_third_party_vendor(LinkedHashMap<String,String> data) throws Exception
	{
		executeTestCase(data);
	}
	
	
	@DataProvider(name = "DP39")
	public Object[][] createData39() throws IllegalFormatException, IOException, Exception {
		testDataTableName= "TC39-AuxilaryData_Resources_ep-queue-group";
		dpFailedTestsMap.put(testDataTableName, extent.startTest(testDataTableName));
		testStatus.put(testDataTableName, "Skipped");
		return(getTestcaseData(testDataSheetName, testDataTableName));
	}
	@Test(dataProvider = "DP39", priority=39)
	public void TC39_AuxilaryData_Resources_ep_queue_group(LinkedHashMap<String,String> data) throws Exception
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
		logger.info("ResourcesDemo class status after the execution is : " + classStatus);
	}
	
	
}
