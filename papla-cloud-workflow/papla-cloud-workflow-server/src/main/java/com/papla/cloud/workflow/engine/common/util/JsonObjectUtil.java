package com.papla.cloud.workflow.engine.common.util;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonObjectUtil {

    private static ObjectMapper mapper = new ObjectMapper();

    public static <T> T mergeObject(T source, T target, Class<? extends T> clazz) throws Exception {
        mapper.setSerializationInclusion(Include.NON_DEFAULT);
        Map<String, Object> sMap = objectToMap(source);
        Map<String, Object> tMap = objectToMap(target);
        tMap.putAll(sMap);
        target = mapper.readValue(mapper.writeValueAsString(tMap), clazz);
        return target;
    }

    public static <T> T MapToObject(Map<String, Object> map, Class<? extends T> clazz) throws Exception {
        mapper.setSerializationInclusion(Include.NON_DEFAULT);
        return mapper.readValue(mapper.writeValueAsString(map), clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> Map<String, Object> objectToMap(T obj) throws Exception {
        mapper.setSerializationInclusion(Include.NON_DEFAULT);
        String oStr = mapper.writeValueAsString(obj);
        Map<String, Object> map = mapper.readValue(oStr, Map.class);
        return map;
    }

    public static <T> String objectToJSON(T obj) throws Exception {
        return mapper.writeValueAsString(obj);
    }

    public static <T> T JSONToObj(String JsonStr, Class<? extends T> clazz) throws Exception {
        mapper.setSerializationInclusion(Include.NON_DEFAULT);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper.readValue(JsonStr, clazz);
    }
}

