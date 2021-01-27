package API_Testing.Functional.Automation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.ServerNotActiveException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.management.InvalidAttributeValueException;
import javax.xml.bind.PropertyException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.poi.hssf.eventusermodel.HSSFUserException;
import org.omg.CosNaming.NamingContextExtPackage.InvalidAddress;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.restassured.builder.ResponseBuilder;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import static com.jayway.restassured.RestAssured.given;

public class API_Header {
	
	public static ExtentReports extent ;
	
	
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
	
	
	public static ExtentTest test ;
	public static LinkedHashMap<String, ExtentTest> dpFailedTestsMap = new LinkedHashMap<String, ExtentTest>();
	public static LinkedHashMap<String, String> testStatus = new LinkedHashMap<String, String>();
	public static LinkedHashMap<String, ExtentTest> moduleClassExtentMap = new LinkedHashMap<String, ExtentTest>();
	public static LinkedHashMap<String, String> moduleStatus = new LinkedHashMap<String, String>();
	
	static{
		logger  = Logger.getLogger(API_Header.class);
		PropertyConfigurator.configure("log4j.properties");
		logger.info("Executed the static block of API_Header");
		
		ReadExternalFiles.createTextFile(System.getProperty("user.dir")+"\\ConsoleOutput.txt");
		PrintStream console = System.out;
		File file = new File(System.getProperty("user.dir")+"\\ConsoleOutput.txt");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PrintStream ps = new PrintStream(fos);
		System.setOut(ps);
		
		new File(System.getProperty("user.dir")+"\\CustomReport.html").delete();
		extent = new ExtentReports(System.getProperty("user.dir")+"/CustomReport.html",true);
		extent.addSystemInfo("ServerEndPoint", ReadExternalFiles.getConfigFileValue("ServerEndPoint"));
		extent.loadConfig(new File(System.getProperty("user.dir")+"/extent-config.xml"));
	}
	
