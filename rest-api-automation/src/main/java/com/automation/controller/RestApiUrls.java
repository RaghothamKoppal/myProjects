package com.automation.controller;

import java.io.File;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automation.util.DataLoader;
import com.automation.util.ResponseUtil;

/**
 * @author Raghotham.Madhusudan
 */

public class RestApiUrls {
	
	public static final Logger log = LogManager.getLogger(RestApiUrls.class);

	private DataLoader dataLoader = DataLoader.getInstance();
	RestResponse restResponse;
	ResponseUtil responseUtil;


	public String constructUrl(String url) throws Exception {

		url = "http://172.18.221.4:8088/callosum/v1/connection/connect";
//		restResponse = new RestResponse();
//		String requestUrl = null;
//		switch (entityType) {
//
//		case ApiConstants.auxillaryDataResources:
//			requestUrl = awsUrl + dataLoader.getUrlsPath().getAuxDataResources() + File.separator + path;
//			break;
//			
//		case ApiConstants.apiSecurity:
//			requestUrl = awsUrl + dataLoader.getUrlsPath().getApiSecurity() + File.separator + path;
//			break;
//			
//		case ApiConstants.auxillaryDataUserData:
//			requestUrl = awsUrl + dataLoader.getUrlsPath().getAuxDataUserData() + File.separator + path;
//			break;
//
//		default:
//			log.info("constructed url is incorrect");
//		}
		log.info("requestUrl is : {}" ,url);
		return url;
	}
}
