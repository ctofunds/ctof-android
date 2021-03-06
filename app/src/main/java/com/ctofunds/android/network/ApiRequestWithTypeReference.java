package com.ctofunds.android.network;

import com.android.volley.Response;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.exception.SerializationException;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by qianhao.zhou on 12/24/15.
 */
class ApiRequestWithTypeReference<T> extends ApiRequestBase<T> {

    private TypeReference<T> typeReference;

    public ApiRequestWithTypeReference(int method, String url, byte[] requestBody, TypeReference<T> typeReference, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
        this.typeReference = typeReference;
    }

    @Override
    protected T parseResponse(byte[] data) throws SerializationException {
        if (data == null || data.length == 0) {
            return null;
        }
        return SmsApplication.getSerializer().deserialize(typeReference, data);
    }
}