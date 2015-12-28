package com.ctofunds.android.network;

import android.content.Intent;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.module.login.LoginActivity;
import com.ctofunds.android.utility.Environment;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;

import java.util.Collections;
import java.util.Map;

/**
 * Created by qianhao.zhou on 12/24/15.
 */
abstract class ApiRequestBase<T> extends JsonRequest<T> {

    private final byte[] requestBody;
    private static final Map<String, String> HEADERS;

    static {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("User-Agent", Environment.getInstance().getUserAgent());
        String token = SmsApplication.getAccountService().getToken();
        if (token != null) {
            headers.put("X-AUTH-TOKEN", token);
        }
        HEADERS = Collections.unmodifiableMap(headers);
    }

    public ApiRequestBase(int method, String url, byte[] requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, null, listener, errorListener);
        this.requestBody = requestBody;
    }

    @Override
    public byte[] getBody() {
        return requestBody;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return HEADERS;
    }

    protected abstract T parseResponse(byte[] data);

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        if (response.statusCode == 200) {
            try {
                return Response.success(parseResponse(response.data), null);
            } catch (JsonSyntaxException jse) {
                return Response.error(new VolleyError("数据格式错误"));
            }
        } else {
            return Response.error(new VolleyError("服务器错误 code:" + response.statusCode));
        }
    }
}