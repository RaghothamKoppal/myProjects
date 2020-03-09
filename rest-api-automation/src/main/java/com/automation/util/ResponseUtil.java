package com.automation.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.automation.controller.RestResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * @author Raghotham.Madhusudan
 */

public class ResponseUtil {

	public static final Logger log = LogManager.getLogger(ResponseUtil.class);
	RestResponse restResponse;

	public ResponseUtil(RestResponse restResponse) {

		this.restResponse = restResponse;
	}

	public String getCode() {

		final Gson gson = new Gson();
		JsonElement paramVal = null;

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonArray jsonArray = element.getAsJsonArray();
			for(int i=0; i< jsonArray.size();i++) {

				JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
				paramVal = jsonObject.get("code");
			}
		}
		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {} ",restResponse.getResponseBody());
		}
		log.info("code obtained from the response is {}",paramVal);
		return paramVal.getAsString();
	}

	public String getResponseCode() {

		final Gson gson = new Gson();
		String paramVal = null;

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonObject jsonObject = element.getAsJsonObject();
			paramVal = jsonObject.get("code").getAsString();
		}

		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {}", restResponse.getResponseBody());
		}
		log.info("code obtained from the response is {}",paramVal);
		return paramVal;
	}

	public String getHref() {

		final Gson gson = new Gson();
		JsonElement paramVal = null;

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonArray jsonArray = element.getAsJsonArray();

			for(int i=0; i< jsonArray.size();i++) {

				JsonObject jsonOuterObject = jsonArray.get(i).getAsJsonObject();
				JsonArray jsonInnerArray = jsonOuterObject.get("links").getAsJsonArray();

				for(int j=0;j < jsonInnerArray.size();j++) {

					JsonObject jsonInnerObject = jsonInnerArray.get(i).getAsJsonObject();
					paramVal = jsonInnerObject.get("href");
				}
			}
		}
		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {} ",restResponse.getResponseBody());
		}
		log.info("href obtained from the response is {}",paramVal);
		return paramVal.getAsString();
	}

	public String getHrefForUser() {

		final Gson gson = new Gson();
		JsonElement paramVal = null;

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);

			JsonObject jsonOuterObject = element.getAsJsonObject();
			JsonArray jsonInnerArray = jsonOuterObject.get("links").getAsJsonArray();

			for(int j=0;j < jsonInnerArray.size();j++) {

				JsonObject jsonInnerObject = jsonInnerArray.get(j).getAsJsonObject();
				paramVal = jsonInnerObject.get("href");
			}
		}
		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {} ",restResponse.getResponseBody());
		}
		log.info("href obtained from the response is {}",paramVal);
		return paramVal.getAsString();
	}

	public String getAnalyserIdFromHref() {

		final Gson gson = new Gson();
		JsonElement paramVal = null;
		String href = "";
		String[] hrefContents;
		String id = null;

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonArray jsonArray = element.getAsJsonArray();
			for(int i=0; i< jsonArray.size();i++) {

				JsonObject jsonOuterObject = jsonArray.get(i).getAsJsonObject();
				JsonArray jsonInnerArray = jsonOuterObject.get("links").getAsJsonArray();
				for(int j=0;j < jsonInnerArray.size();j++) {

					JsonObject jsonInnerObject = jsonInnerArray.get(i).getAsJsonObject();
					paramVal = jsonInnerObject.get("href");
					href = paramVal.getAsString();
					hrefContents = href.split("/");
					int length = hrefContents.length;
					log.info("length is {}",length);
					id = hrefContents[length-1];
					log.info("analyser id is {}",id);
				}
			}
		}
		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {} ",restResponse.getResponseBody());
		}
		log.info("analyser id obtained from the response is {}",paramVal);
		return id;
	}

	public String getAnalyserIdFromHrefForUser() {

		final Gson gson = new Gson();
		JsonElement paramVal = null;
		String href = "";
		String[] hrefContents;
		String id = null;

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);

			JsonObject jsonOuterObject = element.getAsJsonObject();
			JsonArray jsonInnerArray = jsonOuterObject.get("links").getAsJsonArray();
			for(int j=0;j < jsonInnerArray.size();j++) {

				JsonObject jsonInnerObject = jsonInnerArray.get(j).getAsJsonObject();
				paramVal = jsonInnerObject.get("href");
				href = paramVal.getAsString();
				hrefContents = href.split("/");
				int length = hrefContents.length;
				System.out.println("length is ----"+length);
				id = hrefContents[length-1];
				System.out.println("analyser id is-----"+id);
			}
		}
		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {} ",restResponse.getResponseBody());
		}
		log.info("analyser id obtained from the response is {}",paramVal);
		return id;
	}

	public String getAuxillaryDataType() {

		final Gson gson = new Gson();
		String paramVal = null;

		try {
			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonObject jsonObject = element.getAsJsonObject();
			if(jsonObject != null && jsonObject.get("auxiliaryDataType") != null) {

				paramVal = jsonObject.get("auxiliaryDataType").getAsString();
			}

		}

		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {}",restResponse.getResponseBody());
		}
		log.info("auxillary data type obtained from the response is {}", paramVal);
		return paramVal;
	}

	public String getType() {

		final Gson gson = new Gson();
		String paramVal = null;

		try {
			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonObject jsonObject = element.getAsJsonObject();
			if(jsonObject != null && jsonObject.get("type") != null) {

				paramVal = jsonObject.get("type").getAsString();
			}

		}

		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {}",restResponse.getResponseBody());
		}
		log.info("type obtained from the response is {}", paramVal);
		return paramVal;
	}

	public String getReason() {

		final Gson gson = new Gson();
		String paramVal = "";

		try {
			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonObject jsonObject = element.getAsJsonObject();
			final JsonObject jsonObject1 = (JsonObject) jsonObject.get("details");
			paramVal = jsonObject1.get("reason").getAsString();
		}

		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {}",restResponse.getResponseBody());
		}
		log.info("reason obtained from the response is {}",paramVal);
		return paramVal;
	}

	
	public String getStatus() {

		final Gson gson = new Gson();
		String paramVal = "";

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonObject jsonObject = element.getAsJsonObject();
			final JsonObject attributes = (JsonObject) jsonObject.get("attributes");
			paramVal = attributes.get("status__i").getAsString();
		}

		catch(final JsonSyntaxException e) {

			log.error("Not able to find the param {} in response {}",restResponse.getResponseBody());
		}
		log.info("status obtained from the response is {}",paramVal);
		return paramVal;
	}

	public String getStatusForUsers() {

		final Gson gson = new Gson();
		String paramVal = "";

		try {

			final JsonElement element = gson.fromJson(restResponse.getResponseBody(), JsonElement.class);
			final JsonObject jsonObject = element.getAsJsonObject();
			final JsonObject details = (JsonObject) jsonObject.get("details");
			final JsonObject user = (JsonObject) details.get("user");
			final JsonObject attributes = (JsonObject) user.get("attributes");
			paramVal = attributes.get("status__i").getAsString();
		}
		catch(JsonSyntaxException e) {

			log.error("not able to find the param {} in response {}",restResponse.getResponseBody());
		}
		log.info("status obtained from the response is {}",paramVal);
		return paramVal;
	}
}

