package com.automation.tests.prov;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.automation.controller.ApiConstants;
import com.automation.controller.RestExecutor;
import com.automation.controller.RestResponse;
import com.automation.util.DataLoader;
import com.automation.util.ResponseUtil;

/**
 * @author Raghotham.Madhusudan
 */

public class TestAgentProfile {
	
	public static final Logger log = LogManager.getLogger(TestAgentProfile.class);
	Map<String, String> parametersMap = new HashMap<String, String>();
	private DataLoader dataLoader = DataLoader.getInstance();
	RestExecutor restExecutor;
	RestResponse restResponse;
	ResponseUtil responseUtil;
	String jsonName = "";
	String id = "";
	String response = "";

	@Test(priority = 1)
	public void postTenantLevelAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String testData = dataLoader.readTestData(jsonName);
		String name = dataLoader.getName(jsonName);
		String payload = testData.replaceAll(name, name + System.currentTimeMillis());
		log.info("payload is : {}", payload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPost(payload);
		log.info("rest response for post is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		id = responseUtil.getAnalyserIdFromHref();
	//	Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
	}

	@Test(priority = 2)
	public void getTenantLevelAgentProfile() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAgentProfile() + File.separator + id, parametersMap);
		response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getAuxillaryDataType(), dataLoader.getAssertions().getExpectedAuxillaryDataTypeForUserData());
		Assert.assertEquals(responseUtil.getType(), dataLoader.getAssertions().getExpectedTypeForAgentProfile());
	}

	@Test(priority = 3)
	public void putTenantLevelAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, id);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
	}

	@Test(priority = 4)
	public void softDeleteTenantLevelAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, id);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAgentProfile() + File.separator + id, parametersMap);
		response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getStatus(), dataLoader.getAssertions().getExpectedStatusAfterSoftDelete());
	}
	
	@Test(priority = 5)
	public void postSiteLevelAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String testData = dataLoader.readTestData(jsonName);
		String name = dataLoader.getName(jsonName);
		String payload = testData.replaceAll(name, name + System.currentTimeMillis());
		log.info("payload is : {}", payload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPost(payload);
		log.info("rest response for post is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		id = responseUtil.getAnalyserIdFromHref();
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
	}

	@Test(priority = 6)
	public void getSiteLevelAgentProfile() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAgentProfile() + File.separator + id, parametersMap);
		response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getAuxillaryDataType(), dataLoader.getAssertions().getExpectedAuxillaryDataTypeForUserData());
		Assert.assertEquals(responseUtil.getType(), dataLoader.getAssertions().getExpectedTypeForAgentProfile());
	}

	@Test(priority = 7)
	public void putSiteLevelAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, id);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
	}

	@Test(priority = 8)
	public void softDeleteSiteLevelAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, id);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAgentProfile() + File.separator + id, parametersMap);
		response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getStatus(), dataLoader.getAssertions().getExpectedStatusAfterSoftDelete());
	}
	
	@Test(priority = 9)
	public void postSpecificPermissionsAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String testData = dataLoader.readTestData(jsonName);
		String name = dataLoader.getName(jsonName);
		String payload = testData.replaceAll(name, name + System.currentTimeMillis());
		log.info("payload is : {}", payload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPost(payload);
		log.info("rest response for post is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		id = responseUtil.getAnalyserIdFromHref();
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
	}

	@Test(priority = 10)
	public void getSpecificPermissionsAgentProfile() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAgentProfile() + File.separator + id, parametersMap);
		response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getAuxillaryDataType(), dataLoader.getAssertions().getExpectedAuxillaryDataTypeForUserData());
		Assert.assertEquals(responseUtil.getType(), dataLoader.getAssertions().getExpectedTypeForAgentProfile());
	}

	@Test(priority = 11)
	public void putSpecificPermissionsAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, id);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
	}

	@Test(priority = 12)
	public void softDeleteSpecificPermissionsAgentProfile() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, id);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAgentProfile(), parametersMap);
		response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAgentProfile() + File.separator + id);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataUserData,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAgentProfile() + File.separator + id, parametersMap);
		response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getStatus(), dataLoader.getAssertions().getExpectedStatusAfterSoftDelete());
	}
}
