package com.automation.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automation.controller.ApiConstants;
import com.automation.pojo.Assertions;
import com.automation.pojo.AuxillaryData;
import com.automation.pojo.ConfigData;
import com.automation.pojo.HeaderInfo;
import com.automation.pojo.UrlsPath;
import com.automation.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * @author Raghotham.Madhusudan
 */

public class DataLoader {
	
	private Logger logger = LogManager.getLogger(DataLoader.class.getName());
	private static DataLoader instance;
	private HeaderInfo headerInfoData;
	private UrlsPath urlsPathData;
	private Assertions assertions;
	private String baseDir;
	HeaderDataLoader headerDataLoader;
	
	private DataLoader() {
		try {
			beforeSuite();
		} catch (Exception e) {
			logger.error("Exception while instantiating DataLoader", e);
		}
	}

	public static DataLoader getInstance() {
		if (instance == null) {
			instance = new DataLoader();
		}
		return instance;
	}

	public void beforeSuite() throws IOException, Exception {

		logger.info("Inside before suite");
		readConfigData();
	}
	
	public void readConfigData() throws Exception {
		headerDataLoader = new HeaderDataLoader();
		headerDataLoader.readHeaderPropertiesData();
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		ConfigData configData = JsonUtil.readFile(baseDir + "/config/config.JSON" , ConfigData.class);
		headerInfoData = configData.getHeaderInfo();
		urlsPathData = configData.getUrlsPath();
		assertions = configData.getAssertions();
	}
	
	public String readTestData(String jsonName) throws Exception {
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		Map<String, Object> jsonMap = JacksonJsonHelper.convert(data);
		String finalJson = JsonUtil.convertToString(jsonMap);
		List<String> payload = new ArrayList<String>();
		payload.add(finalJson);
		String payloadJson = payload.toString();
		return payloadJson;
	}
	
	public String readTestDataForUser(String jsonName) throws Exception {
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		Map<String, Object> jsonMap = JacksonJsonHelper.convert(data);
		String finalJson = JsonUtil.convertToString(jsonMap);
		return finalJson;
	}
	
	public String getName(String jsonName) throws Exception {
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		String name = (String) data.getAttributes().get(ApiConstants.name);
		return name;
	}
	
	public String getThirdPartyProfileId(String jsonName) throws Exception {
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		String thirdPartyProfileId = (String) data.getAttributes().get(ApiConstants.thirdPartyProfileId);
		return thirdPartyProfileId;
	}
	
	public String getLogin(String jsonName) throws Exception{
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		String login = (String ) data.getAttributes().get(ApiConstants.login);
		return login;
	}
	
	public String getId(String jsonName) throws Exception {
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		String id =  data.getId();
		return id;
	}
	
	public String getAddressBookOrOutdialAniId(String jsonName) throws Exception {
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		String AddressBookId = (String ) data.getAttributes().get(ApiConstants.addressBookId);
		return AddressBookId;
	}
	
	public String getSkillId(String jsonName) throws Exception {
		
		System.setProperty("baseDir", System.getProperty("user.dir"));
		baseDir = System.getProperty("baseDir");
		Map<String, AuxillaryData> auxillaryData = JsonUtil.readTestAuxData(baseDir + ApiConstants.resourcePath + "provisioning.JSON",
				new TypeReference<Map<String, AuxillaryData>>() {
				});
		AuxillaryData data = (AuxillaryData) auxillaryData.get(jsonName);
		String skillId = (String) data.getAttributes().get(ApiConstants.skillId);
		return skillId;
	}
	
	public HeaderInfo getHeaderInfo() {
		
		return headerInfoData;
	}
	
	public UrlsPath getUrlsPath() {
		
		return urlsPathData;
	}
	
	public Assertions getAssertions() {
		
		return assertions;
	}
}
