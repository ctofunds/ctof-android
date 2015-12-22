package com.ctofunds.android.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.ctofunds.android.SmsApplication;
import com.google.common.base.Preconditions;

/**
 * Created by qianhao.zhou on 12/22/15.
 */
public abstract class BaseService {

    private final Context context;

    public BaseService(Context context) {
        this.context = context;
    }

    protected final SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences("sms." + Preconditions.checkNotNull(getClass().getSimpleName().toLowerCase()), Context.MODE_PRIVATE);
    }

    protected final String get(String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, null);
    }

    protected final void put(String key, String value) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(value, "value cannot be null");
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    protected final <T> void putObject(String key, T value) {
        Preconditions.checkNotNull(key, "key cannot be null");
        Preconditions.checkNotNull(value, "value cannot be null");
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, SmsApplication.getSerializer().toJsonString(value));
        editor.commit();
    }

    protected final <T> T getObject(String key, Class<T> type) {
        Preconditions.checkNotNull(key, "key cannot be null");
        SharedPreferences sharedPreferences = getSharedPreferences();
        String jsonString = sharedPreferences.getString(key, null);
        if (jsonString != null) {
            return SmsApplication.getSerializer().fromJsonString(type, jsonString);
        } else {
            return null;
        }
    }

    protected final void remove(String key) {
        Preconditions.checkNotNull(key, "key cannot be null");
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.remove(key);
        editor.commit();
    }

    protected final void removeAll() {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.commit();
    }

}