	@BeforeSuite
	public static void preExecution() throws AddressException, InvalidAttributeValueException, PropertyException, InvalidAddress, IOException, HSSFUserException, ServerNotActiveException
	{
		logger.info("Started Executing @BeforeSuite");
		DataFilesValidator.doPreSuiteValidations();
		setupTestResultDirectory();
		logger.info("Executed @BeforeSuite");
	}
	
	
	private static void setupTestResultDirectory() throws IOException
	{
		logger.info("Started executing setupTestResultDirectory");
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
		
		List<String> totalSheetsList = ReadExternalFiles.readPropertiesWithActiveRunmode();
		try {
			CreateExcelFile.createExcelFileWithHeaders(outputExcelPath, headerRow, totalSheetsList);
			CreateExcelFile.createExcelFileWithHeaders(resultSummaryPath, summaryHeaderRow, totalSheetsList);
		}catch (Exception e) {
			logger.warn("Found an exception in method createExcelFileWithHeaders and it is : " + e.getMessage());
			e.printStackTrace();
		}
		logger.info("Created directory is : " + SystemDirectoryUtil.createDir(projectDirectory+"\\Test_Result\\JsonResponses"));
		ReadExternalFiles.setPropertyIntoConfigFile("LastExecutionTime", executionStartTime.replace(":", "-"));
		logger.info("Set setPropertyIntoConfigFile");
		logger.info("Executed setupTestResultDirectory successfully");
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
	/*private static String getAdditionalPath(String definedAddionalPath)
			throws Exception
	{
		String path = "";
		String lastPath = "", pathArrValue="";
		
		if((definedAddionalPath.length()>1) && (definedAddionalPath.contains("#Step:") || definedAddionalPath.contains("#Method:")))
		{
			String[] pathArr = definedAddionalPath.split("/"); //Since path will have forward slashes I used that only to split. If more than one expressions are there, then it helps. like /exp/exp
			String pValue = "";
			int index=0, lastIndex = 0;String subString = "";
			
			
			
				for(int i=1; i<pathArr.length; i++)
				{ //Since path starts from forward slash only, so i value initialized to 1.
					pathArrValue = pathArr[i];
					if(pathArrValue.contains("?")){
						// In case list of objects appended to that path, then it usually continues in path after question mark.
						pathArrValue = pathArr[i].substring(0, pathArrValue.indexOf("?"));
						lastPath = pathArr[i].substring(pathArr[i].indexOf("?"));
						lastPath = lastPath.replace("\n", ""); // Values we need to append to url as list of objects, we defined them with new line in test data cell. 
					}
					if(pathArrValue.startsWith("#Step:"))
					{
						pValue = pathArrValue.replace("#Step:", "").replace("#", "");
						// If the expression is there and starts with step, then from below libe of code, it wll get the value utilizing step number mentioned in expression
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
	}*/
	
	private static String getAdditionalPath(String definedAddionalPath)
			throws Exception
	{
		String fLine = ""; String subString= "", reusableVal="";
		logger.info("definedAddionalPath is - " + definedAddionalPath);
		if((definedAddionalPath.length()>1) && (definedAddionalPath.contains("#Step:") || definedAddionalPath.contains("#Method:")))
		{
			String[] jsonBodyArr = definedAddionalPath.split("/"); // Just splitting with new line to make dynamic values replacement in better way, if json body contains expressions to replace.
			int i=1; fLine = jsonBodyArr[i];
			logger.info("jsonBodyArr with i " + i + " is - " + jsonBodyArr[i]);
			while(i<jsonBodyArr.length)
			{
				int index = 0, lastIndex = 0;
					if(fLine.contains("#Step:"))
					{
						index = fLine.indexOf("#Step:") + 6;
						lastIndex= fLine.indexOf("#", index);
						subString = fLine.substring(index, lastIndex);
						//If the expression is of step related, it will get only the expression from complete line of string, and pass in below line to get respective value from re usbale params variable.
						reusableVal = getReusableParam(subString);
						fLine = fLine.replace("#Step:"+subString+"#", reusableVal); // Retrieved value is going to replaced in json body expression exactly.
						definedAddionalPath = definedAddionalPath.replace("#Step:"+subString+"#", reusableVal);
					}
					if(fLine.contains("#Method:"))
					{
						index = fLine.indexOf("#Method:") + 8;
						lastIndex= fLine.indexOf("#", index);
						subString = fLine.substring(index, lastIndex);
						//If the expression is of method related, it will get value by executing below line properly, by passing retrieved expression only.
						reusableVal = getValueFromMethodExpressions_WithNested_Variables_Concates(subString);
						fLine = fLine.replace("#Method:"+subString+"#", reusableVal); // Retrieved value is replaced exactly in the place of expression.
						definedAddionalPath = definedAddionalPath.replace("#Method:"+subString+"#", reusableVal);
					}
					logger.info("fLine after replace is - " + fLine);
					if(fLine.contains("#Step:") || fLine.contains("#Method:"))
						i=i-1;	 // Same line of string may have more than one expression. So again the same line looped back by decrementing. If containing once only, then this decrementing will not be executed by control any how
				i++;
			}
			
		}
		return definedAddionalPath;
	}
	
	private static String getJsonBodyWithReusableParameters() throws Exception
	{
		String fLine = ""; String subString= "", reusableVal="";
		String jsonBody = testData.get("Request_JsonBody");
		if((jsonBody.length() > 2)) // If not containing more than 2 characters, it will return by default value, since value will be empty definitely.
		{
			if(jsonBody.contains("IP_"))
				jsonBody = ipJsonMap.get(jsonBody.split(":")[0].trim() + ":" + jsonBody.split(":")[1].trim()); // If jsonbody cell data contining IP:, then the josn body must be prepared and stored in the map. So getting from there only.
			// If json body not containing IP, then json body content will be there in that jsonBody variable by default.
			String[] jsonBodyArr = jsonBody.split("\n"); // Just splitting with new line to make dynamic values replacement in better way, if json body contains expressions to replace.
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
						//If the expression is of step related, it will get only the expression from complete line of string, and pass in below line to get respective value from re usbale params variable.
						reusableVal = getReusableParam(subString);
						fLine = fLine.replace("#Step:"+subString+"#", reusableVal); // Retrieved value is going to replaced in json body expression exactly.
						jsonBody = jsonBody.replace("#Step:"+subString+"#", reusableVal);
					}
					if(fLine.contains("#Method:"))
					{
						index = fLine.indexOf("#Method:") + 8;
						lastIndex= fLine.indexOf("#", index);
						subString = fLine.substring(index, lastIndex);
						//If the expression is of method related, it will get value by executing below line properly, by passing retrieved expression only.
						reusableVal = getValueFromMethodExpressions_WithNested_Variables_Concates(subString);
						fLine = fLine.replace("#Method:"+subString+"#", reusableVal); // Retrieved value is replaced exactly in the place of expression.
						jsonBody = jsonBody.replace("#Method:"+subString+"#", reusableVal);
					}
					if(fLine.contains("#Step:") || fLine.contains("#Method:"))
						i=i-1;	 // Same line of string may have more than one expression. So again the same line looped back by decrementing. If containing once only, then this decrementing will not be executed by control any how
				i++;
			}
		}
		return jsonBody;
	}
	
	private static String getValueFromMethodExpressions_WithNested_Variables_Concates(String methodDefinition)
			throws Exception
	{
		// Method expressions may be like - #Method:MethodName(args)+StringToConcatenate=VariableName#
		// Method expressions may also be like - #Method:StringToConcatenate+MethodName(args)=VariableName#
		// Method expressions may also be like - #MethodName(args)=VariableName#
		// Method expressions may also be like - #MethodName(StepExpression)=VariableName# and as such.
		// These expressions are hold in methodDefinition variable.
		String value = "", variableToStore="";
		String concatinationString = ""; boolean isConStringFirst = false;
		String mArr0 = "", mArr1="";int index = 0;
		if(methodDefinition.contains("="))
		{
			String[] methodDefinitionArr = methodDefinition.split("=");
			methodDefinition = methodDefinitionArr[0];
			variableToStore = methodDefinitionArr[1]; // Variable will be in the last of the expression by default.
		}
		methodDefinition = methodDefinition.replace("#Method:", "");
		if(methodDefinition.endsWith("#"))
			methodDefinition = methodDefinition.replace("#", "");
		// Above lines make expression additional syntax strings free.
		if(!methodDefinition.contains("+")) //If the expression is not having concatenation string, it will directly evaluate the expression and get the value using below line of code. If contains + symbol, then ges to else part.
			value = getValueFromMethodExpressionWithNested(methodDefinition);
		else
		{
			// If the argument is not any expression, then exact argument is going to be filtered and executed the expression properly to get the value.
			index = methodDefinition.indexOf("+");
			mArr0 = methodDefinition.substring(0, index);
			mArr1 = methodDefinition.substring(index+1);
			if(mArr0.contains("(") && mArr0.endsWith(")")){
				concatinationString = mArr1;methodDefinition=mArr0;}
			else{methodDefinition=mArr1;
				concatinationString = mArr0;isConStringFirst=true;}
			value = getValueFromMethodExpressionWithNested(methodDefinition);
		}
		if(isConStringFirst) //concatenation string can be present in any side of the method name in the expression, so accordingly concatenation is take care.
			value = concatinationString + value;
		else
			value = value + concatinationString;
		if(variableToStore.length() > 0) //If the variable name is mentioned to store the resulted value, then that key name should have minimum one character.
			params_reused.put(testData.get("Step_No")+"_"+variableToStore, value); // If the reusbale variable name is mentioned, then the resulted value is going t be stored in the params_reused map.
		return value;
	}
	private static String getValueFromMethodExpressionWithNested(String methodExpression) throws Exception
	{
		int index = methodExpression.indexOf("(");
		String methodName = methodExpression.substring(0, index); // finding out only the method name without arguments.
		String methodArgument = methodExpression.substring(index+1).replace(")", "").replace(")", "");// Whatever the value mentioned within brackets, treated as one argument by default.
		if(methodArgument.startsWith("Step:")) // But that argument value can also be step expression. Handled this possibility as well.
			methodArgument = getValueFromParamReusedMapUsingStepExpression(methodArgument); //It will return the value from re usable params, if argument is actually a step expression.
		else if(methodArgument.contains("("))
		{//even the argument can be a sub method expression. By default method expression will be open & close braces, so condition checked based on that.
			index = methodArgument.indexOf("(");
			methodArgument = getValueFromReflectionMethod(methodArgument.substring(0, index).trim(), methodArgument.substring(index+1).trim());
		}// So if the method expression is there, then this above called method executes and returns the value.
		return getValueFromReflectionMethod(methodName, methodArgument);
	}
	
	
	/**
	 * @category Test_Executor
	 * @return It will return the response in the form of Response object
	 * @throws Exception
	 * @author mshellagi
	 */
	public static Response getResponseOfAPI() throws Exception{
		logger.info("Started executing getResponseOfAPI");
		Map<String, String> reqHeadersMap = getRequestHeadersInMap(); // Getting all the request headers into one map.
		Map<String, String> reqParametersMap = getRequestParametersInMap(); // Getting all the request parameters into one map.
		outputfileMap.put("Request_Parameters", reqParametersMap); // Putting request parameters into output map
		Map<String, String> reqHeadersMap1 = getRequestHeadersWithReusableValues(reqHeadersMap);
		Map<String, String> reqParametersMap1 = getRequestParamsWithReusableValues(reqParametersMap); // Will evaluate and get a map with parameters contained expressions to get re usable values.
		logger.info("getRequestParamsWithReusableValues are : " + reqParametersMap1);
		String reqLink = ReadExternalFiles.getAPI_URL(testData.get("API_Component")); // It will return the url from api_url properties file along with server end point in a proper url mode, If either is not existing according to given component, then it will return null in that part if the url.
		logger.info("reqLink is -- " + reqLink);
		if(reqLink.endsWith("null"))
		{
			failureMsg = "Given Api component is not defined with path in the Api_Path.properties file\n";
			logger.error("Given Api component is not defined with path in the Api_Path.properties file\n");
			throw new NullPointerException("Given Api component is not defined with path in the Api_Path.properties file\n");
		}
		String requestLink = reqLink + getAdditionalPath(testData.get("Additional_Path")); // If there is any dynamic value to be placed in expressions, then it does and comes with actual values to hit the request.
		logger.info("requestLink is -- " + requestLink);
		outputfileMap.put("Request_Link", requestLink); // The evaluated url is stored in the output map
		logger.info("Found requestLink is : " + requestLink);
		String jsobBodyWithDynamicValues = getJsonBodyWithReusableParameters().trim(); // If there is any dynamic value to be placed in expressions, then it does replacement and comes with actual values to pass as complete json body along with input request.
		logger.info("jsobBodyWithDynamicValues is : \n" + jsobBodyWithDynamicValues);
		outputfileMap.put("Request_JsonBody", jsobBodyWithDynamicValues);
		Response response = getServerResponse(reqHeadersMap1, reqParametersMap1, requestLink, jsobBodyWithDynamicValues); //By passing all required inputs, will ge the actual Response from server.
		logger.info("Response data of the request is : \n " + response);
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
				reqParametersMap.put(key, value);
			}
			else if(value.startsWith("#Method:"))
			{
				value = //getValueUsingReflectionMethods(data, value, params_reused);
						getValueFromMethodExpressions_WithNested_Variables_Concates(value);
				reqParametersMap.put(key, value);
			}
		}
		return reqParametersMap;
	}
	private static Map<String, String> getRequestHeadersWithReusableValues(Map<String, String> reqHeadersMap
			) throws Exception
	{
		String value = "";
		for(String key: reqHeadersMap.keySet())
		{
			value = reqHeadersMap.get(key);
			
			String fLine = "", subString="", reusableVal="";
			String[] jsonBodyArr = value.split("\n"); // Just splitting with new line to make dynamic values replacement in better way, if json body contains expressions to replace.
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
					//If the expression is of step related, it will get only the expression from complete line of string, and pass in below line to get respective value from re usbale params variable.
					reusableVal = getReusableParam(subString);
					fLine = fLine.replace("#Step:"+subString+"#", reusableVal); // Retrieved value is going to replaced in json body expression exactly.
					value = value.replace("#Step:"+subString+"#", reusableVal);
				}
				if(fLine.contains("#Method:"))
				{
					index = fLine.indexOf("#Method:") + 8;
					lastIndex= fLine.indexOf("#", index);
					subString = fLine.substring(index, lastIndex);
					//If the expression is of method related, it will get value by executing below line properly, by passing retrieved expression only.
					reusableVal = getValueFromMethodExpressions_WithNested_Variables_Concates(subString);
					fLine = fLine.replace("#Method:"+subString+"#", reusableVal); // Retrieved value is replaced exactly in the place of expression.
					value = value.replace("#Method:"+subString+"#", reusableVal);
				}
				if(fLine.contains("#Step:") || fLine.contains("#Method:"))
						i=i-1;	 // Same line of string may have more than one expression. So again the same line looped back by decrementing. If containing once only, then this decrementing will not be executed by control any how
				i++;
			}
			
			reqHeadersMap.put(key, value);
			
		}
		return reqHeadersMap;
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
		logger.info("Requesting to the server");
		RequestSpecification reqSpec = given().headers(reqHeadersMap).parameters(reqParametersMap).body(jsonBodyWithDynamicValues);
		Response response = null;
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
		logger.info("Response duration is : " + cMilliSeconds);
		return response;
	}
	private static String getValueFromParamReusedMapUsingStepExpression(String stepExpression) throws Exception
	{
		if(stepExpression.startsWith("#Step:"))
			stepExpression = stepExpression.replace("#Step:", "").trim();
		else if(stepExpression.startsWith("Step:"))
			stepExpression = stepExpression.replace("Step:", "").trim();
		else{
			logger.error("stepExpression passed in the getValueFromReusableMapUsingStepExpression method is invalid, it is not starting with Step: , and passed expression is - " + stepExpression);
			throw new Exception("stepExpression passed in the getValueFromReusableMapUsingStepExpression method is invalid, it is not starting with Step: , and passed expression is - " + stepExpression);
		}//return params_reused.get(stepExpression.replace("#", "").trim());
		return getReusableParam(stepExpression.replace("#", "").trim());
	}
	private static String getValueFromReflectionMethod(String methodName, String methodArgument) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		// This method will by default accepts one method name and one mehid argument as arguments.
		// Method name is going to be identified in the ReflectionMethods class, If not found, gives a excep.
		// Found method name is going to be executed by passing methodArgument.
		// By default method argument will be string. If we want method argument to be some other data type, then in respective method implementation only, we have to convert using wrapper classes.
		String methodDefinition = "Method body";
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
			logger.error("There is no reflection method available with the name - > " + methodName);
			failureMsg = "There is no reflection method available with the name - > " + methodName;
			throw new NoSuchMethodException("There is no reflection method available with the name - > " + methodName+"\n"+e.getMessage());
		}catch(SecurityException e){
			//System.out.println("Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class");
			logger.error("Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class");
			failureMsg = "Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class";
			throw new SecurityException("Passed reflection method name- " + methodName + " is giving an SecurityException, check method implemenaion in Reflection class");
		}catch(IllegalAccessException e){
			//System.out.println("Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class");
			logger.error("Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class");
			failureMsg = "Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class";
			throw new IllegalAccessException("Passed reflection method name- " + methodName + " is giving an IllegalAccessException, check method implemenaion in Reflection class");
		}catch(IllegalArgumentException e){
			//System.out.println("Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument);
			logger.error("Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument);
			failureMsg = "Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument;
			throw new IllegalAccessException("Passed reflection method name- " + methodName + " is giving an IllegalArgumentException, check passed argument - " + methodArgument);
		}catch(InvocationTargetException e){
			//System.out.println("Passed reflection method name- '" + methodName + "' with argument '"+ methodArgument +"' is giving an InvocationTargetException, check passed method and arguments, and method implemenaion in Reflection class");
			logger.error("Passed reflection method name- '" + methodName + "' with argument '"+ methodArgument +"' is giving an InvocationTargetException, check passed method and arguments, and method implemenaion in Reflection class");
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
	
	
	/**
	 * @category Test_Executor
	 * @return map of non expected keys and values.
	 * @throws Exception
	 */
	public static Map<String, String> getNonExpectedValues1() throws Exception
	{
		String expectedValues = testData.get("NonExpected_Values").trim();
		// Even if more than one value is written in non-expected cell, a complete cell value will be stored as string only. Hence taking that to a variable.
		Map<String, String> expectedDataMap = new LinkedHashMap<String, String>();
		String[] expParams = expectedValues.split("\n");
		// If more than one value expecting, that would be mentioned with new line character in the excel cell. Hence splitting using \n
		String expectedVal = ""; int startIndex = 0;
		
		for(int i=0; i<expParams.length; i++)
		{
			expectedVal = expParams[i];
			if(expectedVal.length()>0)
			{
				if(expectedVal.startsWith("$"))
					startIndex = 2;
				if(! expectedVal.contains("=")){
					logger.error("Non Expected_Values are not defined appropriately. Given values are : " + expectedVal);
					throw new Exception("Non Expected_Values are not defined appropriately. Given values are : " + expectedVal);
				}else
					expectedDataMap.put(expectedVal.substring(startIndex, expectedVal.indexOf("=")).trim(), expectedVal.substring(expectedVal.indexOf("=")+1).trim());
			}
			startIndex = 0;
		}
		return expectedDataMap;
	}
	
	
	/**
	 * @category Test_Executor
	 * @return map of expected keys and values.
	 * @throws Exception
	 */
	public static Map<String, String> getExpectedValues1() throws Exception
	{
		String expectedValues = testData.get("Expected_Values").trim();
		// Even if more than one value is expected in cell, a single cell value will be stored as string only. Hence taking that to a variable.
		Map<String, String> expectedDataMap = new LinkedHashMap<String, String>();
		String[] expParams = expectedValues.split("\n");
		String expectedVal = ""; int startIndex = 0;
		// If more than one value expecting, that would be mentioned with new line character in the excel cell. Hence splitting using \n
		for(int i=0; i<expParams.length; i++)
		{
			expectedVal = expParams[i];
			if(expectedVal.length()>0)
			{ // Expected values will usually are nothing but jPath's which starts with $.
				if(expectedVal.startsWith("$"))
					startIndex = 2;
				if(! expectedVal.contains("=")){ //It should have key=value, hence equals symbol is not there, treating that as wrong input.
					logger.error("Expected_Values are not defined appropriately. and the given expected value is : " + expectedVal);
					throw new Exception("Expected_Values are not defined appropriately. and the given expected value is : " + expectedVal);
				}else
					expectedDataMap.put(expectedVal.substring(startIndex, expectedVal.indexOf("=")).trim(), expectedVal.substring(expectedVal.indexOf("=")+1).trim());
			}
			startIndex = 0;
		}
		return expectedDataMap;
	}
	
	
	private static String getExpectedValueWithReusableDataFromRegEx(String regExString) throws Exception
	{
		// If the reg ex expression is having any expressions related to step or method, those expressions are evaluated and retrieved values accordingly.
		// If any expression is contained and the found value against that expression is going to be replaced and returned back.
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
					reusableVal = getValueFromMethodExpressions_WithNested_Variables_Concates(subString);
					regExString = regExString.replace("#Method:"+subString+"#", reusableVal);
				}
				if(regExString.contains("#Step:") || regExString.contains("#Method:"))
					i=i-1;
				i++;
			}
		return regExString;	
	}
	/**
	 * @category Test_Executor
	 * @param response1 - Actual response returned from server request which object of Response.
	 * @param expectedDataMap - List of key and values are expected in the response, should be passed as one map. 
	 * @param nonExpectedDataMap - List of key and values which are not expected in the response, should be passed as one map. 
	 * @throws Exception
	 */
	public static void validateResponseData1(Response response1, Map<String, String> expectedDataMap, Map<String, String> nonExpectedDataMap) throws Exception
	{
		Response newResponse = null;
		String responseString = response1.getBody().asString();
		int actReponseCode =  response1.getStatusCode();
		String expResponseCode = testData.get("Expected_HTTP_Code").trim();// Expected response http code is fetched from testdata object only, and validated with actual response value.
		int matcher = 0; 
		if(expResponseCode.length()>0)
			if(! expResponseCode.equals(actReponseCode+"")){
				failureMsg = "Expected and actual HTTP response codes are not matching : Expected is - " + expResponseCode + " , and Actual is - " +actReponseCode ;
				matcher = 1;
			}
		// failureMsg variable is used, if at all validation failure is existing, based on captured message in failureMsg variable, further test status is verified in future code.
		if(matcher == 0)
		{// Some times, even response is json content, the returned object may be text. hence converting in such cases below.
			if(testData.get("Response_Type").trim().equalsIgnoreCase("text"))
				newResponse = new ResponseBuilder().clone(response1).setBody(responseString).setContentType(ContentType.JSON).build();
			else//Only if response type mentioned as text in data, then the conversion is done, else by default it treats as json object. 
				newResponse = response1;
			validateExpectedData(newResponse, expectedDataMap); // All the expected data is validated.
			validateNonExpectedData(newResponse, nonExpectedDataMap);// All the non expected data is validated.
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
		for(int i = 0; i<reusableNameAndValues.length; i++)
		{
			if(!reusableNameAndValues[i].startsWith("#Method:"))
			{
				name = reusableNameAndValues[i].split("=")[0].trim();
				value = reusableNameAndValues[i].split("=")[1].trim();
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
		// newResponse is nothing but the response retrieved from requested server output. 
		//expectedDataMap is a map with key value pair like - jpath = expected_data_value
		List<Object> listOfParameter = null;
		String expValue = "", actValue = "";
		for(String str : expectedDataMap.keySet())
		{
			expValue = expectedDataMap.get(str); 
			if(expValue.startsWith("\n")) //In case, data input given with new line like - jpath = \nexpected_data_value
				expValue = expValue.substring(1);
			
			if(!str.contains("[*]"))
			{// str is jpath which may have [*], it means, such expressions find list of string. So first without that, only single string value is validated in this condition. 
				try{
				actValue = newResponse.then().contentType(ContentType.JSON).extract().path(str).toString(); // from response, based on str(jsonpath) value is fetched out.
				}catch(IllegalArgumentException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
					break;
				}catch(NullPointerException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
					break;
				}
				//expValue may be a straight forward expected value, or it may expressions or regular expression as well.
				// Code written below for all conditions.
				String val = "";
				if(expValue.startsWith("#RegEx:"))
				{
					expValue = getExpectedValueWithReusableDataFromRegEx(expValue); // Because, reg ex expressions may also have step/method expressions in it.
					if(!(TestUtils.matchStringWithRegEx(actValue, expValue.replace("#RegEx:", "").trim())))
						failureMsg = failureMsg + "\n\nExpected RegEx and Actual values are not matching: Expected Reg ex is - " + expValue.replace("#RegEx:", "").trim() + 
						", and actual value - " + actValue; // It will match the expected reg ex value with actual value.
				}
				else if(expValue.startsWith("#Step:") || expValue.startsWith("#Method:")){
					val = getExpectedValueFromReusableList(testData, expValue); // If the method or step is existing in place expected value, accordingly the value is going to be replaced in that place.
					if( ! val.equalsIgnoreCase(actValue))
						failureMsg = failureMsg + "\n\nExpected and Actual values of  '" + str + "' are not matching: expcted is = '" + expValue + " - " + val + "' ; Actual is = '" + actValue + "'.";
				}
				else
					if( ! expValue.equalsIgnoreCase(actValue))
						failureMsg = failureMsg + "\n\nExpected and Actual values of  '" + str + "' are not matching: expcted is = '" + expValue + "' ; Actual is = '" + actValue + "'.";
			}
			else
			{// If the json path is containing [*] which require to check the expected value among list of string values.
				try{
					listOfParameter= newResponse.then().contentType(ContentType.JSON).extract().path(str.replace("[*]", ""));
					}catch(IllegalArgumentException iae){
						failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
						break;
					}catch(NullPointerException iae){
						failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
						break;
					}
				String val=""; boolean contains = false;
				for(int i=0; i<listOfParameter.size(); i++){
					// listOfParamaters may have more number of strings, and if the expected value is existing in any one index, the validation treated as passed.
					// If not at all present, the failurMsg is going to be captured.
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
		// newResponse is nothing but the response retrieved from requested server output. 
		//nonExpectedDataMap is a map with key value pair like - jpath = non_expected_data_value
		List<Object> listOfParameter = null;
		String nonExpValue = "", actValue = "";
		for(String str : nonExpectedDataMap.keySet())
		{
			nonExpValue = nonExpectedDataMap.get(str);
			if(nonExpValue.startsWith("\n"))//In case, data input given with new line like - jpath = \nnon_expected_data_value
				nonExpValue = nonExpValue.substring(1);
			
			if(!str.contains("[*]"))
			{// str is jpath which may have [*], it means, such expressions find list of string. So first without that, only single string value is validated in this condition. 
				try{
				actValue = newResponse.then().contentType(ContentType.JSON).extract().path(str).toString();// from response, based on str(jsonpath) value is fetched out.
				}catch(IllegalArgumentException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
					break;
				}catch(NullPointerException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
					break;
				}
				//non expValue may be a straight forward value, or it may expressions or regular expression as well.
				// Code written below for all conditions.
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
			{// If the json path is containing [*] which require to check the expected value among list of string values.
				try{
				listOfParameter= newResponse.then().contentType(ContentType.JSON).extract().path(str.replace("[*]", ""));
				}catch(IllegalArgumentException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str;
					break;
				}catch(NullPointerException iae){
					failureMsg = failureMsg + "Actual response is not containing a JsonPath of - " +  str + " OR been defined with null/empty value";
					break;
				}
				String val=""; boolean contains = false;
				for(int i=0; i<listOfParameter.size(); i++){
					// listOfParamaters may have more number of strings, and if the non expected value is existing in any one index, the validation treated as failed & failurMsg is going to be captured
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
		logger.info("Started dataProvider method for table : " + testDataTableName );
		if(!(ReadExternalFiles.isTCRunmodeSet(testDataSheetName, testDataTableName))){
			storeSkipResultsIntoExcelFiles(testDataSheetName, testDataTableName); // Stores skipped result into out put excel files
			logger.error(testDataTableName + " runmode is set to No/null in the RunModes sheet");
			throw new SkipException(testDataTableName + " runmode is set to No/null in the RunModes sheet");
		}
		logger.info("Going to retrieve table data");
		Object[][] retObjArr=ReadExternalFiles.getTableData(testDataSheetName,testDataTableName);
		logger.info("Retrieved table data, and converting object to map, along woth column headers retrieval");
		Object[][] data = TestUtils.Obj2Map(retObjArr, ReadExternalFiles.getColumnHeaders(testDataSheetName, testDataTableName));
		//If there is any data of Request_JsonBody column is having the reference of child file table data, then such table named data will be fetched into ArrayList
		ArrayList<String> namesList= getJsonChildTableNameList(retObjArr, ReadExternalFiles.getColumnHeaders(testDataSheetName, testDataTableName));
		logger.info("Retrieved getJsonChildTableNameList is : " + namesList);
		for(int i = 0; i<namesList.size(); i++)
		{
			Object[][] jsonDataObj=ReadExternalFiles.getJsonData(testDataSheetName,namesList.get(i)); // Based on each row's reference value, json data is fetched out.
			prepareJsonBody1(testDataSheetName, namesList.get(i), jsonDataObj, ReadExternalFiles.getJsonAttributes(testDataSheetName, namesList.get(i).split(":")[0]));
		}
		logger.info("Prepared ipJsonMap is : " + ipJsonMap);
		DataFilesValidator.validateInputData1(data, testDataSheetName, testDataTableName);
		params_reused.clear();
		logger.info("Executed dataProvider method for table : " + testDataTableName );
		return data;
	}
	
	/**
	 * @category Test_Executor
	 * @param tcName - Test case name should be passed as argument over here.
	 * @throws Exception
	 */
	public static void doExecuteRequest(String tcName) throws Exception
	{
		logger.info("Started doExecuteRequest execution with step number : " + testData.get("Step_No"));
		logger.info("Retrieved step data is : " + testData);
		Response actualResponse = getResponseOfAPI(); // It will return the response of request.
		String actualResponseBody = actualResponse.getBody().asString();
		outputfileMap.put("Actual_Response_Body", actualResponseBody); // Storing the actual response to output map.
		appendResponseIntoTextFile(actualResponseBody, tcName); // Appending the response into text file
		Map<String, String> expectedDataMap = getExpectedValues1(); // Getting all the expected data into separate map, by replacing expressions with dynamic values
		logger.info("expectedDataMap is :::" + expectedDataMap);
		Map<String, String> nonExpectedDataMap = getNonExpectedValues1();  // Getting all the non expected data into separate map, by replacing expressions with dynamic values
		logger.info("nonExpectedDataMap is :::" + nonExpectedDataMap);
		validateResponseData1(actualResponse, expectedDataMap, nonExpectedDataMap); // validating response with all expected values.
		logger.info("failureMessage is " + failureMsg);
		outputfileMap.put("Reusable_Parameters", params_reused.toString());
		logger.info("Executed doExecuteRequest method");
	}
	
	
	/**
	 * @category Test_Executor
	 * @param tcName - Test case name should be passed as argument over here.
	 * @return - It will if there is an dependent method is passed/failed, based on that it will return true/false respectively.
	 * @throws ExecutionException
	 */
	public static boolean executeIfDependentStepPassed(String tcName) throws ExecutionException
	{
		boolean isPassed = true;
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
						failureMsg = "The earlier dependent step '" + step+"' has been failed";
						logger.error(failureMsg);
						//throw new ExecutionException("The earlier dependent step '" + step+"' has been failed", null);
						isPassed =  false;
					}
				}
			}
		}
		return isPassed ;
	}
	
	public static void storeResultsIntoExcelFiles(String testDataSheetName, String testDataTableName, String testStatus) throws Exception
	{
		if(!(testStatus.length()>1)){
			testStatus = "Error";
			if(!(failureMsg.length()>1))
				failureMsg="Execution interrupted because of server improper response, or improper data execution. Check data and re run";
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
		// When the complete test case is skipped, the skipped information as to be stored in the excel.
		// In order to get store the skipped test cases completely in both the excel result files, the below complete code is written
			outputfileMap.put("TestCase Name", testDataTableName);
			outputfileMap.put("Test_Status", "Skipped");
			summaryResultFileMap.put("TestCase Name", testDataTableName);
			summaryResultFileMap.put("Test_Status", "Skipped");
			String sMsg = "This test case run mode has been set to No, hence the test case is completely skipped";
			outputfileMap.put("Failure_Message", sMsg);
			summaryResultFileMap.put("Failure_Message", sMsg);
			
			// By default, all columns are going to be filled in the out put excel files.
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
			// Since above called method accepts arguments in he form of string of array, used values[] and summaryValues[] in this method
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
	            	 if(!(TestUtils.matchStringWithRegEx(val, ".*__[a-z]") || TestUtils.matchStringWithRegEx(val, ".*__[a-z][a-z]"))){
	            		 logger.error("Given attribute is not ending with variable type, and the given attribute is : " + attributes[k]);
	            		 throw new Exception ("Given attribute is not ending with variable type, and the given attribute is : " + attributes[k]);
	             	}if(val.endsWith("__s"))
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
    
    /**
     * @category Config_Reusable
     * @param testDataSheetName - The child sheet name from which we want to get json data.
     * @param jsonIPId - Each row of json data will have Input_Id and from which row, we want to get, that should be passed as argument over here.
     * @param jsonParamsValues - Child json table values fetched in multi array.
     * @param attributes - Child json table attributes which are fetched in String of array
     * @throws Exception
     */
public static void prepareJsonBody1(String testDataSheetName, String jsonIPId, Object[][] jsonParamsValues, String[] attributes) throws Exception {
    	
        logger.info("Started executing prepareJsonBody for jsonIPId - " + jsonIPId);
        // Usually the jsonIPId will be like - Users:IP_001
        // Mentioned start end points in the cell will be delimited by semicolon.
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
		//If there is any data of Request_JsonBody column is having the reference of child file data, then such table named data will be fetched into ArrayList
        ArrayList<String> tableNames=new ArrayList<String>();
	    try{
	        for (int i=0;i<retObjArr.length;i++)
	        	for(int k=0;k<columnHdrs.length;k++){
		             // Running through column headers and identifying "Request_JsonBody"
	        		if(columnHdrs[k].equalsIgnoreCase("Request_JsonBody"))
	        		{//If any data from any step, having ":IP_", it means that, there is a reference to child excel data. So that reference will be stored into tableNames ArrayList
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
		logger.info("moduleClassExtentMap in after suite is : " + moduleClassExtentMap);
		for(String temp : moduleClassExtentMap.keySet()){
			extent.endTest(moduleClassExtentMap.get(temp));
			
			if(moduleStatus.get(temp).equalsIgnoreCase("Warn"))
				moduleClassExtentMap.get(temp).log(LogStatus.WARNING, "There are tests in the module which got failed / error occured");
			else if(moduleStatus.get(temp).equalsIgnoreCase("Skipped"))
				moduleClassExtentMap.get(temp).log(LogStatus.SKIP, "The module has been skipped since the run mode set to NO");
			//else if(moduleStatus.get(temp).equalsIgnoreCase(""))
			else	
			moduleClassExtentMap.get(temp).log(LogStatus.PASS, "All tests in this module are passed successfully");
		}
		
		logger.info("Execution started in @AfterSuite");
		//extent.endTest(null);
		extent.flush();
		//extent.close();
		//if(ReadExternalFiles.getConfigFileValue("SortReports").contains("Y") || ReadExternalFiles.getConfigFileValue("SortReports").contains("Y"))
		//	PostBuild.main(null);
		logger.info("Execution end from @AfterSuite");
	}
	
	
}

