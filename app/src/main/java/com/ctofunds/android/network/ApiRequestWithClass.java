package com.ctofunds.android.network;

import android.util.Log;

import com.android.volley.Response;
import com.ctofunds.android.SmsApplication;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by qianhao.zhou on 12/24/15.
 */
class ApiRequestWithClass<T> extends ApiRequestBase<T> {

    private Class<T> type;

    public ApiRequestWithClass(int method, String url, byte[] requestBody, Class<T> type, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.type = type;
    }

    @Override
    protected T parseResponse(byte[] data) {
        String str = new String(data);
        Log.d("ApiRequest", "response:\n" + str);
        return SmsApplication.getSerializer().fromJsonString(type, str);
    }
}