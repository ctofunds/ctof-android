package com.ctofunds.android.serializer;

import com.ctofunds.android.constants.Constants;
import com.ctofunds.android.exception.SerializationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Field;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
//public class GsonSerializer implements Serializer {
//
//    private static final Gson gson = new GsonBuilder()
//            .disableInnerClassSerialization()
//            .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
//            .setPrettyPrinting()
//            .setDateFormat(Constants.DEFAULT_DATE_FORMAT_PATTERN)
//            .create();
//
//    private static class SuperClassExclusionStrategy implements ExclusionStrategy {
//        @Override
//        public boolean shouldSkipClass(Class<?> arg0) {
//            return false;
//        }
//
//        @Override
//        public boolean shouldSkipField(FieldAttributes fieldAttributes) {
//            String fieldName = fieldAttributes.getName();
//            Class<?> theClass = fieldAttributes.getDeclaringClass();
//
//            return isFieldInSuperclass(theClass, fieldName);
//        }
//
//        private boolean isFieldInSuperclass(Class<?> subclass, String fieldName) {
//            Class<?> superclass = subclass.getSuperclass();
//            Field field;
//
//            while (superclass != null) {
//                field = getField(superclass, fieldName);
//
//                if (field != null)
//                    return true;
//
//                superclass = superclass.getSuperclass();
//            }
//
//            return false;
//        }
//
//        private Field getField(Class<?> theClass, String fieldName) {
//            try {
//                return theClass.getDeclaredField(fieldName);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//    }
//
//    @Override
//    public byte[] serialize(Object obj) {
//        return gson.toJson(obj).getBytes();
//    }
//
//    @Override
//    public String toJsonString(Object obj) {
//        return gson.toJson(obj);
//    }
//
//    @Override
//    public <T> T fromJsonString(TypeReference<T> type, String str) {
//        try {
//            return gson.fromJson(str, type.getType());
//        } catch (JsonSyntaxException e) {
//            throw new SerializationException(e);
//        }
//    }
//
//    @Override
//    public <T> T deserialize(TypeReference<T> type, byte[] data) {
//        try {
//            return gson.fromJson(new String(data), type.getType());
//        } catch (JsonSyntaxException ex) {
//            throw new SerializationException(ex);
//        }
//    }
//}
