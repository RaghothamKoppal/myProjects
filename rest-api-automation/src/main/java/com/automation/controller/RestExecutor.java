package com.automation.controller;

import static com.jayway.restassured.RestAssured.given;
import java.util.HashMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automation.controller.RestApiUrls;
import com.automation.util.DataLoader;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;

/**
 * @author Raghotham.Madhusudan
 */

public class RestExecutor extends RestApiUrls{
	
	public static final Logger log = LogManager.getLogger(RestExecutor.class);
	private DataLoader dataLoader = DataLoader.getInstance();
	String requestUrl;
	String paramConn;
	Map<String, String> headers = new HashMap<String, String>();
	Map<String, String> param = new HashMap<String, String>();

	public RestExecutor(String url, Map<String, String> parameters) throws Exception {
		
		this.paramConn = "{\"accountName\":\"thoughtspot_partner\",\"user\":\"ASTHA_ARYA\",\"password\":\"th0ughtSp0t1\",\"role\":\"developer\",\"warehouse\":\"X_SMALL_WH\",\"database\":\"\",\"schema\":\"\",\"connection_name\":\"TEST_SWAGGER\",\"description\":\"world\"}";
		this.requestUrl = constructUrl(url);
		this.param.put("type", "RDBMS_SNOWFLAKE");
		this.param.put("config", paramConn);
//		this.param.p
//		this.headers.put(dataLoader.getHeaderInfo().getFromHeader(), dataLoader.getHeaderInfo().getFrom());
//		this.headers.put(dataLoader.getHeaderInfo().getAuthorizationHeader(), dataLoader.getHeaderInfo().getAuthorization()+" tenantId=" + dataLoader.getHeaderInfo().getTenantId() + "");
//		this.headers.put(dataLoader.getHeaderInfo().getContentTypeHeader(), dataLoader.getHeaderInfo().getContentType());
		this.param.putAll(parameters);
	}

	public String doPost(String payload) {
		
		log.info("request url is : {}",requestUrl);
//		log.info("header information are : {}",headers);
		log.info("payload is : {}",payload);
		RequestSpecification reqSpec = given().headers(headers).parameters(param).body(payload);
		Response response = reqSpec.when().post(requestUrl).andReturn();
		return response.getBody().asString();
	}
	
	public String doPut(String payload) {
		
		log.info("request url is : {}",requestUrl);
		RequestSpecification reqSpec = given().headers(headers).parameters(param).body(payload);
		Response response = reqSpec.when().put(requestUrl).andReturn();
		return response.getBody().asString();
	}
	
	public String doGet() {
		
		String payload ="{}";
		log.info("request url is : {}",requestUrl);
		RequestSpecification reqSpec = given().headers(headers).parameters(param).body(payload);
		Response response = reqSpec.when().get(requestUrl).andReturn();
		return response.getBody().asString();
	}
	
	public String doDelete() {
		
		String payload = "{}";
		log.info("request url is : {}",requestUrl);
		RequestSpecification reqSpec = given().headers(headers).parameters(param).body(payload);
		Response response = reqSpec.when().delete(requestUrl).andReturn();
		return response.getBody().asString();
	}
}
