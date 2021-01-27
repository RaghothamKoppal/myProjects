package API_Testing.Functional.Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.DataFormatException;

import javax.mail.internet.AddressException;
import javax.management.InvalidAttributeValueException;
import javax.xml.bind.PropertyException;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.eventusermodel.HSSFUserException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.testng.SkipException;

import com.sun.javafx.fxml.PropertyNotFoundException;

public class DataFilesValidator {
	
	public static String configPropertiesFile = ReadExternalFiles.configPropertiesFile;
	public static String testdataWorkbookFile = ReadExternalFiles.testdataWorkbookFile;
	public static String apiPathsPropertiesFile = ReadExternalFiles.apiPathsPropertiesFile;
	public static String runModesPropertiesFile = ReadExternalFiles.runModesPropertiesFile;
	static Logger logger = API_Header.logger;
	
	public static void doPreSuiteValidations() throws AddressException, PropertyException, IOException, InvalidAttributeValueException, InvalidAddress, HSSFUserException, ServerNotActiveException
	{
		logger.info("Started Executing doPreSuiteValidations");
		checkFilesExistance();
		validateConfigFileContent();
		checkServerActive();
		validateRunmodeFile();
		validateAPIPaths();
		
		List<String> activeRunmodes = ReadExternalFiles.readPropertiesWithActiveRunmode();
		FileInputStream fis = new FileInputStream(testdataWorkbookFile);
		HSSFWorkbook workbook =  new HSSFWorkbook(fis);
		List<String> listOfSheets = new ArrayList<String>();
		for (int i = 0; i < workbook.getNumberOfSheets(); i++)
			listOfSheets.add(workbook.getSheetName(i));
		List<String> nonExistanceRunmodes = new ArrayList<String>();
		for(String k : activeRunmodes)
			if(!TestUtils.checkStringExistanceInList(listOfSheets, k))
				nonExistanceRunmodes.add(k);
		if(nonExistanceRunmodes.size() > 0)
		{
			logger.error("Runmodes set in properties file's data sheets are not availiable in Testdata.xls file. They are : " + nonExistanceRunmodes);
			throw new HSSFUserException("Runmodes set in properties file's data sheets are not availiable in Testdata.xls file. They are : " + nonExistanceRunmodes);
		}
		logger.info("Excuted doPreSuiteValidations method successfully");
	}
	
	private static void checkServerActive() throws IOException, ServerNotActiveException
	{
		logger.info("Started excuting checkServerActive");
		URL url = new URL(ReadExternalFiles.serverEndPoint);
		HttpURLConnection openConnection = (HttpURLConnection) url.openConnection();
		openConnection.connect();
		int rCode = openConnection.getResponseCode();
		if(!(rCode == 200))
		{
			logger.error("Server is not active, responding with code " + rCode + "");
			throw new ServerNotActiveException("Server is not active, responding with code " + rCode + "");
		}
		logger.info("Excuted checkServerActive method successfully");
	}
	
	private static void checkFilesExistance() throws FileNotFoundException
	{
		logger.info("Started excuting checkFilesExistance");
		String msg = "Required files in the test data folder are not existing :- ";
		boolean found = true;
		if(!new File(configPropertiesFile).exists()){
			msg = msg + ":CONFIG.properties ";
			found = false;
		}
		if(!new File(testdataWorkbookFile).exists()){
			msg = msg + ":TestData.xls ";
			found = false;
		}
		if(!new File(apiPathsPropertiesFile).exists()){
			msg = msg + ":API_Paths.properties ";
			found = false;
		}
		if(!new File(runModesPropertiesFile).exists()){
			msg = msg + ":RunMode.properties ";
			found = false;
		}
		if(!found){
			logger.error(msg);
			throw new FileNotFoundException(msg);
		}
		logger.info("Excuted checkFilesExistance method successfully");
	}
	
