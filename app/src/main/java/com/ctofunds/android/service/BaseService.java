package com.ctofunds.android.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.serializer.Serializer;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by qianhao.zhou on 12/22/15.
 */
public abstract class BaseService {

    private final Context context;

    private ConcurrentMap<String, Object> cache = Maps.newConcurrentMap();

    public BaseService(Context context) {
        this.context = context;
    }

    protected final SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("sms." + Preconditions.checkNotNull(getClass().getSimpleName().toLowerCase()), Context.MODE_PRIVATE);
    }

    protected final boolean isEmpty() {
        return getSharedPreferences().getAll().size() == 0;
    }


    protected final String get(String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        SharedPreferences sharedPreferences = getSharedPreferences();
        String result = sharedPreferences.getString(key, null);
        if (result != null) {
            cache.put(key, result);
        }
        return result;
    }

    protected final void put(String key, String value) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(value, "value cannot be null");
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
        cache.put(key, value);
    }

    protected final <T> void putObject(String key, T value) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(value, "value cannot be null");
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        String serializedValue = getSerializer().toJsonString(value);
        editor.putString(key, serializedValue);
        editor.commit();
        cache.put(key, value);
    }

    protected final <T> T getObject(String key, Class<T> type) {
        Preconditions.checkNotNull(key, "key cannot be null");
        if (cache.containsKey(key)) {
            return (T) cache.get(key);
        }
        SharedPreferences sharedPreferences = getSharedPreferences();
        String jsonString = sharedPreferences.getString(key, null);
        if (jsonString != null) {
            T result = getSerializer().fromJsonString(type, jsonString);
            cache.put(key, result);
            return result;
        } else {
            return null;
        }
    }

    protected abstract Serializer getSerializer();

    protected final void remove(String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.commit();
        cache.remove(key);
    }

    protected final void removeAll() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.commit();
        cache.clear();
    }

}
