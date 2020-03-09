package com.automation.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class HeaderDataLoader {
	private String baseDir;

	public void readHeaderPropertiesData() {

		try {
			String datacenter = System.getProperty("datacenter");
			Properties pro = new Properties();
			System.setProperty("baseDir", System.getProperty("user.dir"));
			baseDir = System.getProperty("baseDir");

			FileInputStream fis = new FileInputStream(
					new File(baseDir + File.separator + "config/" + datacenter + ".properties"));
			pro.load(fis);
			String fromHeader = pro.getProperty("fromHeader");
			String authorizationHeader = pro.getProperty("authorizationHeader");
			String contentTypeHeader = pro.getProperty("contentTypeHeader");
			String from = pro.getProperty("from");
			String authorization = pro.getProperty("authorization");
			String contentType = pro.getProperty("contentType");
			String tenantId = pro.getProperty("tenantId");
			String awsUrl = pro.getProperty("awsUrl");
			File datajsonfile = new File(baseDir + "/config/config.JSON");
			String rootstring = FileUtils.readFileToString(datajsonfile);
			JSONObject root = new JSONObject(rootstring);
			root.getJSONObject("headerInfo").put("fromHeader", fromHeader);
			root.getJSONObject("headerInfo").put("awsUrl", awsUrl);
			root.getJSONObject("headerInfo").put("authorizationHeader", authorizationHeader);
			root.getJSONObject("headerInfo").put("contentTypeHeader", contentTypeHeader);
			root.getJSONObject("headerInfo").put("from", from);
			root.getJSONObject("headerInfo").put("authorization", authorization);
			root.getJSONObject("headerInfo").put("contentType", contentType);
			root.getJSONObject("headerInfo").put("tenantId", tenantId);
			FileWriter fw = new FileWriter(baseDir + "/config/config.JSON");
			fw.write(root.toString());
			fw.close();

		} catch (Exception e) {
		}
	}
}