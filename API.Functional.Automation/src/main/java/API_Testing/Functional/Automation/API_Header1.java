package API_Testing.Functional.Automation;

import static com.jayway.restassured.RestAssured.given;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.mail.internet.AddressException;
import javax.management.InvalidAttributeValueException;
import javax.xml.bind.PropertyException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.eventusermodel.HSSFUserException;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.jayway.restassured.builder.ResponseBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class API_Header1 {
	
	public static ExtentReports extent ;
	public static ExtentTest test ;
	public static LinkedHashMap<String, ExtentTest> testsMap = new LinkedHashMap<String, ExtentTest>();
	
	
	public static Logger logger  = null;
	public static String failureMsg = "";
	
	public static LinkedHashMap<String,String> params_reused = new LinkedHashMap<String,String>();
	public static LinkedHashMap<String,String> testData = new LinkedHashMap<String, String>();
	public static Map<String, Object> outputfileMap = new LinkedHashMap<String, Object>();
	public static Map<String, Object> summaryResultFileMap = new LinkedHashMap<String, Object>();
	public static Map<String, String> ipJsonMap = new LinkedHashMap<String, String>();
	public static Map<String, String> testStepResultMap = new LinkedHashMap<String, String>();
	
	public static String projectDirectory = System.getProperty("user.dir");
	public static String executionStartTime = Date_Time.getCurrentDate()+"_"+Date_Time.getCurrentTime();
	public static String resultDataFileName = "ResultData "+executionStartTime.replace(":", "-")+".xls";
	public static String resultSumaryFileName = "ResultSummary "+executionStartTime.replace(":", "-")+".xls";
	public static String outputExcelPath = System.getProperty("user.dir")+"\\Test_Result\\"+resultDataFileName;
	public static String resultSummaryPath = System.getProperty("user.dir")+"\\Test_Result\\"+resultSumaryFileName;
	
	public static String jsonBodyTextfilePath = System.getProperty("user.dir")+"\\Test_Result\\JsonResponses\\";
	public static String jsonBodyTextfilePathAndName = "";
	
	public static String jsonDoubleQuote = "\"";
	
	static{
		logger  = Logger.getLogger(API_Header.class);
		PropertyConfigurator.configure("log4j.properties");
		
		extent = new ExtentReports(System.getProperty("user.dir")+"/CustomReport.html",true);
		extent.addSystemInfo("Name","Manju")
		.addSystemInfo("Test","API")
		.addSystemInfo("Env","Dev");
		extent.loadConfig(new File(System.getProperty("user.dir")+"/extent-config.xml"));
	}
	
	@BeforeSuite
	public static void preExecution() throws AddressException, InvalidAttributeValueException, PropertyException, InvalidAddress, IOException, HSSFUserException, ServerNotActiveException
	{
		DataFilesValidator.doPreSuiteValidations();
		setupTestResultDirectory();
	}
	
	
	private static void setupTestResultDirectory() throws IOException
	{
		SystemDirectoryUtil.createDir(projectDirectory+"\\Test_Result");
		FileUtils.cleanDirectory(new File(projectDirectory+"\\Test_Result"));
		FileUtils.copyFileToDirectory(new File(projectDirectory+"\\TestData\\TestData.xls"), new File(projectDirectory+"\\Test_Result\\"));
		
		List<String> headerRow = new ArrayList<String>();
		List<String> summaryHeaderRow = new ArrayList<String>();
		headerRow.add("TestCase Name");summaryHeaderRow.add("TestCase Name");
		headerRow.add("Step_No");summaryHeaderRow.add("Step_No");
		headerRow.add("API_Component");
		headerRow.add("Description");
		headerRow.add("Request_Link");
		headerRow.add("API_Type");
		headerRow.add("Request_Headers");
		headerRow.add("Request_Parameters");
		headerRow.add("Request_JsonBody");
		headerRow.add("Expected_HTTP_Code");
		headerRow.add("Expected_Values");
		headerRow.add("NonExpected_Values");
		headerRow.add("Actual_Response_Body");
		headerRow.add("Test_Status");summaryHeaderRow.add("Test_Status");
		headerRow.add("Failure_Message");summaryHeaderRow.add("Failure_Message");
		headerRow.add("Reusable_Parameters");
		headerRow.add("Execution_Duration");summaryHeaderRow.add("Execution_Duration");
		
		for(String i : headerRow)
			outputfileMap.put(i, "");
		for(String i : summaryHeaderRow)
			summaryResultFileMap.put(i, "");
		
		logger.info("outputfileMap after adding data is : " + outputfileMap);
		
		List<String> totalSheetsList = ReadExternalFiles.readPropertiesWithActiveRunmode();
		
		try {
			CreateExcelFile.createExcelFileWithHeaders(outputExcelPath, headerRow, totalSheetsList);
			CreateExcelFile.createExcelFileWithHeaders(resultSummaryPath, summaryHeaderRow, totalSheetsList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SystemDirectoryUtil.createDir(projectDirectory+"\\Test_Result\\JsonResponses");
		ReadExternalFiles.setPropertyIntoConfigFile("LastExecutionTime", executionStartTime.replace(":", "-"));
	}
	
	private static String getReusableParam(String key)
	{
		boolean found = false;
		for(String actKey : params_reused.keySet())
		{
			if(actKey.equals(key)){
				found = true; break;}
		}
		if(found)
			return  params_reused.get(key);
		else{
			failureMsg = "Given key '" + key + "' from expression is not present in Reusable Params list\n";
			return "null";
		}
	}
	private static String getAdditionalPath(String definedAddionalPath)
			throws Exception
	{
		String path = "";
		String lastPath = "", pathArrValue="";
		
		if((definedAddionalPath.length()>1) && (definedAddionalPath.contains("#Step:") || definedAddionalPath.contains("#Method:")))
		{
			String[] pathArr = definedAddionalPath.split("/");
			
			logger.info("String[] pathArr  0 is : "  + pathArr[0]);
			logger.info("String[] pathArr  1 is : "  + pathArr[1]);
			String pValue = "";
			
			int index=0, lastIndex = 0;String subString = "";
			for(int i=1; i<pathArr.length; i++)
			{
				pathArrValue = pathArr[i];
				if(pathArrValue.contains("?")){
					pathArrValue = pathArr[i].substring(0, pathArrValue.indexOf("?"));
					logger.info("pathArrValue when contains ? is : " + pathArrValue);
					lastPath = pathArr[i].substring(pathArr[i].indexOf("?"));
					lastPath = lastPath.replace("\n", "");
				}
				if(pathArrValue.startsWith("#Step:"))
				{
					pValue = pathArrValue.replace("#Step:", "").replace("#", "");
					//path = path + "/" + params_reused.get(pValue);
					path = path + "/" + getReusableParam(pValue);
				}
				else if(pathArrValue.startsWith("#Method:"))
				{
					path = path + "/" + //getValueUsingReflectionMethods(data, pathArr[i], params_reused);
							getValueFromMethodExpressions_WithNested_Variables_Concates(pathArr[i]);
				}
				else
					path = path+"/"+pathArrValue;
			}
			
			
			while(lastPath.contains("#Step:") || lastPath.contains("#Method:"))
			{
				if(lastPath.contains("#Step:")){
					index = lastPath.indexOf("#Step:");
					lastIndex= lastPath.indexOf("#", index+6);
					subString = lastPath.substring(index+6, lastIndex);
					
					logger.info("retrieved substring from lastpath is: " + subString);
					
					lastPath = lastPath.replace("#Step:"+ subString+"#", params_reused.get(subString));
				}
				if(lastPath.contains("#Method:")){
					index = lastPath.indexOf("#Method:") + 8;
					lastIndex= lastPath.indexOf("#", index);
					subString = lastPath.substring(index, lastIndex);
					lastPath = lastPath.replace("#Method:"+subString+"#", getValueFromMethodExpressions_WithNested_Variables_Concates(subString));
				}
			}
			return path+lastPath;
		}
		else
			return definedAddionalPath;
	}
	
	
	private static String getJsonBodyWithReusableParameters() throws Exception
	{
		String fLine = ""; String subString= "", reusableVal="";
		String jsonBody = testData.get("Request_JsonBody");
		if((jsonBody.length() > 2))
		{
			if(jsonBody.contains("IP_"))
				jsonBody = ipJsonMap.get(jsonBody.split(":")[0].trim() + ":" + jsonBody.split(":")[1].trim());
			
			String[] jsonBodyArr = jsonBody.split("\n");
			int i=0;
			while(i<jsonBodyArr.length)
			{
				int index = 0, lastIndex = 0;
				fLine = jsonBodyArr[i];
					if(fLine.contains("#Step:"))
					{
						index = fLine.indexOf("#Step:") + 6;
						lastIndex= fLine.indexOf("#", index);
						subString = fLine.substring(index, lastIndex);
						reusableVal = getReusableParam(subString);
						
						fLine = fLine.replace("#Step:"+subString+"#", reusableVal);
						jsonBody = jsonBody.replace("#Step:"+subString+"#", reusableVal);
					}
					
					if(fLine.contains("#Method:"))
					{
						index = fLine.indexOf("#Method:") + 8;
						lastIndex= fLine.indexOf("#", index);
						subString = fLine.substring(index, lastIndex);
						logger.info("subString of method is : " + subString);
						reusableVal = getValueFromMethodExpressions_WithNested_Variables_Concates(subString);
						logger.info("getValueUsingReflectionMethods is : " + reusableVal);
						fLine = fLine.replace("#Method:"+subString+"#", reusableVal);
						jsonBody = jsonBody.replace("#Method:"+subString+"#", reusableVal);
					}
					
					if(fLine.contains("#Step:") || fLine.contains("#Method:"))
						i=i-1;
				
				i++;
			}
		}
		
		return jsonBody;
	}
	
	private static String getValueFromMethodExpressions_WithNested_Variables_Concates(String methodDefinition)
			throws Exception
	{
		
		String value = "", variableToStore="";
		String concatinationString = ""; boolean isConStringFirst = false;
		String mArr0 = "", mArr1="";int index = 0;
		if(methodDefinition.contains("="))
		{
			String[] methodDefinitionArr = methodDefinition.split("=");
			methodDefinition = methodDefinitionArr[0];
			variableToStore = methodDefinitionArr[1];
		}
		methodDefinition = methodDefinition.replace("#Method:", "");
		if(methodDefinition.endsWith("#"))
			methodDefinition = methodDefinition.replace("#", "");
		
		if(!methodDefinition.contains("+"))
			value = getValueFromMethodExpressionWithNested(methodDefinition);
		else
		{
			index = methodDefinition.indexOf("+");
			mArr0 = methodDefinition.substring(0, index);
			mArr1 = methodDefinition.substring(index+1);
			logger.info(" mArr0 is : " + mArr0);logger.info(" mArr1 is : " + mArr1);
			if(mArr0.contains("(") && mArr0.endsWith(")")){
				concatinationString = mArr1;methodDefinition=mArr0;}
			else{methodDefinition=mArr1;
				concatinationString = mArr0;isConStringFirst=true;}
			logger.info("methodDefinition before splitting with ( is : " + methodDefinition);
			value = getValueFromMethodExpressionWithNested(methodDefinition);
		}
		logger.info("Found evaluated value along with nested methods : " + value );
		if(isConStringFirst)
			value = concatinationString + value;
		else
			value = value + concatinationString;
		logger.info("variableToStore is :: " + variableToStore);
		if(variableToStore.length() > 0)
			params_reused.put(testData.get("Step_No")+"_"+variableToStore, value);
		
		return value;
	}
	private static String getValueFromMethodExpressionWithNested(String methodExpression) throws Exception
	{
		int index = methodExpression.indexOf("(");
		String methodName = methodExpression.substring(0, index);
		String methodArgument = methodExpression.substring(index+1).replace(")", "").replace(")", "");
		if(methodArgument.startsWith("Step:"))
			methodArgument = getValueFromParamReusedMapUsingStepExpression(methodArgument);
		else if(methodArgument.contains("("))
		{
			index = methodArgument.indexOf("(");
			methodArgument = getValueFromReflectionMethod(methodArgument.substring(0, index).trim(), methodArgument.substring(index+1).trim());
		}
		return getValueFromReflectionMethod(methodName, methodArgument);
	}
	
	
	public static Response getResponseOfAPI() throws Exception{
		
		Map<String, String> reqHeadersMap = getRequestHeadersInMap();
		Map<String, String> reqParametersMap = getRequestParametersInMap();
		logger.info("reqParametersMap isss : " + reqParametersMap) ;
		outputfileMap.put("Request_Parameters", reqParametersMap);
		
		Map<String, String> reqParametersMap1 = getRequestParamsWithReusableValues(reqParametersMap);
		logger.info("getRequestParamsWithReusableValues are :  " + reqParametersMap1);
		
		String reqLink = ReadExternalFiles.getAPI_URL(testData.get("API_Component"));
		if(reqLink.endsWith("null"))
		{
			failureMsg = "Given Api component is not defined with path in the Api_Path.properties file\n";
			logger.info("Given Api component is not defined with path in the Api_Path.properties file\n");
			throw new NullPointerException("Given Api component is not defined with path in the Api_Path.properties file\n");
		}
		String requestLink = reqLink + getAdditionalPath(testData.get("Additional_Path"));
		outputfileMap.put("Request_Link", requestLink);
		
		String jsobBodyWithDynamicValues = getJsonBodyWithReusableParameters().trim();
		Response response = getServerResponse(reqHeadersMap, reqParametersMap1, requestLink, jsobBodyWithDynamicValues);
		outputfileMap.put("Request_JsonBody", jsobBodyWithDynamicValues);
		return response;
	}
	private static Map<String, String> getRequestParamsWithReusableValues(Map<String, String> reqParametersMap
			) throws Exception
	{
		String value = "";
		for(String key: reqParametersMap.keySet())
		{
			value = reqParametersMap.get(key);
			if(value.startsWith("#Step:"))
			{
				value = getValueFromParamReusedMapUsingStepExpression(value);
				logger.info("Retrieved value with new method is : " + value);
				reqParametersMap.put(key, value);
			}
			else if(value.startsWith("#Method:"))
			{
				logger.info("Value which is passing to method is : " + value);
				value = //getValueUsingReflectionMethods(data, value, params_reused);
						getValueFromMethodExpressions_WithNested_Variables_Concates(value);
				reqParametersMap.put(key, value);
				logger.info("params_reused after appending is :   " + params_reused);
			}
		}
		return reqParametersMap;
	}
	private static Map<String, String> getRequestHeadersInMap()
	{
		String[] reqHeadersArr = testData.get("Request_Headers").split("\n");
		Map<String, String> reqHeadersMap = new LinkedHashMap<String, String>();
		String headerName="", headerValue="";
		
		if(testData.get("Request_Headers").length() > 2)
		{
			for(int i=0; i<reqHeadersArr.length; i++){
				headerName = reqHeadersArr[i].split("=")[0];
				headerValue = reqHeadersArr[i].replace(headerName, "").trim();
				headerValue = headerValue.substring(1);
				reqHeadersMap.put(headerName.trim(), headerValue.trim());
			}
		}
		return reqHeadersMap;
	}
	private static Map<String, String> getRequestParametersInMap()
	{
		String[] reqParametersArr = testData.get("Request_Parameters").split("\n");
		Map<String, String> reqParametersMap = new LinkedHashMap<String, String>();
		String paramName="", paramValue="";
		if(testData.get("Request_Parameters").length() > 2)
		{
			for(int i=0; i<reqParametersArr.length; i++){
				paramName = reqParametersArr[i].split("=")[0];
				//paramValue = reqParametersArr[i].replace(paramName, "").trim();
				paramValue = reqParametersArr[i].replaceFirst(paramName, "").trim();
				paramValue = paramValue.substring(1);
				reqParametersMap.put(paramName.trim(), paramValue.trim());
			}
		}
		return reqParametersMap;
	}
	private static Response getServerResponse(Map<String, String> reqHeadersMap, 
			Map<String, String> reqParametersMap, String requestLink, String jsonBodyWithDynamicValues)
	{
		// Use UnknownHostException for catch, and make a failure msg whenever server goes down, helps to get a message prior 
		RequestSpecification reqSpec = given().headers(reqHeadersMap).parameters(reqParametersMap).body(jsonBodyWithDynamicValues);
		Response response = null;
		/*if(data.get("API_Type").trim().equalsIgnoreCase("GET"))
		{
			response = 
			given().headers(reqHeadersMap).parameters(reqParametersMap).when().
			get(requestLink).
			andReturn();
		}
		else if(data.get("API_Type").trim().equalsIgnoreCase("POST"))
		{
			response = 
			given().headers(reqHeadersMap).parameters(reqParametersMap).body(jsonBodyWithDynamicValues).
			when().post(requestLink).andReturn();
		}
		else if(data.get("API_Type").trim().equalsIgnoreCase("PUT"))
		{
			response = 
			given().headers(reqHeadersMap).parameters(reqParametersMap).body(jsonBodyWithDynamicValues).
			when().put(requestLink).andReturn();
		}
		else if(data.get("API_Type").trim().equalsIgnoreCase("PATCH"))
		{
			response = 
			given().headers(reqHeadersMap).parameters(reqParametersMap).body(jsonBodyWithDynamicValues).
			when().patch(requestLink).andReturn();
		}
		else if(data.get("API_Type").trim().equalsIgnoreCase("DELETE"))
		{
			response = 
			given().headers(reqHeadersMap).parameters(reqParametersMap).body(jsonBodyWithDynamicValues).
			when().delete(requestLink).andReturn();
		}*/
		
		long cMilliSeconds = System.currentTimeMillis();
		if(testData.get("API_Type").trim().equalsIgnoreCase("GET"))
			response =  reqSpec.when().get(requestLink).andReturn();
		else if(testData.get("API_Type").trim().equalsIgnoreCase("POST"))
			response =  reqSpec.when().post(requestLink).andReturn();
		else if(testData.get("API_Type").trim().equalsIgnoreCase("PUT"))
			response =  reqSpec.when().put(requestLink).andReturn();
		else if(testData.get("API_Type").trim().equalsIgnoreCase("PATCH"))
			response =  reqSpec.when().patch(requestLink).andReturn();
		else if(testData.get("API_Type").trim().equalsIgnoreCase("DELETE"))
			response =  reqSpec.when().delete(requestLink).andReturn();
		cMilliSeconds = System.currentTimeMillis() - cMilliSeconds;
		outputfileMap.put("Execution_Duration", cMilliSeconds+"");
		summaryResultFileMap.put("Execution_Duration", cMilliSeconds+"");
		return response;
	}
	private static String getValueFromParamReusedMapUsingStepExpression(String stepExpression) throws Exception
	{
		if(stepExpression.startsWith("#Step:"))
			stepExpression = stepExpression.replace("#Step:", "").trim();
		else if(stepExpression.startsWith("Step:"))
			stepExpression = stepExpression.replace("Step:", "").trim();
		else
			throw new Exception("stepExpression passed in the getValueFromReusableMapUsingStepExpression method is invalid, it is not starting with Step: , and passed expression is - " + stepExpression);
		//return params_reused.get(stepExpression.replace("#", "").trim());
		return getReusableParam(stepExpression.replace("#", "").trim());
	}
	private static String getValueFromReflectionMethod(String methodName, String methodArgument) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		String methodDefinition = "Method body";
		logger.info("getValueFromReflectionMethod mname is : " + methodName);
		logger.info("getValueFromReflectionMethod methodArgument is : " + methodArgument);
		Object ret = null;
		ReflectionMethods refK = new ReflectionMethods();
		
		try{
		if(methodArgument.length() >=1)
		{
			Class<?>[] paramTypes = {String.class};
			Method m = refK.getClass().getMethod(methodName, paramTypes);
			ret = m.invoke(methodDefinition, methodArgument);
		}
		else {
			Method m = refK.getClass().getMethod(methodName, null);
			ret = String.valueOf(m.invoke(refK, null));
		}
		}catch(NoSuchMethodException e){
			//System.out.println("There is no method available reflections methods with the name - > " + methodName);
			logger.info("There is no reflection method available with the name - > " + methodName);
			failureMsg = "There is no reflection method available with the name - > " + methodName;
			throw new NoSuchMethodException("There is no reflection method available with the name - > " + methodName+"\n"+e.getMessage());
		}catch(SecurityException e){
			//System.out.println("Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class");
			logger.info("Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class");
			failureMsg = "Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class";
			throw new SecurityException("Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class");
		}catch(IllegalAccessException e){
			//System.out.println("Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class");
			logger.info("Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class");
			failureMsg = "Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class";
			throw new IllegalAccessException("Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class");
		}catch(IllegalArgumentException e){
			//System.out.println("Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument);
			logger.info("Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument);
			failureMsg = "Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument;
			throw new IllegalAccessException("Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument);
		}catch(InvocationTargetException e){
			//System.out.println("Passed reflection method name- '" + methodName + "' with argument '"+ methodArgument +"' is giving an InvocationTargetException, check passed method and arguments, and method implemenaion in Reflection class");
			logger.info("Passed reflection method name- '" + methodName + "' with argument '"+ methodArgument +"' is giving an InvocationTargetException, check passed method and arguments, and method implemenaion in Reflection class");
			failureMsg = "Passed reflection method name- '" + methodName + "' with argument '"+ methodArgument +"' is giving an InvocationTargetException, check passed method and arguments, and method implemenaion in Reflection class";
			throw new InvocationTargetException(e, "Passed reflection method name- '" + methodName + "' with argument '"+ methodArgument +"' is giving an InvocationTargetException, check passed method and arguments, and method implemenaion in Reflection class");
		}
		return ret.toString();
	}
	
	
	
	
	
	
	
	/*
	public static Map<String, String> getExpectedValues(LinkedHashMap<String,String> data) throws Exception
	{
		String expectedValues = data.get("Expected_Values").trim();
		
		Map<String, String> expectedDataMap = new LinkedHashMap<String, String>();
		
		//if(expReturnType.equalsIgnoreCase("text"))
		//	expectedDataMap.put("text", expectedValues);
		//else
		//{
			String[] expParams = expectedValues.split("#>>");
			String expectedVal = "";
			for(int i=0; i<expParams.length; i++)
			{
				expectedVal = expParams[i];
				
				if(expectedVal.length()>0)
				{
					if(! expectedVal.contains("="))
						throw new Exception("Expected_Values are not defined appropriately. Kindly check again.");
					else
						expectedDataMap.put(expectedVal.substring(0, expectedVal.indexOf("=")).trim(), expectedVal.substring(expectedVal.indexOf("=")+1).trim());
				}
			}
		//}
		
		return expectedDataMap;
	}*/
	
	
	
	public static Map<String, String> getNonExpectedValues1() throws Exception
	{
		String expectedValues = testData.get("NonExpected_Values").trim();
		Map<String, String> expectedDataMap = new LinkedHashMap<String, String>();
		String[] expParams = expectedValues.split("\n");
		String expectedVal = ""; int startIndex = 0;
		
		for(int i=0; i<expParams.length; i++)
		{
			expectedVal = expParams[i];
			if(expectedVal.length()>0)
			{
				if(expectedVal.startsWith("$"))
					startIndex = 2;
				if(! expectedVal.contains("="))
					throw new Exception("Expected_Values are not defined appropriately. Kindly check again.");
				else
					expectedDataMap.put(expectedVal.substring(startIndex, expectedVal.indexOf("=")).trim(), expectedVal.substring(expectedVal.indexOf("=")+1).trim());
			}
			startIndex = 0;
		}
		return expectedDataMap;
	}
	
	
	
	public static Map<String, String> getExpectedValues1() throws Exception
	{
		String expectedValues = testData.get("Expected_Values").trim();
		Map<String, String> expectedDataMap = new LinkedHashMap<String, String>();
		String[] expParams = expectedValues.split("\n");
		String expectedVal = ""; int startIndex = 0;
		
		for(int i=0; i<expParams.length; i++)
		{
			expectedVal = expParams[i];
			if(expectedVal.length()>0)
			{
				if(expectedVal.startsWith("$"))
					startIndex = 2;
				if(! expectedVal.contains("="))
					throw new Exception("Expected_Values are not defined appropriately. Kindly check again.");
				else
					expectedDataMap.put(expectedVal.substring(startIndex, expectedVal.indexOf("=")).trim(), expectedVal.substring(expectedVal.indexOf("=")+1).trim());
			}
			startIndex = 0;
		}
		return expectedDataMap;
	}
	
	
	private static String getExpectedValueWithReusableDataFromRegEx(String regExString) throws Exception
	{
			String subString= "", reusableVal="";int i=0;
			int occurances = TestUtils.getNumberOfSubstringOccurance(regExString, "#Step:") + TestUtils.getNumberOfSubstringOccurance(regExString, "#Method:");
			while(i<occurances)
			{
				int index = 0, lastIndex = 0;
				if(regExString.contains("#Step:"))
				{
					index = regExString.indexOf("#Step:") + 6;
					lastIndex= regExString.indexOf("#", index);
					subString = regExString.substring(index, lastIndex);
					reusableVal = params_reused.get(subString);
					regExString = regExString.replace("#Step:"+subString+"#", reusableVal);
				}
				if(regExString.contains("#Method:"))
				{
					index = regExString.indexOf("#Method:") + 8;
					lastIndex= regExString.indexOf("#", index);
					subString = regExString.substring(index, lastIndex);
					logger.info("subString of method is : " + subString);
					reusableVal = getValueFromMethodExpressions_WithNested_Variables_Concates(subString);
					logger.info("getValueUsingReflectionMethods is : " + reusableVal);
					regExString = regExString.replace("#Method:"+subString+"#", reusableVal);
				}
				if(regExString.contains("#Step:") || regExString.contains("#Method:"))
					i=i-1;
				i++;
			}
		return regExString;	
	}
	public static void validateResponseData1(Response response1, Map<String, String> expectedDataMap, Map<String, String> nonExpectedDataMap) throws Exception
	{
		
		Response newResponse = null;
		String responseString = response1.getBody().asString();
		int actReponseCode =  response1.getStatusCode();
		String expResponseCode = testData.get("Expected_HTTP_Code").trim();
		int matcher = 0;
		
		if(expResponseCode.length()>0)
			if(! expResponseCode.equals(actReponseCode+"")){
				failureMsg = "Expected and actual HTTP response codes are not matching : Expected is - " + expResponseCode + " , and Actual is - " +actReponseCode ;
				matcher = 1;
			}
		
		if(matcher == 0)
		{
			if(testData.get("Response_Type").trim().equalsIgnoreCase("text"))
				newResponse = new ResponseBuilder().clone(response1).setBody(responseString).setContentType(ContentType.JSON).build();
			else
				newResponse = response1;
			validateExpectedData(newResponse, expectedDataMap);
			validateNonExpectedData(newResponse, nonExpectedDataMap);
		}
		
		if(testData.get("Reusable_Parameters").length() > 2)
			updateReusableParameters(newResponse);
		
	}
	private static String getExpectedValueFromReusableList(LinkedHashMap<String,String> data, String expression)
			throws Exception
	{
		//System.out.println("Expected value going to retrieve from exp is : " + expression);
		if(expression.startsWith("#Step:"))
			expression =getReusableParam(expression.replace("#Step:", "").replace("#", ""));
		else if(expression.startsWith("#Method:"))
			expression = getValueFromMethodExpressions_WithNested_Variables_Concates(expression);
		return expression;
	}
	
	public static void updateReusableParameters(Response response) throws Exception
	{
		String[] reusableNameAndValues = testData.get("Reusable_Parameters").trim().split("\n");
		String name="", value = "", stepNo = testData.get("Step_No");
		
		logger.info("reusableNameAndValues.length is : " + reusableNameAndValues.length);
		
		for(int i = 0; i<reusableNameAndValues.length; i++)
		{
			if(!reusableNameAndValues[i].startsWith("#Method:"))
			{
				name = reusableNameAndValues[i].split("=")[0].trim();
				logger.info("name is ::::::: " + name);
				value = reusableNameAndValues[i].split("=")[1].trim();
				logger.info("Value is :::::: " + value);
				name = stepNo + "_" + name;
				try{
				value = response.then().contentType(ContentType.JSON).extract().path(value.replace("$.", "")).toString();
				}catch(NullPointerException npe){
					value = "null";
				}
				params_reused.put(name, value);
			}
			else
				getValueFromMethodExpressions_WithNested_Variables_Concates(reusableNameAndValues[i]);
		}
	}
	private static void validateExpectedData(Response newResponse, Map<String, String> expectedDataMap) throws Exception
	{
		List<Object> listOfParameter = null;
		String expValue = "", actValue = "";
		for(String str : expectedDataMap.keySet())
		{
			expValue = expectedDataMap.get(str);
			if(expValue.startsWith("\n"))
				expValue = expValue.substring(1);
			
			if(!str.contains("[*]"))
			{
				try{
				actValue = newResponse.then().contentType(ContentType.JSON).extract().path(str).toString();
				//System.out.println("retrieved actual value for the str is : " + actValue);
				}catch(IllegalArgumentException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
					break;
				}catch(NullPointerException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
					break;
				}
				
				String val = "";
				if(expValue.startsWith("#RegEx:"))
				{
					expValue = getExpectedValueWithReusableDataFromRegEx(expValue);
					if(!(TestUtils.matchStringWithRegEx(actValue, expValue.replace("#RegEx:", "").trim())))
						failureMsg = failureMsg + "\n\nExpected RegEx and Actual values are not matching: Expected Reg ex is - " + expValue.replace("#RegEx:", "").trim() + 
						", and actual value - " + actValue;
				}
				else if(expValue.startsWith("#Step:") || expValue.startsWith("#Method:")){
					val = getExpectedValueFromReusableList(testData, expValue);
					if( ! val.equalsIgnoreCase(actValue))
						failureMsg = failureMsg + "\n\nExpected and Actual values of  '" + str + "' are not matching: expcted is = '" + expValue + " - " + val + "' ; Actual is = '" + actValue + "'.";
				}
				else
					if( ! expValue.equalsIgnoreCase(actValue))
						failureMsg = failureMsg + "\n\nExpected and Actual values of  '" + str + "' are not matching: expcted is = '" + expValue + "' ; Actual is = '" + actValue + "'.";
			}
			else
			{
				try{
					listOfParameter= newResponse.then().contentType(ContentType.JSON).extract().path(str.replace("[*]", ""));
					}catch(IllegalArgumentException iae){
						failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
						break;
					}catch(NullPointerException iae){
						failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
						break;
					}
				//System.out.println("Actual value sixe is : " + listOfParameter.size());
				String val=""; boolean contains = false;
				for(int i=0; i<listOfParameter.size(); i++){
					try{actValue = listOfParameter.get(i).toString();
					}catch(NullPointerException e){actValue="null";}
					if(expValue.startsWith("#RegEx:"))
					{
						expValue = getExpectedValueWithReusableDataFromRegEx(expValue);
						if((TestUtils.matchStringWithRegEx(actValue, expValue.replace("#RegEx:", "").trim()))){
							contains = true; break;}
					}
					else if(expValue.startsWith("#Step:") || expValue.startsWith("#Method:")){
						val = getExpectedValueFromReusableList(testData, expValue);
						if( val.equalsIgnoreCase(actValue)){
							contains = true; break;}
					}
					else
						if( expValue.equalsIgnoreCase(actValue)){
							contains = true; break;}
				}
				if(! contains)
					failureMsg = failureMsg + "\n\nExpected list value is not containing in actual response list : expected is = '" + expValue + "' ; Actual list is = '" + listOfParameter;
			}	
		}
	}
	
	private static void validateNonExpectedData(Response newResponse, Map<String, String> nonExpectedDataMap) throws Exception
	{
		List<Object> listOfParameter = null;
		String nonExpValue = "", actValue = "";
		for(String str : nonExpectedDataMap.keySet())
		{
			nonExpValue = nonExpectedDataMap.get(str);
			if(nonExpValue.startsWith("\n"))
				nonExpValue = nonExpValue.substring(1);
			
			if(!str.contains("[*]"))
			{
				//System.out.println("Trying to actual value for the str : " + str);
				try{
				actValue = newResponse.then().contentType(ContentType.JSON).extract().path(str).toString();
				//System.out.println("retrieved actual value for the str is : " + actValue);
				}catch(IllegalArgumentException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
					break;
				}catch(NullPointerException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
					break;
				}
				
				String val = "";
				if(nonExpValue.startsWith("#RegEx:"))
				{
					nonExpValue = getExpectedValueWithReusableDataFromRegEx(nonExpValue);
					if(TestUtils.matchStringWithRegEx(actValue, nonExpValue.replace("#RegEx:", "").trim()))
						failureMsg = failureMsg + "\n\nNon Expected RegEx and Actual values are matching: Non Expected Reg ex is - " + nonExpValue.replace("#RegEx:", "").trim() + 
						", and actual value - " + actValue;
				}
				else if(nonExpValue.startsWith("#Step:") || nonExpValue.startsWith("#Method:")){
					val = getExpectedValueFromReusableList(testData, nonExpValue);
					if(val.equalsIgnoreCase(actValue))
						failureMsg = failureMsg + "\n\nNon Expected and Actual values of  '" + str + "' are matching: non expcted is = '" + nonExpValue + " - " + val + "' ; Actual is = '" + actValue + "'.";
				}
				else
					if(nonExpValue.equalsIgnoreCase(actValue))
						failureMsg = failureMsg + "\n\nNon Expected and Actual values of  '" + str + "' are matching: non expcted is = '" + nonExpValue + "' ; Actual is = '" + actValue + "'.";
			}
			else
			{
				try{
				listOfParameter= newResponse.then().contentType(ContentType.JSON).extract().path(str.replace("[*]", ""));
				}catch(IllegalArgumentException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
					break;
				}catch(NullPointerException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
					break;
				}
				//System.out.println("Actual value sixe is : " + listOfParameter.size());
				String val=""; boolean contains = false;
				for(int i=0; i<listOfParameter.size(); i++){
					actValue = listOfParameter.get(i).toString();
					if(nonExpValue.startsWith("#RegEx:"))
					{
						nonExpValue = getExpectedValueWithReusableDataFromRegEx(nonExpValue);
						if((TestUtils.matchStringWithRegEx(actValue, nonExpValue.replace("#RegEx:", "").trim()))){
							contains = true; break;}
					}
					else if(nonExpValue.startsWith("#Step:") || nonExpValue.startsWith("#Method:")){
						val = getExpectedValueFromReusableList(testData, nonExpValue);
						if( val.equalsIgnoreCase(actValue)){
							contains = true; break;}
					}
					else
						if( nonExpValue.equalsIgnoreCase(actValue)){
							contains = true; break;}
				}
				if(contains)
					failureMsg = failureMsg + "\n\nNon Expected value is containing in actual response list : non expected is = '" + nonExpValue + "' ; Actual list is = '" + listOfParameter;
			}
		}
	}
	
	
	public static void putDataIntoOutputExcelMap()
	{
		outputfileMap.put("Step_No", testData.get("Step_No"));summaryResultFileMap.put("Step_No", testData.get("Step_No"));
		outputfileMap.put("API_Component", testData.get("API_Component"));
		outputfileMap.put("Description", testData.get("Description"));
		outputfileMap.put("API_Type", testData.get("API_Type"));
		outputfileMap.put("Expected_HTTP_Code", testData.get("Expected_HTTP_Code"));
		outputfileMap.put("Expected_Values", testData.get("Expected_Values"));
		outputfileMap.put("Request_Headers", testData.get("Request_Headers"));
		outputfileMap.put("NonExpected_Values", testData.get("NonExpected_Values"));
	}
	
	
	public static void appendResponseIntoTextFile(String response, String tcName) throws IOException
	{
		String l1 = "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+tcName+" : " + testData.get("Step_No") + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<" + 
		 "\n" + response +
		"\n##############################################################################################################";	
		ReadExternalFiles.appendTextInFile(jsonBodyTextfilePathAndName, l1);
	}
	
	
	
	
	public static Object[][] getTestcaseData(String testDataSheetName, String testDataTableName) throws Exception
	{
		testsMap.put(testDataTableName, extent.startTest(testDataTableName));
		if(!(ReadExternalFiles.isTCRunmodeSet(testDataSheetName, testDataTableName))){
			storeSkipResultsIntoExcelFiles(testDataSheetName, testDataTableName);
			throw new SkipException(testDataTableName + "runmode is set to No/null in the RunModes sheet");
		}
		Object[][] retObjArr=ReadExternalFiles.getTableData(testDataSheetName,testDataTableName);
		Object[][] data = TestUtils.Obj2Map(retObjArr, ReadExternalFiles.getColumnHeaders(testDataSheetName, testDataTableName));
		
		ArrayList<String> namesList= getJsonChildTableNameList(retObjArr, ReadExternalFiles.getColumnHeaders(testDataSheetName, testDataTableName));
		for(int i = 0; i<namesList.size(); i++)
		{
			Object[][] jsonDataObj=ReadExternalFiles.getJsonData(testDataSheetName,namesList.get(i));
			prepareJsonBody1(testDataSheetName, namesList.get(i), jsonDataObj, ReadExternalFiles.getJsonAttributes(testDataSheetName, namesList.get(i).split(":")[0]));
		}
		DataFilesValidator.validateInputData1(data, testDataSheetName, testDataTableName);
		params_reused.clear();
		return data;
	}
	
	
	public static void doExecuteRequest(String tcName) throws Exception
	{
		
		putDataIntoOutputExcelMap();
		executeIfDependentStepPassed(tcName);
		Response actualResponse = getResponseOfAPI();
		String actualResponseBody = actualResponse.getBody().asString();
		outputfileMap.put("Actual_Response_Body", actualResponseBody);
		appendResponseIntoTextFile(actualResponseBody, tcName);
		Map<String, String> expectedDataMap = getExpectedValues1();
		logger.info("full data is :::" + testData);
		logger.info("expectedDataMap is :::" + expectedDataMap);
		Map<String, String> nonExpectedDataMap = getNonExpectedValues1();
		logger.info("nonExpectedDataMap is :::" + nonExpectedDataMap);
		validateResponseData1(actualResponse, expectedDataMap, nonExpectedDataMap);
		logger.info("failureMessage is " + failureMsg);
		outputfileMap.put("Reusable_Parameters", params_reused.toString());
	}
	
	private static void executeIfDependentStepPassed(String tcName) throws ExecutionException
	{
		if(testStepResultMap.keySet().size() > 0)
		{
			String step = "";
			for(String keys: testStepResultMap.keySet())
			{
				step = keys.replace(tcName+"#", "");
				if((testData.get("Additional_Path").contains("#Step:"+step)) || (testData.get("Request_Parameters").contains("#Step:"+step))
						|| (testData.get("Request_JsonBody").contains("#Step:"+step)) || (testData.get("Expected_Values").contains("#Step:"+step)) || 
						(testData.get("Reusable_Parameters").contains("#Step:"+step)) || (testData.get("NonExpected_Values").contains("#Step:"+step)))
				{
					if(!(testStepResultMap.get(keys).equalsIgnoreCase("Passed")))
					{
						//System.out.println("The earlier dependent step '" + step+"' has been failed");
						failureMsg = "The earlier dependent step '" + step+"' has been failed";
						throw new ExecutionException("The earlier dependent step '" + step+"' has been failed", null);
					}
				}
			}
		}
	}
	
	public static void storeResultsIntoExcelFiles(String testDataSheetName, String testDataTableName, String testStatus) throws Exception
	{
		if(!(testStatus.length()>1)){
			testStatus = "Error";
			if(!(failureMsg.length()>1))
				failureMsg="There is an internal server error.";
		}
		testStepResultMap.put(testDataTableName+"#"+testData.get("Step_No"), testStatus);
		
		outputfileMap.put("Failure_Message", failureMsg);
		summaryResultFileMap.put("Failure_Message", failureMsg);
		
		outputfileMap.put("TestCase Name", testDataTableName);
		outputfileMap.put("Test_Status", testStatus);
		
		summaryResultFileMap.put("TestCase Name", testDataTableName);
		summaryResultFileMap.put("Test_Status", testStatus);
		
		List<String> valuesList = new ArrayList<String>();
		List<String> summaryValuesList = new ArrayList<String>();
		
		String jsonBody = outputfileMap.get("Actual_Response_Body").toString();
		String[] jBody = jsonBody.split("\n");
		if(jBody.length>20){
				jsonBody = "";
			for(int i=0; i<19; i++)
				jsonBody = jsonBody+jBody[i]+"\n";
			jsonBody = jsonBody+"Continued... with full data in output text file for reference";
		}
		outputfileMap.put("Actual_Response_Body", jsonBody);
		
		String values[] = new String[outputfileMap.size()];
		String summaryValues[] = new String[summaryResultFileMap.size()];
		
		for(String key : outputfileMap.keySet()){
			valuesList.add(outputfileMap.get(key).toString());
			outputfileMap.put(key, "");
		}
		for(int i=0; i<values.length;i++)
			values[i] = valuesList.get(i);
		
		for(String key : summaryResultFileMap.keySet()){
			summaryValuesList.add(summaryResultFileMap.get(key).toString());
			summaryResultFileMap.put(key, "");
		}
		for(int i=0; i<summaryValues.length;i++)
			summaryValues[i] = summaryValuesList.get(i);
		
		ReadExternalFiles.setResultsInOutputExcel(resultSummaryPath, testDataSheetName, summaryValues);
		ReadExternalFiles.setResultsInOutputExcel(outputExcelPath, testDataSheetName, values);
	}
	
	
	private static void storeSkipResultsIntoExcelFiles(String testDataSheetName, String testDataTableName) throws Exception
	{
			outputfileMap.put("TestCase Name", testDataTableName);
			outputfileMap.put("Test_Status", "Skipped");
			summaryResultFileMap.put("TestCase Name", testDataTableName);
			summaryResultFileMap.put("Test_Status", "Skipped");
			String sMsg = "This test case run mode has been set to No, hence the test case is completely skipped";
			outputfileMap.put("Failure_Message", sMsg);
			summaryResultFileMap.put("Failure_Message", sMsg);
			
			List<String> valuesList = new ArrayList<String>();
			List<String> summaryValuesList = new ArrayList<String>();
			
			String values[] = new String[outputfileMap.size()];
			String summaryValues[] = new String[summaryResultFileMap.size()];
			
			for(String key : outputfileMap.keySet()){
				valuesList.add(outputfileMap.get(key).toString());
				outputfileMap.put(key, "");
			}
			for(int i=0; i<values.length;i++)
				values[i] = valuesList.get(i);
			
			for(String key : summaryResultFileMap.keySet()){
				summaryValuesList.add(summaryResultFileMap.get(key).toString());
				summaryResultFileMap.put(key, "");
			}
			for(int i=0; i<summaryValues.length;i++)
				summaryValues[i] = summaryValuesList.get(i);
			
			ReadExternalFiles.setResultsInOutputExcel(resultSummaryPath, testDataSheetName, summaryValues);
			ReadExternalFiles.setResultsInOutputExcel(outputExcelPath, testDataSheetName, values);
		
	}
	
	
	
	
	
    public static void prepareJsonBody (String testDataSheetName, String jsonIPId, Object[][] jsonParamsValues, String[] attributes) throws Exception {
    	
        int i; String jsonBody = ""; String ipId = "";
        //System.out.println("JsonIP_Id is : " + jsonIPId);
        String[] startEndPoints = ReadExternalFiles.getStartEndpointofJson(testDataSheetName, jsonIPId.split(":")[0].trim()).split(":");
        String val = ""; String outerBodyContent = "", innerBodyContent = "";
        
        for (i=0;i<jsonParamsValues.length;i++)
        {
        	ipId = jsonParamsValues[i][0].toString();
	        for(int k=1;k<attributes.length;k++)
	        {
	             if(attributes[k].startsWith("attributes."))
	             {
	            	 val = attributes[k].replace("attributes.", "");
	            	 if(!(TestUtils.matchStringWithRegEx(val, ".*__[a-z]") || TestUtils.matchStringWithRegEx(val, ".*__[a-z][a-z]")))
	            		 throw new Exception ("Given attribute is not ending with variable type, and the given attribute is : " + attributes[k]);
	            	 if(val.endsWith("__s"))
	            		 innerBodyContent = innerBodyContent + jsonDoubleQuote+ val + jsonDoubleQuote + ": " +jsonDoubleQuote+jsonParamsValues[i][k].toString()+jsonDoubleQuote+",\n";
	            	else if(val.endsWith("__i") || val.endsWith("__l") || val.endsWith("__d")){
	            		if(jsonParamsValues[i][k].toString().length()>0)
	            			innerBodyContent = innerBodyContent + jsonDoubleQuote+ val + jsonDoubleQuote + ": " +jsonParamsValues[i][k].toString()+",\n";
	            		else
	            			innerBodyContent = innerBodyContent + jsonDoubleQuote+ val + jsonDoubleQuote + ": " +"0,\n";
	            	}else if(val.endsWith("__sa"))
	            	 {
	            		 String temp = "";
	            		 String[] vals = jsonParamsValues[i][k].toString().split("\n");
	            		 for(int j=0; j<vals.length; j++)
	            			 temp = jsonDoubleQuote+vals[j]+jsonDoubleQuote+",\n";
	            		 temp = temp.substring(0, temp.length()-2);
	            		 innerBodyContent = innerBodyContent + jsonDoubleQuote+ val + jsonDoubleQuote + ": [" + temp + "],\n";
	            	 }
	             }
	             else{
	            	 outerBodyContent = outerBodyContent + jsonDoubleQuote+ attributes[k] + jsonDoubleQuote + ": " +jsonDoubleQuote+jsonParamsValues[i][k].toString()+jsonDoubleQuote+",\n";	 
	             }
	        }
	        
	        innerBodyContent = innerBodyContent.substring(0, innerBodyContent.length()-2);
	        outerBodyContent = outerBodyContent.substring(0, outerBodyContent.length()-1);
	        innerBodyContent = "\"attributes\": {\n"+innerBodyContent+"}";
	        jsonBody = startEndPoints[0] + outerBodyContent + innerBodyContent + startEndPoints[1];
	        ipJsonMap.put(jsonIPId.split(":")[0].trim()+":"+ipId, jsonBody);
	        jsonBody = "";
	        outerBodyContent = ""; innerBodyContent = ""; ipId="";
        }
        
    }
	
	
    
    private static String validateValueBasedOnParam(String parameter, String value)
    {
    	if(parameter.endsWith("__s"))
    	{
    		//System.out.println("valuei s ending with __s, and it is : " + value);
    		if(value.length()>0){
	    		if(!value.startsWith(jsonDoubleQuote))
	    			value = jsonDoubleQuote+value;
	    		if(!value.endsWith(jsonDoubleQuote))
	    			value = value + jsonDoubleQuote;
    		}
    		else
    			value = jsonDoubleQuote+jsonDoubleQuote;
    	}
	   	else if(parameter.endsWith("__i") || parameter.endsWith("__l") || parameter.endsWith("__d")){
	   		if(value.startsWith(jsonDoubleQuote) || value.endsWith(jsonDoubleQuote))
	   			value = value.replaceAll(jsonDoubleQuote, "");
	   		if(value.length()<1)
	   			value = "0";
	   	}
	   	else if(value.endsWith("__sa"))
		 {
			 String temp = "";String temp1 = "";
			 String[] vals = value.split("\n");
			 for(int j=0; j<vals.length; j++){
				 temp1 = vals[j];
				 if(!temp1.startsWith(jsonDoubleQuote))
					 temp1 = jsonDoubleQuote+temp1;
	    		if(!temp1.endsWith(jsonDoubleQuote))
	    			temp1 = temp1 + jsonDoubleQuote;
	    		if(temp1.length() < 1)
	    			temp1 = jsonDoubleQuote+jsonDoubleQuote;
	    		temp = temp+temp1+",\n";
	    		}
			 value = temp;
		 }
	   return value;
    }
public static void prepareJsonBody1(String testDataSheetName, String jsonIPId, Object[][] jsonParamsValues, String[] attributes) throws Exception {
    	
        //System.out.println("JsonIP_Id is : " + jsonIPId);
        String[] startEndPoints = ReadExternalFiles.getStartEndpointofJson(testDataSheetName, jsonIPId.split(":")[0].trim()).split(":");
        
        Map<String, LinkedHashMap<String, String>> stepMap = new LinkedHashMap<String, LinkedHashMap<String, String>>();
        
        for(int j=0; j<jsonParamsValues.length; j++)
        {
        	stepMap.put(jsonParamsValues[j][0].toString(), null);
        	LinkedHashMap<String, String> attributeMap = new LinkedHashMap<String, String>();
        	 for(int k=1; k<attributes.length; k++)
             {
        		 attributeMap.put(attributes[k], jsonParamsValues[j][k].toString());
        		 stepMap.put(jsonParamsValues[j][0].toString(), attributeMap);
             }
        }
        
        String value = "";
        for(String step : stepMap.keySet())
        	for(String parameter : stepMap.get(step).keySet())
        	{
        		value = stepMap.get(step).get(parameter);
        		value = validateValueBasedOnParam(parameter, value);
        		stepMap.get(step).put(parameter, value);
        	}
        
        LinkedHashSet<String> allParamsSet = new LinkedHashSet<String>();
        int innerAttributesMaxCount = 0;
        for(String step : stepMap.keySet())
        	for(String parameter : stepMap.get(step).keySet())
        	{
        		if(TestUtils.getNumberOfSubstringOccurance(parameter, ".") > innerAttributesMaxCount)
            		innerAttributesMaxCount = TestUtils.getNumberOfSubstringOccurance(parameter, ".");
            	if(parameter.contains("."))
            		allParamsSet.add(parameter.substring(0,  parameter.lastIndexOf(".")+1));
            	else
            		allParamsSet.add(parameter);
        	}
        
        for(String step : stepMap.keySet())
        {
        	LinkedHashMap<String, String> sMap = stepMap.get(step);
        	LinkedHashMap<String, String> shortenMap = getMapOfSortedJsonValues(sMap, allParamsSet);
        	LinkedHashMap<Integer, List<String>> descCountMapAttributesList =  getDescSortedAttributesList(shortenMap, innerAttributesMaxCount);
        	LinkedHashMap<String, String> shortenMap1 = new LinkedHashMap<String, String>();
        	
        	for(String ss : shortenMap.keySet())
        		shortenMap1.put(ss, shortenMap.get(ss));
        	
        	String jsonBody = "";
        	for(int k : descCountMapAttributesList.keySet())
        	{
        		LinkedHashMap<String, String> rMap = getConcatenatedToParentAttributes(descCountMapAttributesList.get(k), shortenMap1);
        		
        		for(int m=0; m<descCountMapAttributesList.get(k).size(); m++)
        			shortenMap1.remove(descCountMapAttributesList.get(k).get(m));
        		
        		for(String keyss :  rMap.keySet())
        		{
        			if(keyss.equalsIgnoreCase("main."))
        				shortenMap1.put("main", rMap.get(keyss));
        			else{
	        			String sm1VConc = shortenMap1.get(keyss)+rMap.get(keyss);
	        			shortenMap1.put(keyss, sm1VConc);
        			}
        		}
        	}
        	
    		jsonBody = shortenMap1.get("main");
    		jsonBody = startEndPoints[0]+jsonBody+startEndPoints[1];
    		jsonBody = jsonBody.replaceAll(",\n\n}", "\n}");
    		jsonBody = jsonBody.replaceAll(",\n}", "\n}");
    		ipJsonMap.put(jsonIPId.split(":")[0].trim()+":"+step, jsonBody);
        }
    }
	private static LinkedHashMap<String, String> getConcatenatedToParentAttributes( List<String> descCountMapList, LinkedHashMap<String, String> shortenMap)
	{
		LinkedHashMap<String, String> attributeNameToConcat = new LinkedHashMap<String, String>();
		LinkedHashMap<String, String> attributeNamePathSubParent = new LinkedHashMap<String, String>();
		for(int j=0; j<descCountMapList.size(); j++){
			String val = (String) descCountMapList.get(j);
			String val1 = (String) descCountMapList.get(j);
			if(val.endsWith(".")){
				val = val.substring(0, val.lastIndexOf("."));
				val1 = val1.substring(0, val1.lastIndexOf("."));
				if(val.contains(".")){
					val = val.substring(val.lastIndexOf(".")+1);
					val1 = val1.substring(0, val1.lastIndexOf(".")+1);
				}
				else
					val1 = "main.";
			}
			else
				val1="main";
			attributeNameToConcat.put(descCountMapList.get(j), val);
			attributeNamePathSubParent.put(descCountMapList.get(j), val1);
		}
		LinkedHashMap<String, String> mergedMap = new LinkedHashMap<String, String>();
		for(String str : attributeNamePathSubParent.keySet())
			mergedMap.put(attributeNamePathSubParent.get(str), "");
		
		LinkedHashSet<String> subParentAllSet = new LinkedHashSet<String>();
		for(String s:attributeNamePathSubParent.keySet())
			subParentAllSet.add(attributeNamePathSubParent.get(s));
		
		int numberOfKeys = 0; boolean isMain = false, isMainDot = false;
		for(String s:attributeNamePathSubParent.keySet())
		{
			if(attributeNamePathSubParent.get(s).equalsIgnoreCase("main")){
				isMain = true; numberOfKeys = 1;break;}
			else if(attributeNamePathSubParent.get(s).equalsIgnoreCase("main.")){
				isMainDot = true; break;}
		}
		if(numberOfKeys!=1)
			numberOfKeys = attributeNamePathSubParent.size();
		
		String concatenatedValues = "";
		if(isMain)
		{
			for(String str : attributeNamePathSubParent.keySet())
				concatenatedValues = concatenatedValues+shortenMap.get(str);
			mergedMap.put("main", concatenatedValues);
		}
		else if(isMainDot)
		{
			for(String str : attributeNameToConcat.keySet())
			{
				String val = jsonDoubleQuote+attributeNameToConcat.get(str)+jsonDoubleQuote+": {\n"+shortenMap.get(str)+"\n},";
				concatenatedValues = concatenatedValues+val;
				//System.out.println("main dot conca val is : " + concatenatedValues);
			}
			mergedMap.put("main.", concatenatedValues);
		}
		else
		{
			for(String str : attributeNameToConcat.keySet())
			{
				String val = jsonDoubleQuote+attributeNameToConcat.get(str)+jsonDoubleQuote+": {\n"+shortenMap.get(str)+"\n},";
				concatenatedValues = concatenatedValues+val;
				mergedMap.put(attributeNamePathSubParent.get(str), concatenatedValues);
			}
		}
		//System.out.println("mergedMap iss : " + mergedMap);
		return mergedMap;
	}
	
	
	private static LinkedHashMap<Integer, List<String>> getDescSortedAttributesList(LinkedHashMap<String, String> shortenMap, int dotCount)
	{
		HashMap<String, Integer> countMap = new HashMap<String, Integer>();
		for(int i=dotCount; i>=0; i--)
			for(String s : shortenMap.keySet())
				countMap.put(s, TestUtils.getNumberOfSubstringOccurance(s, "."));
		
		LinkedHashMap<Integer, List<String>> descCountMap = new LinkedHashMap<Integer, List<String>>();
		for(int i=dotCount; i>=0; i--)
		{
			List<String> paramList = new ArrayList<String>();
			for(String k : countMap.keySet())
			{
				if(countMap.get(k) == i)
					paramList.add(k);
			}
			descCountMap.put(i, paramList);
		}
		return descCountMap;
	}
	
	
	private static LinkedHashMap<String, String> getMapOfSortedJsonValues(LinkedHashMap<String, String> sMap, LinkedHashSet<String> allParamsSet)
	{
		String value = ""; String kr = "";
		LinkedHashMap<String, String> allParamsSetMap = new LinkedHashMap<String, String>();
    	Iterator iter = allParamsSet.iterator();
        while (iter.hasNext()) {
        	allParamsSetMap.put(iter.next().toString(), "");
        }
        //System.out.println("allParamsSetMap sis : " + allParamsSetMap);
        for(String key: allParamsSetMap.keySet())
        {
        	for(String sKey : sMap.keySet())
        	{
        		if(sKey.equals(key)){
        			value = jsonDoubleQuote+key+jsonDoubleQuote+": "+sMap.get(sKey)+",\n";
        		}
        		if((sKey.length() > key.length()) && sKey.startsWith(key))
        		{
        			kr = sKey.replace(key, "");
        			if(!(kr.substring(1).contains(".")))
        				value = jsonDoubleQuote+kr.substring(0)+jsonDoubleQuote+": "+sMap.get(sKey)+",\n";
        		}
        		allParamsSetMap.put(key, allParamsSetMap.get(key)+value);
        		value = "";
        	}
        }
        //System.out.println("allParamsSetMap is : " + allParamsSetMap);
    	
        for(String str : allParamsSetMap.keySet())
        {
        	//System.out.println("Value of " + str + " is ==> " + allParamsSetMap.get(str));
        }
        
        return allParamsSetMap;
	}







    private static String getParamUsingAttributeAndValue(String key, String value)
    {
    	//System.out.println("Received key and value are : " + key + "; " + value);
    	String concStr = "";
    	if(key.endsWith("__s"))
    		concStr = jsonDoubleQuote+ key + jsonDoubleQuote + ": " +jsonDoubleQuote+value+jsonDoubleQuote+",\n";
	   	else if(key.endsWith("__i") || key.endsWith("__l") || key.endsWith("__d")){
	   		if(value.length()>0)
	   			concStr = jsonDoubleQuote+ key + jsonDoubleQuote + ": " +value+",\n";
	   		else
	   			concStr = jsonDoubleQuote+ value + jsonDoubleQuote + ": " +"0,\n";
	   	}else if(value.endsWith("__sa"))
		 {
			 String temp = "";
			 String[] vals = value.split("\n");
			 for(int j=0; j<vals.length; j++)
				 temp = jsonDoubleQuote+vals[j]+jsonDoubleQuote+",\n";
			 temp = temp.substring(0, temp.length()-2);
			 concStr = jsonDoubleQuote+ value + jsonDoubleQuote + ": [" + temp + "],\n";
		 }
	   	else
	   		concStr = jsonDoubleQuote+ key + jsonDoubleQuote + ": " + value + ",\n";
    	
    	return concStr ;
    }
    
    
    
	private static ArrayList<String> getJsonChildTableNameList(Object[][] retObjArr, String[] columnHdrs)
	{
        ArrayList<String> tableNames=new ArrayList<String>();
	    try{
	        for (int i=0;i<retObjArr.length;i++)
	        	for(int k=0;k<columnHdrs.length;k++){
		             
	        		if(columnHdrs[k].equalsIgnoreCase("Request_JsonBody"))
	        		{
	        			if(retObjArr[i][k].toString().contains(":IP_"))
	        				tableNames.add(retObjArr[i][k].toString());
	        		}
	        	}
        }catch(Exception e){
            e.printStackTrace();
        }
        return tableNames;
	}
	
	
	
	
	
	@AfterSuite
	public void executeAfterSuite() throws Exception
	{
		logger.info("Execution started in After suite");
		if(ReadExternalFiles.getConfigFileValue("SortReports").contains("Y") || ReadExternalFiles.getConfigFileValue("SortReports").contains("Y"))
			PostBuild.main(null);
		logger.info("Execution end from After suite");
	}
	
	
}