	private static void validateConfigFileContent() throws FileNotFoundException, IOException, PropertyException, AddressException
	{
		// Check all properties are defined.
		// Check set flags for all properly
		// Check endpoint url is poper
		// Validate mail ids
		logger.info("Started excuting validateConfigFileContent");
		String[] configReqProps = {"MailIds","SortReports","ServerEndPoint","SendToCC","CCMails", "SendMail"};
		List<String> props= ReadExternalFiles.getPropertiesFromFile(ReadExternalFiles.configPropertiesFile);
		boolean found = true;
		List<String> nonExistanceProps = new ArrayList<String>();
		for(int i=0; i<configReqProps.length; i++)
			if(!TestUtils.checkStringExistanceInList(props, configReqProps[i]))
			{
				found = false;
				nonExistanceProps.add(configReqProps[i]);
			}
		if(!found){
			logger.error("Non Existing properties in Config.properties file are : " + nonExistanceProps);
			throw new PropertyNotFoundException("Non Existing properties in Config.properties file are : " + nonExistanceProps);
		}
		
		if(!ReadExternalFiles.serverEndPoint.startsWith("http://"))
			if(!ReadExternalFiles.serverEndPoint.startsWith("https://")){
				logger.error("The config properties file having property ServerEndPoint is set with improper value, and the set value is - "
			+ ReadExternalFiles.serverEndPoint + "\n It should start with http/https");
				throw new PropertyException("The config properties file having property ServerEndPoint is set with improper value, and the set value is - "
			+ ReadExternalFiles.serverEndPoint + "\n It should start with http/https");
			}
		if(ReadExternalFiles.serverEndPoint.endsWith("/")){
			logger.error("The config properties file having property ServerendPoint should not end with '/', and the set value is - " 
		+ ReadExternalFiles.serverEndPoint);
				throw new PropertyException("The config properties file having property ServerendPoint should not end with '/', and the set value is - " 
		+ ReadExternalFiles.serverEndPoint);
		}
		
		boolean flagSet = true;
		LinkedHashMap<String, String> improperFlagProps = new LinkedHashMap<String, String>(); 
		String propVal = ReadExternalFiles.getConfigFileValue("SortReports");
		if(!(propVal.contains("Y") || propVal.contains("y")))
			if(!(propVal.contains("N") || propVal.contains("n")))
			{
				flagSet = false;
				improperFlagProps.put("SortReports", propVal);
			}
		propVal = ReadExternalFiles.getConfigFileValue("SendToCC");
		if(!(propVal.contains("Y") || propVal.contains("y")))
			if(!(propVal.contains("N") || propVal.contains("n")))
			{
				flagSet = false;
				improperFlagProps.put("SendToCC", propVal);
			}
		propVal = ReadExternalFiles.getConfigFileValue("SendMail");
		if(!(propVal.contains("Y") || propVal.contains("y")))
			if(!(propVal.contains("N") || propVal.contains("n")))
			{
				flagSet = false;
				improperFlagProps.put("SendMail", propVal);
			}
		if(!flagSet)
		{
			logger.error("Properties from config file are not set correctly, and they are : " + improperFlagProps);
			throw new PropertyException("Properties from config file are not set correctly, and they are : " + improperFlagProps);
		}
		
		String[] toMails = ReadExternalFiles.getConfigFileValue("MailIds").split(",");
		List<String> invalMails = new ArrayList<String>();
		for(int i=0;i<toMails.length; i++)
			if(!TestUtils.isValidMailAddress(toMails[i]))
				invalMails.add(toMails[i]);
		if(invalMails.size() > 0)
		{
			logger.error("To Mails list in config file are mentioned with invalid mail address, and are : " + invalMails);
			throw new AddressException("To Mails list in config file are mentioned with invalid mail address, and are : " + invalMails);
		}
		invalMails.clear();
		String[] ccMails = ReadExternalFiles.getConfigFileValue("CCMails").split(",");
		for(int i=0;i<ccMails.length; i++)
			if(!TestUtils.isValidMailAddress(ccMails[i]))
				invalMails.add(ccMails[i]);
		if(invalMails.size() > 0)
		{
			logger.error("CC Mails list in config file are mentioned with invalid mail address, and are : " + invalMails);
			throw new AddressException("CC Mails list in config file are mentioned with invalid mail address, and are : " + invalMails);
		}
		logger.info("Excuted validateConfigFileContent method successfully");
	}
	
	
	private static void validateRunmodeFile() throws FileNotFoundException, IOException, InvalidAttributeValueException
	{
		logger.info("Started excuting validateRunmodeFile");
		List<String> keys = ReadExternalFiles.getPropertiesFromFile(runModesPropertiesFile);
		String propVal = ""; boolean flagSet = true;
		LinkedHashMap<String, String> improperFlagProps = new LinkedHashMap<String, String>(); 
		for(String key : keys)
		{
			propVal = ReadExternalFiles.readProperty(runModesPropertiesFile, key);
			if(!(propVal.contains("Y") || propVal.contains("y")))
				if(!(propVal.contains("N") || propVal.contains("n")))
				{
					flagSet = false;
					improperFlagProps.put(key, propVal);
				}
		}
		if(!flagSet){
			logger.error("There are properties in Runmodes file neither defined with Yes, nor defined with No, and they are : " + improperFlagProps);
			throw new InvalidAttributeValueException("There are properties in Runmodes file neither defined with Yes, nor defined with No, and they are : " + improperFlagProps);
		}
		logger.info("Excuted validateRunmodeFile method successfully");
	}
	
