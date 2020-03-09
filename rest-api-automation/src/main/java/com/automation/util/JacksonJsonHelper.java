package com.automation.util;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * @author Raghotham.Madhusudan
 */

public class JacksonJsonHelper {

	public static String serialize(final Object object) throws Exception {

		final ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(object);
		} catch (final JsonProcessingException e) {
			throw new Exception("Error in serializing object", e);
		}
	}

	public static String serialize(final Object object, final Class<?> clazz) throws Exception {

		final ObjectWriter writer = new ObjectMapper().writerFor(clazz);
		try {
			return writer.writeValueAsString(object);
		} catch (final JsonProcessingException e) {
			throw new Exception("Error in serializing object", e);
		}
	}

	public static String serialize(final Object object, final TypeReference<?> typeRef) throws Exception {

		final ObjectWriter writer = new ObjectMapper().writerFor(typeRef);
		try {
			return writer.writeValueAsString(object);
		} catch (final JsonProcessingException e) {
			throw new Exception("Error in serializing object", e);
		}
	}

	public static <T> T deserialize(final String json, final Class<T> clazz) throws Exception {

		final ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.readValue(json, clazz);
		} catch (final IOException e) {
			throw new Exception(String.format("Error in deserializing json %s", json), e);
		}
	}
	
	public static <T> T deserialize(final String json, final TypeReference<?> typetoken) throws Exception {

		final ObjectMapper mapper = new ObjectMapper();
//		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

		try {
			return mapper.readValue(json, typetoken);
		} catch (final IOException e) {
			throw new Exception(String.format("Error in deserializing json %s", json), e);
		}
	}

	public static <T> T convertValue(final Map<String, Object> map, final Class<T> clazz) throws Exception {

		final ObjectMapper mapper = new ObjectMapper();

		try {
			return mapper.convertValue(map, clazz);
		} catch (final Exception e) {
			throw new Exception(String.format("Error in converting map %s to pojo of %s", map, clazz), e);
		}
	}

	public static Map<String, Object> convert(final Object model) throws Exception {

		final TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
		final ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.setSerializationInclusion(Include.NON_NULL);
			return mapper.convertValue(model, typeRef);
		} catch (final Exception e) {
			throw new Exception(String.format("Error in converting model %s to map", model.toString()), e);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> object2Map(Object fromValue,Class<Map> toValueType) {
		
		ObjectMapper oMapper = new ObjectMapper();
		Map <String,String> callInfoMap = oMapper.convertValue(fromValue, toValueType);
		return callInfoMap;
	}
}
