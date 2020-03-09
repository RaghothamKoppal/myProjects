package com.automation.util;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Map;
import org.apache.commons.io.FileUtils;

import com.automation.pojo.AuxillaryData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

/**
 * @author Raghotham.Madhusudan
 */

public class JsonUtil {
	
	@SuppressWarnings("unused")
	public static <T> T readTestAuxData(String fileName,final TypeReference<?> typetoken) throws Exception {

		  File file = new File(fileName);
		  String json = FileUtils.readFileToString(file, "UTF-8");
		  final Type typeOf = new TypeToken<Map<String,AuxillaryData>>(){}.getType();
		  final T object = JacksonJsonHelper.deserialize(json, typetoken);
		  return object;
	}
	
	public static <T> T readFile(String fileName,final Class<T> clazz) throws Exception {

		File file = new File(fileName);
		String json = FileUtils.readFileToString(file, "UTF-8");
		final T object = JacksonJsonHelper.deserialize(json, clazz);
		return object;
	} 
	
	public static String convertToString(Map<String, Object> jsonMap) {
		
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String stringInJson = gson.toJson(jsonMap);
		return stringInJson;
	}
}