	private static void validateAPIPaths() throws FileNotFoundException, IOException, InvalidAddress
	{
		logger.info("Started excuting validateAPIPaths");
		List<String> keys = ReadExternalFiles.getPropertiesFromFile(apiPathsPropertiesFile);
		String propVal = ""; boolean flagSet = true;
		LinkedHashMap<String, String> improperFlagProps = new LinkedHashMap<String, String>();
		for(String key : keys)
		{
			propVal = ReadExternalFiles.readProperty(apiPathsPropertiesFile, key);
			if(propVal.startsWith("/")){
				if(propVal.endsWith("/"))
				{
					flagSet = false;
					improperFlagProps.put(key, propVal);
				}
			}
			else{
				flagSet = false;
				improperFlagProps.put(key, propVal);
			}
		}
		if(!flagSet){
			logger.error("API paths should start with '/' and should not end with '/', but defined in API_Paths preoperties file are : " + improperFlagProps);
			throw new InvalidAddress("API paths should start with '/' and should not end with '/', but defined in API_Paths preoperties file are : " + improperFlagProps);
		}
		logger.info("Excuted validateAPIPaths method successfully");
	}
	
	//===========================================================================================================================
	
	
	
	@SuppressWarnings("unchecked")
	public static void validateInputData1(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateInputData1");
		validateDuplicateSteps(data, testDataSheetName, testDataTableName);
		validateApiComponents(data, testDataSheetName, testDataTableName);
		validateAPITypes(data, testDataSheetName, testDataTableName);
		validateAdditionalPath(data, testDataSheetName, testDataTableName);
		validateHeaders(data, testDataSheetName, testDataTableName);
		validateParameters(data, testDataSheetName, testDataTableName);
		validateExpHTTPStatusCode(data, testDataSheetName, testDataTableName);
		validateResponseType(data, testDataSheetName, testDataTableName);
		validateExpectedValues(data, testDataSheetName, testDataTableName);
		validateReusableParams(data, testDataSheetName, testDataTableName);
		validateNonExpectedValues(data, testDataSheetName, testDataTableName);
		validateJsonBody(data, testDataSheetName, testDataTableName);
		logger.info("Executed validateInputData1 method");
	}
	
