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

public class TestAddressBook {
	
	public static final Logger log = LogManager.getLogger(TestAddressBook.class);
	Map<String, String> parametersMap = new HashMap<String, String>();
	private DataLoader dataLoader = DataLoader.getInstance();
	RestExecutor restExecutor;
	ResponseUtil responseUtil;
	RestResponse restResponse;
	String jsonName = "";
	String addressBookId = "";
	String addressBookListEntryId = "";

	@Test(priority = 1)
	public void postTenantLevelAddressBook() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String testData = dataLoader.readTestData(jsonName);
		String name = dataLoader.getName(jsonName);
		String payload = testData.replaceAll(name, name + System.currentTimeMillis());
		log.info("payload is : {}", payload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBook(), parametersMap);
		String response = restExecutor.doPost(payload);
		log.info("rest response for post is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		addressBookId = responseUtil.getAnalyserIdFromHref();
		//Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBook() + File.separator + addressBookId);
	}
	
	@Test(priority = 2)
	public void postTenantLevelAddressBookListEntry() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String testData = dataLoader.readTestData(jsonName);
		String idFromJson = dataLoader.getAddressBookOrOutdialAniId(jsonName);
		String payload = testData.replaceAll(idFromJson, addressBookId);
		log.info("payload is : {}", payload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBookListEntry(), parametersMap);
		String response = restExecutor.doPost(payload);
		log.info("rest response for post is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		addressBookListEntryId = responseUtil.getAnalyserIdFromHref();
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBookListEntry() + File.separator + addressBookListEntryId);
	}

	@Test(priority = 3)
	public void getTenantLevelAddressBook() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBook() + File.separator + addressBookId, parametersMap);
		String response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getAuxillaryDataType(), dataLoader.getAssertions().getExpectedAuxillaryDataType());
		Assert.assertEquals(responseUtil.getType(), dataLoader.getAssertions().getExpectedTypeForAddressBook());
	}
	
	@Test(priority = 4)
	public void getTenantLevelAddressBookListEntry() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBookListEntry() + File.separator + addressBookListEntryId, parametersMap);
		String response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getAuxillaryDataType(), dataLoader.getAssertions().getExpectedAuxillaryDataType());
		Assert.assertEquals(responseUtil.getType(), dataLoader.getAssertions().getExpectedTypeForAddressBookListEntry());
	}

	@Test(priority = 5)
	public void putTenantLevelAddressBook() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, addressBookId);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBook(), parametersMap);
		String response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBook() + File.separator + addressBookId);
	}
	
	@Test(priority = 6)
	public void putTenantLevelAddressBookListEntry() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, addressBookListEntryId);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBookListEntry(), parametersMap);
		String response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBookListEntry() + File.separator + addressBookListEntryId);
	}
	
	@Test(priority = 7)
	public void deleteTenantLevelAddressBookListEntry() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBookListEntry() + File.separator + addressBookListEntryId, parametersMap);
		String response = restExecutor.doDelete();
		log.info("rest response for delete is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedDeleteReason());
		String getResponse = restExecutor.doGet();
		log.info("rest response for get is : {}", getResponse);
		restResponse = new RestResponse();
		restResponse.setResponseBody(getResponse);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedInvalidCodeForRecordNotFound());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedRecordNotFoundMsg());
	}

	@Test(priority = 8)
	public void deleteTenantLevelAddressBook() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBook() + File.separator + addressBookId, parametersMap);
		String response = restExecutor.doDelete();
		log.info("rest response for delete is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedDeleteReason());
		String getResponse = restExecutor.doGet();
		log.info("rest response for get is : {}", getResponse);
		restResponse = new RestResponse();
		restResponse.setResponseBody(getResponse);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedInvalidCodeForRecordNotFound());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedRecordNotFoundMsg());
	}
	
	@Test(priority = 9)
	public void postSiteLevelAddressBook() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String testData = dataLoader.readTestData(jsonName);
		String name = dataLoader.getName(jsonName);
		String payload = testData.replaceAll(name, name + System.currentTimeMillis());
		log.info("payload is : {}", payload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBook(), parametersMap);
		String response = restExecutor.doPost(payload);
		log.info("rest response for post is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		addressBookId = responseUtil.getAnalyserIdFromHref();
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBook() + File.separator + addressBookId);
	}
	
	@Test(priority = 10)
	public void postSiteLevelAddressBookListEntry() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String testData = dataLoader.readTestData(jsonName);
		String idFromJson = dataLoader.getAddressBookOrOutdialAniId(jsonName);
		String payload = testData.replaceAll(idFromJson, addressBookId);
		log.info("payload is : {}", payload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBookListEntry(), parametersMap);
		String response = restExecutor.doPost(payload);
		log.info("rest response for post is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		addressBookListEntryId = responseUtil.getAnalyserIdFromHref();
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBookListEntry() + File.separator + addressBookListEntryId);
	}

	@Test(priority = 11)
	public void getSiteLevelAddressBook() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBook() + File.separator + addressBookId, parametersMap);
		String response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getAuxillaryDataType(), dataLoader.getAssertions().getExpectedAuxillaryDataType());
		Assert.assertEquals(responseUtil.getType(), dataLoader.getAssertions().getExpectedTypeForAddressBook());
	}
	
	@Test(priority = 12)
	public void getSiteLevelAddressBookListEntry() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBookListEntry() + File.separator + addressBookListEntryId, parametersMap);
		String response = restExecutor.doGet();
		log.info("rest response for get is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getAuxillaryDataType(), dataLoader.getAssertions().getExpectedAuxillaryDataType());
		Assert.assertEquals(responseUtil.getType(), dataLoader.getAssertions().getExpectedTypeForAddressBookListEntry());
	}

	@Test(priority = 13)
	public void putSiteLevelAddressBook() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, addressBookId);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBook(), parametersMap);
		String response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBook() + File.separator + addressBookId);
	}
	
	@Test(priority = 14)
	public void putSiteLevelAddressBookListEntry() throws Exception {

		jsonName = Thread.currentThread().getStackTrace()[1].getMethodName();
		log.info("test json name is : {}", jsonName);
		String payload = dataLoader.readTestData(jsonName);
		String idToBeReplaced = dataLoader.getId(jsonName);
		String idAppendedPayload = payload.replace(idToBeReplaced, addressBookListEntryId);
		log.info("payload is : {}", idAppendedPayload);
		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(), dataLoader.getUrlsPath().getAddressBookListEntry(), parametersMap);
		String response = restExecutor.doPut(idAppendedPayload);
		log.info("rest response for put is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil  = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getHref(), dataLoader.getAssertions().getExpectedHrefForAddressBookListEntry() + File.separator + addressBookListEntryId);
	}
	
	@Test(priority = 15)
	public void deleteSiteLevelAddressBookListEntry() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBookListEntry() + File.separator + addressBookListEntryId, parametersMap);
		String response = restExecutor.doDelete();
		log.info("rest response for delete is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedDeleteReason());
		String getResponse = restExecutor.doGet();
		log.info("rest response for get is : {}", getResponse);
		restResponse = new RestResponse();
		restResponse.setResponseBody(getResponse);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedInvalidCodeForRecordNotFound());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedRecordNotFoundMsg());
	}

	@Test(priority = 16)
	public void deleteSiteLevelAddressBook() throws Exception {

		restExecutor = new RestExecutor(dataLoader.getHeaderInfo().getAwsUrl(), ApiConstants.auxillaryDataResources,
				dataLoader.getHeaderInfo().getTenantId(),
				dataLoader.getUrlsPath().getAddressBook() + File.separator + addressBookId, parametersMap);
		String response = restExecutor.doDelete();
		log.info("rest response for delete is : {}", response);
		restResponse = new RestResponse();
		restResponse.setResponseBody(response);
		responseUtil = new ResponseUtil(restResponse);
		//		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedValidCode());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedDeleteReason());
		String getResponse = restExecutor.doGet();
		log.info("rest response for get is : {}", getResponse);
		restResponse = new RestResponse();
		restResponse.setResponseBody(getResponse);
		responseUtil  = new ResponseUtil(restResponse);
		Assert.assertEquals(responseUtil.getResponseCode(), dataLoader.getAssertions().getExpectedInvalidCodeForRecordNotFound());
		Assert.assertEquals(responseUtil.getReason(), dataLoader.getAssertions().getExpectedRecordNotFoundMsg());
	}
}
