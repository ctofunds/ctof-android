package com.ctofunds.android.serializer.impl;

import com.ctofunds.android.exception.SerializationException;
import com.ctofunds.android.serializer.Serializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;

import java.io.IOException;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public class JacksonSerializer implements Serializer {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    @Override
    public byte[] serialize(Object obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T fromJsonString(TypeReference<T> type, String str) {
        try {
            return objectMapper.readValue(str, type);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T deserialize(TypeReference<T> type, byte[] data) {
        try {
            return objectMapper.readValue(data, type);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T fromJsonString(Class<T> type, String str) {
        try {
            return objectMapper.readValue(str, type);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @Override
    public <T> T deserialize(Class<T> type, byte[] data) {
        try {
            return objectMapper.readValue(data, type);
        } catch (IOException e) {
            throw new SerializationException(e);
        }
    }
}
