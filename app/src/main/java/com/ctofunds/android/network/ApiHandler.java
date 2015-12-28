package com.ctofunds.android.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.Error;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.utility.ServerInfo;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class ApiHandler {

    private ApiHandler() {
    }

    private static final Response.ErrorListener DEFAULT_ERROR_LISTENER = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Context applicationContext = SmsApplication.getInstance().getApplicationContext();
            if (error.networkResponse != null && error.networkResponse.data != null) {
                try {
                    Error errorEntity = SmsApplication.getSerializer().deserialize(Error.class, error.networkResponse.data);
                    Toast.makeText(applicationContext, errorEntity.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    Log.w("DEFAULT_ERROR_LISTENER", "error parsing error.networkResponse", e);
                }
            }
            String errorMsg = error.getMessage();
            if (errorMsg == null) {
                errorMsg = "服务器错误";
            }
            Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final Class<RESP> responseType,
                                        Response.Listener<RESP> listener) {
        post(relativePath, requestObject, responseType, listener, null);
    }

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final TypeReference<RESP> responseType,
                                        Response.Listener<RESP> listener) {
        post(relativePath, requestObject, responseType, listener, null);
    }

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final Class<RESP> responseType,
                                        Response.Listener<RESP> listener,
                                        Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithClass<>(Request.Method.POST,
                        getUrl(relativePath),
                        SmsApplication.getSerializer().serialize(requestObject),
                        responseType,
                        listener,
                        errorListener == null ? DEFAULT_ERROR_LISTENER : errorListener));
    }

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final TypeReference<RESP> responseType,
                                        Response.Listener<RESP> listener,
                                        Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithTypeReference<>(Request.Method.POST,
                        getUrl(relativePath),
                        SmsApplication.getSerializer().serialize(requestObject),
                        responseType,
                        listener,
                        errorListener == null ? DEFAULT_ERROR_LISTENER : errorListener));
    }

    public static <RESP> void get(String relativePath,
                                  final TypeReference<RESP> responseType,
                                  Response.Listener<RESP> listener,
                                  Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithTypeReference<>(Request.Method.GET,
                        getUrl(relativePath),
                        null,
                        responseType,
                        listener,
                        errorListener == null ? DEFAULT_ERROR_LISTENER : errorListener));
    }

    public static <RESP> void get(String relativePath,
                                  final Class<RESP> responseType,
                                  Response.Listener<RESP> listener,
                                  Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithClass<>(Request.Method.GET,
                        getUrl(relativePath),
                        null,
                        responseType,
                        listener,
                        errorListener == null ? DEFAULT_ERROR_LISTENER : errorListener));
    }

    private static String getUrl(String relativePath) {
        String host = ServerInfo.getInstance().getServerHost();
        String url;
        if (host.endsWith("/")) {
            url = host + relativePath;
        } else {
            url = host + "/" + relativePath;
        }
        return url;
    }
}
