package com.vts.clientcenter.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonHelper implements InitializingBean {
    public static final String INVALID_JSON_STRING = "Invalid JSON String: {}";
    private static JsonHelper instance;
    private final Logger logger = LoggerFactory.getLogger(JsonHelper.class);
    @Autowired
    private ObjectMapper objectMapper;

    public static JsonHelper getInstance() {
        return instance;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JsonHelper.instance = this;//NOSONAR
    }

    public String toJsonString(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e); //NOSONAR
        }
    }

    public byte[] toJsonBytes(Object value) {
        try {
            return objectMapper.writeValueAsBytes(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(e); //NOSONAR
        }
    }

    public <T> T fromJsonString(String jsonStr, Class<T> objectType) {
        try {
            return objectMapper.readValue(jsonStr, objectType);
        } catch (Exception e) {
            logger.error("Invalid JSON: {}", jsonStr);
            throw new IllegalArgumentException(e); //NOSONAR
        }
    }

    public <T> T fromJsonObject(JSONObject jsonObject, Class<T> objectType) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(jsonObject), objectType);
        } catch (Exception e) {
            logger.error("Invalid JSON: {}", jsonObject);
            throw new IllegalArgumentException(e); //NOSONAR
        }
    }

    public JSONObject jsonObjectFromString(String jsonString){
        try {
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(jsonString);
        }catch (ParseException err){
            logger.error(INVALID_JSON_STRING, jsonString);
            throw new IllegalArgumentException(err); //NOSONAR
        }
    }

    public JSONObject jsonObjectFromObject(Object object){
        try {
            JSONParser parser = new JSONParser();
            return (JSONObject) parser.parse(objectMapper.writeValueAsString(object));
        }catch (ParseException | JsonProcessingException err){
            logger.error(INVALID_JSON_STRING, object);
            throw new IllegalArgumentException(err); //NOSONAR
        }
    }

    public JSONArray jsonArrayFromArray(List objects) {
        try {
            JSONArray array = new JSONArray();
            JSONParser parser = new JSONParser();
            for (Object object: objects) {
                JSONObject object1 = (JSONObject) parser.parse(objectMapper.writeValueAsString(object));
                if (object1 != null) array.add(object1);
            }
            return array;
        }catch (ParseException | JsonProcessingException err){
            logger.error(INVALID_JSON_STRING, objects);
            throw new IllegalArgumentException(err); //NOSONAR
        }
    }
}