	private static void validateDuplicateSteps(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateDuplicateSteps");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashSet<String> stepSet = new LinkedHashSet<String>();
		List<String> dupSteps = new ArrayList<String>();
		List<String> invalidSteps = new ArrayList<String>();
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			if(map.get("Step_No").startsWith("\n"))
				invalidSteps.add(map.get("Step_No"));
			if(stepSet.add(map.get("Step_No")) == false)
				dupSteps.add(map.get("Step_No"));
		}
		if(dupSteps.size() > 0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, "Given steps should be unique, duplicates found are : " + dupSteps);
			logger.error("Given steps should be unique, duplicates found are : " + dupSteps);
			throw new DataFormatException("Given steps should be unique, duplicates found are : " + dupSteps);
		}
		if(invalidSteps.size() > 0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, "Steps not properly set in the cells, and are : " + invalidSteps);
			logger.error("Steps not properly set in the cells, and are : " + invalidSteps);
			throw new DataFormatException("Steps not properly set in the cells, and are : " + invalidSteps);
		}
		logger.info("Executed validateDuplicateSteps method");
	}
	
	private static void validateApiComponents(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateApiComponents");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashSet<String> uniqApiCompsSet = new LinkedHashSet<String>();
		LinkedHashMap< String, String > invalidSteps = new LinkedHashMap<String, String>();
		List<String> blankComponentSteps = new ArrayList<String>();
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			uniqApiCompsSet.add(map.get("API_Component"));
			if(!(map.get("API_Component").length() > 0))
				blankComponentSteps.add(map.get("Step_No"));
			if(map.get("API_Component").startsWith("\n"))
				invalidSteps.put(map.get("Step_No"), map.get("API_Component"));
		}
		if(blankComponentSteps.size() > 0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, "API_Component is blank in the steps : " + blankComponentSteps);
			logger.error("API_Component is blank in the steps : " + blankComponentSteps);
			throw new DataFormatException("API_Component is blank in the steps : " + blankComponentSteps);
		}
		if(invalidSteps.size() > 0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, "API components not properly set in the cells, and are : " + invalidSteps);
			logger.error("API components not properly set in the cells, and are : " + invalidSteps);
			throw new DataFormatException("Steps not properly set in the cells, and are : " + invalidSteps);
		}
		List<String> allPaths = ReadExternalFiles.getPropertiesFromFile(apiPathsPropertiesFile);
		List<String> invalidComps = new ArrayList<String>();
		for (String s : uniqApiCompsSet)
			if(! TestUtils.checkStringExistanceInList(allPaths, s))
				invalidComps.add(s);
		
		 if(invalidComps.size() > 0)
		{
			 storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, "Mentioned API_Component's are not defined in the API_Paths properties file, and they are : " + invalidComps);
			logger.error("Mentioned API_Component's are not defined in the API_Paths properties file, and they are : " + invalidComps);
			throw new DataFormatException("Mentioned API_Component's are not defined in the API_Paths properties file, and they are : " + invalidComps);
		}
		 logger.info("Executed validateApiComponents method");
	}
	
	
	private static void validateAPITypes(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateAPITypes");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidSteps = new LinkedHashMap<String, String>();
		String[] listOfAPIStdtypes = {"GET", "POST", "PUT", "DELETE", "PATCH", "TRACE", "HEAD", "OPTIONS"};
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			boolean found = false;
			for(String str : listOfAPIStdtypes)
	            if(str.equalsIgnoreCase(map.get("API_Type")))
	            	found = true;
			if(!found)
				invalidSteps.put(map.get("Step_No"), map.get("API_Type"));
		}
		if(invalidSteps.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, "API_Type column is a mandatory , and is mentioned invalid. It should be within std API methods (GET/POST/PUT/DELETE/PATCH/TRACE/HEAD/OPTIONS): \nInvaid API types mentioned are : " + invalidSteps);
			logger.error("API_Type column is a mandatory , and is mentioned invalid. It should be within std API methods (GET/POST/PUT/DELETE/PATCH/TRACE/HEAD/OPTIONS): \nInvaid API types mentioned are : " + invalidSteps);
			throw new DataFormatException("API_Type column is a mandatory , and is mentioned invalid. It should be within std API methods (GET/POST/PUT/DELETE/PATCH/TRACE/HEAD/OPTIONS): \nInvaid API types mentioned are : " + invalidSteps);
		}
		logger.info("Executed validateAPITypes method");
	}
	
	
	private static void validateAdditionalPath(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateAdditionalPath");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidPaths = new LinkedHashMap<String, String>();
		String propVal = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			propVal = map.get("Additional_Path");
			if(propVal.length()>0){
				if(propVal.startsWith("/")){
					if(propVal.endsWith("/"))
						invalidPaths.put(map.get("Step_No"), propVal);
				}
				else
					invalidPaths.put(map.get("Step_No"), propVal);
			}
			propVal = "";
		}
		if(invalidPaths.size() > 0){
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName, "API paths should start with '/' and should not end with '/', but defined in API_Paths preoperties file are : " + invalidPaths);
			logger.error("API paths should start with '/' and should not end with '/', but defined in API_Paths preoperties file are : " + invalidPaths);
			throw new DataFormatException("API paths should start with '/' and should not end with '/', but defined in API_Paths preoperties file are : " + invalidPaths);
		}
		logger.info("Executed validateAdditionalPath method");
	}
	
	
	private static void validateHeaders(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateHeaders");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidHaders = new LinkedHashMap<String, String>();
		String header = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			header = map.get("Request_Headers");
			if(!(header.startsWith("\n")))
			{
				String[] allParams = header.split("\n");
				for(int j = 0; j<allParams.length; j++)
				{
					if(allParams[j].length()>0){
						if(!(allParams[j].split("=").length >= 2))
							invalidHaders.put(map.get("Step_No"), header);
						else
							if((!(allParams[j].split("=")[0].length()>0)) || (!(allParams[j].split("=")[0].length()>0)))
								invalidHaders.put(map.get("Step_No"), header);
					}
				}
			}
			else
				invalidHaders.put(map.get("Step_No"), header);
			header = "";
		}
		if(invalidHaders.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Contained invalid headers format and are : " + invalidHaders);
			logger.error("Contained invalid headers format and are : " + invalidHaders);
			throw new DataFormatException("Contained invalid headers format and are : " + invalidHaders);
		}
		logger.info("Executed validateHeaders method");
	}
	
	private static void validateParameters(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateParameters");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidParams = new LinkedHashMap<String, String>();
		String param = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			param = map.get("Request_Parameters");
			if(!(param.startsWith("\n"))){
				String[] allParams = param.split("\n");
				for(int j = 0; j<allParams.length; j++)
					if(allParams[j].length()>0){
						if(!(allParams[j].split("=").length == 2))
							invalidParams.put(map.get("Step_No"), param);
						else
							if((!(allParams[j].split("=")[0].length()>0)) || (!(allParams[j].split("=")[0].length()>0)))
								invalidParams.put(map.get("Step_No"), param);
					}
			}
			else
				invalidParams.put(map.get("Step_No"), param);
			param = "";
		}
		if(invalidParams.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Contained invalid Request_Parameters format and are : " + invalidParams);
			logger.error("Contained invalid Request_Parameters format and are : " + invalidParams);
			throw new DataFormatException("Contained invalid Request_Parameters format and are : " + invalidParams);
		}
		logger.info("Executed validateParameters method");
	}
	
	private static void validateExpHTTPStatusCode(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateExpHTTPStatusCode");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidSteps = new LinkedHashMap<String, String>();
		int expHttpcode = 0;
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			if(map.get("Expected_HTTP_Code").length()>0){
				try{
					expHttpcode = Integer.parseInt(map.get("Expected_HTTP_Code").trim());
					if(expHttpcode<100 || expHttpcode > 510)
						invalidSteps.put(map.get("Step_No"), map.get("Expected_HTTP_Code"));
				}catch(NumberFormatException nfe)
				{invalidSteps.put(map.get("Step_No"), map.get("Expected_HTTP_Code"));}
			}
			expHttpcode = 0;
		}
		if(invalidSteps.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Expected HTTP codes are with invalid format/content, and they are " + invalidSteps);
			logger.error("Expected HTTP codes are with invalid format/content, and they are " + invalidSteps);
			throw new DataFormatException("Expected HTTP codes are with invalid format/content, and they are " + invalidSteps);
		}
		logger.info("Executed validateExpHTTPStatusCode method");
	}
	
	
	
	private static void validateResponseType(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateResponseType");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidSteps = new LinkedHashMap<String, String>();
		String temp = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			temp = map.get("Response_Type");
			if(temp.length()>0)
			{
				if(!(temp.equalsIgnoreCase("Text")))
					invalidSteps.put(map.get("Step_No"), temp);
			}
			temp="";
		}
		if(invalidSteps.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Response Type with Text/Json is handled, Invalid type mentioned are : " + invalidSteps);
			logger.error("Response Type with Text/Json is handled, Invalid type mentioned are : " + invalidSteps);
			throw new DataFormatException("Response Type with Text/Json is handled, Invalid type mentioned are : " + invalidSteps);
		}
		logger.info("Executed validateResponseType method");
	}
	
	
	
	private static void validateExpectedValues(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateExpectedValues");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidParams = new LinkedHashMap<String, String>();
		String param = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			param = map.get("Expected_Values");
			if(!(param.startsWith("\n"))){
				String[] allParams = param.split("\n");
				for(int j = 0; j<allParams.length; j++)
				{
					if(allParams[j].length()>0){
						if(!(allParams[j].split("=").length >= 2))
							invalidParams.put(map.get("Step_No"), param);
						else
							if((!(allParams[j].split("=")[0].length()>0)) || (!(allParams[j].split("=")[0].length()>0)))
								invalidParams.put(map.get("Step_No"), param);
						if(!(allParams[j].startsWith("$.")))
							invalidParams.put(map.get("Step_No"), param);
					}
				}
			}
			else
				invalidParams.put(map.get("Step_No"), param);
			param = "";
		}
		if(invalidParams.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Contained Expected values with invalid format and are : " + invalidParams);
			logger.error("Contained Expected values with invalid format and are : " + invalidParams);
			throw new DataFormatException("Contained Expected values with invalid format and are : " + invalidParams);
		}
		logger.info("Executed validateExpectedValues method");
	}
	
	
	
	private static void validateReusableParams(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateReusableParams");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidParams = new LinkedHashMap<String, String>();
		String param = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			param = map.get("Reusable_Parameters");
			if(!(param.startsWith("\n"))){
				String[] allParams = param.split("\n");
				for(int j = 0; j<allParams.length; j++)
					if(allParams[j].length()>0){
						if(!(allParams[j].split("=").length == 2))
							invalidParams.put(map.get("Step_No"), param);
						else
							if((!(allParams[j].split("=")[0].length()>0)) || (!(allParams[j].split("=")[0].length()>0)))
								invalidParams.put(map.get("Step_No"), param);
					}
			}
			else
				invalidParams.put(map.get("Step_No"), param);
			param = "";
		}
		if(invalidParams.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Contained Reusable parameters with invalid format and are : " + invalidParams);
			logger.error("Contained Reusable parameters with invalid format and are : " + invalidParams);
			throw new DataFormatException("Contained Reusable parameters with invalid format and are : " + invalidParams);
		}
		logger.info("Executed validateReusableParams method");
	}
	
	
	private static void validateNonExpectedValues(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateNonExpectedValues");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidParams = new LinkedHashMap<String, String>();
		String param = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			param = map.get("NonExpected_Values");
			if(!(param.startsWith("\n"))){
				String[] allParams = param.split("\n");
				for(int j = 0; j<allParams.length; j++)
				{
					if(allParams[j].length()>0){
						if(!(allParams[j].split("=").length >= 2))
							invalidParams.put(map.get("Step_No"), param);
						else
							if((!(allParams[j].split("=")[0].length()>0)) || (!(allParams[j].split("=")[0].length()>0)))
								invalidParams.put(map.get("Step_No"), param);
						if(!(allParams[j].startsWith("$.")))
							invalidParams.put(map.get("Step_No"), param);
					}
				}
			}
			else
				invalidParams.put(map.get("Step_No"), param);
			param = "";
		}
		if(invalidParams.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Contained NonExpected_Values with invalid format and are : " + invalidParams);
			logger.error("Contained NonExpected_Values with invalid format and are : " + invalidParams);
			throw new DataFormatException("Contained NonExpected_Values with invalid format and are : " + invalidParams);
		}
		logger.info("Executed validateNonExpectedValues method");
	}
	
	
	private static boolean doCheckStepExp(String expressionText)
	{
		String stepRegEx = "^.*#Step:[a-zA-Z0-9_]*#.*$";
		return TestUtils.matchStringWithRegEx(expressionText, stepRegEx);
	}
	
	
	private static boolean doCheckMethodExp(String expressionText)
	{
		boolean matched = false;
		if(TestUtils.matchStringWithRegEx(expressionText, "^.*#Method:.*[a-zA-Z0-9_](.*)+.*=[a-zA-Z0-9_]+#.*$"))
			matched = true;
		else if(TestUtils.matchStringWithRegEx(expressionText, "^.*#Method:.*[a-zA-Z0-9_](.*)+.*#.*$"))
			matched = true;
		return matched;
	}
	
	
	
	private static void validateJsonBody(Object[][] data, String testDataSheetName, String testDataTableName) throws Exception
	{
		logger.info("Started executing validateJsonBody");
		LinkedHashMap< String, String > map = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > invalidExpressions = new LinkedHashMap<String, String>();
		LinkedHashMap< String, String > defaultSetSteps = new LinkedHashMap<String, String>();
		String param = "", storeLine = "", jsonDefaultValuesBody = "";
		for(int i =0; i<data.length; i++)
		{
			map = (LinkedHashMap<String, String>) data[i][0];
			param = map.get("Request_JsonBody");
			if((param.length()>1) && (!(param.contains(":IP_"))))
			{
				String[] jsonLines = param.split("\n");
				for(String line : jsonLines)
				{
					storeLine = line.trim();
					if(line.contains("Method:")){
						if(!(doCheckMethodExp(line)))
							invalidExpressions.put(map.get("Step_No"), line);
						else
							storeLine = replaceExpLineWithdefaultValue(storeLine);
					}
					else if(line.contains("Step:"))
					{
						if(!(doCheckStepExp(line)))
							invalidExpressions.put(map.get("Step_No"), line);
						else
							storeLine = replaceExpLineWithdefaultValue(storeLine);
					}
					jsonDefaultValuesBody = jsonDefaultValuesBody + storeLine+"\n";
				}
				defaultSetSteps.put(map.get("Step_No"), jsonDefaultValuesBody);
			}
			jsonDefaultValuesBody = "";
		}
		if(invalidExpressions.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Request Json body contained with invalid expressions, and are : " + invalidExpressions);
			logger.error("Request Json body contained with invalid expressions, and are : " + invalidExpressions);
			throw new DataFormatException("Request Json body contained with invalid expressions, and are : " + invalidExpressions);
		}
		List<String> invalidSteps = new ArrayList<String>();
		for(String steps : defaultSetSteps.keySet())
		{
			if(!(TestUtils.isJSONValid(defaultSetSteps.get(steps))))
				invalidSteps.add(steps);
		}
		if(invalidSteps.size()>0)
		{
			storeResultsIntoExcelFiles(testDataSheetName, testDataTableName,"Request Json body is with invalid Json format, and their steps are : " + invalidSteps);
			logger.error("Request Json body is with invalid Json format, and their steps are : " + invalidSteps);
			throw new DataFormatException("Request Json body is with invalid Json format, and their steps are : " + invalidSteps);
		}
		logger.info("Executed validateJsonBody method");
	}
	
	private static String replaceExpLineWithdefaultValue(String expLine)
	{
		int sIndex = 0;
		int eIndex = 0;
		String exp = "";
		if(expLine.contains("#Step:"))
		{
			sIndex = expLine.indexOf("#Step:");
			eIndex = expLine.indexOf("#", sIndex+1);
			exp = expLine.substring(sIndex, eIndex);
			expLine = expLine.replace(exp, "0");
		}
		else if(expLine.contains("#Method:"))
		{
			sIndex = expLine.indexOf("#Method:");
			eIndex = expLine.indexOf("#", sIndex+1);
			exp = expLine.substring(sIndex, eIndex);
			expLine = expLine.replace(exp, "0");
		}
		return expLine;
	}
	
	
	
	
	private static void storeResultsIntoExcelFiles(String testDataSheetName, String testDataTableName, String failureMsg) throws Exception
	{
		String[] summaryValues = {testDataTableName, "","", "","", "","", "","", "","", "","","Skipped_TD_error",failureMsg, "",""};
		String[] values = {testDataTableName,"","Skipped_TD_error",failureMsg, ""};
		ReadExternalFiles.setResultsInOutputExcel(API_Header.resultSummaryPath, testDataSheetName, values);
		ReadExternalFiles.setResultsInOutputExcel(API_Header.outputExcelPath, testDataSheetName, summaryValues);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
