package com.ctofunds.android.network;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.Error;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.event.TokenExpireEvent;
import com.ctofunds.android.utility.ServerInfo;
import com.fasterxml.jackson.core.type.TypeReference;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class ApiHandler {

    private ApiHandler() {
    }

    private static class DefaultErrorListener implements Response.ErrorListener {

        private final Response.ErrorListener innerListener;

        private DefaultErrorListener(Response.ErrorListener innerListener) {
            this.innerListener = innerListener;
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            final Context applicationContext = SmsApplication.getInstance().getApplicationContext();
            if (error.networkResponse != null && error.networkResponse.data != null) {
                try {
                    Error errorEntity = SmsApplication.getSerializer().deserialize(Error.class, error.networkResponse.data);
                    Toast.makeText(applicationContext, errorEntity.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.w("DEFAULT_ERROR_LISTENER", "error parsing error.networkResponse", e);
                }
            }
            if (innerListener != null) {
                innerListener.onErrorResponse(error);
            }
            if (error instanceof AuthFailureError) {
                SmsApplication.getEventBus().post(new TokenExpireEvent());
            }
        }
    }

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final Class<RESP> responseType,
                                        Response.Listener<RESP> listener,
                                        Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithClass<>(Request.Method.POST,
                        ServerInfo.getUrl(relativePath),
                        SmsApplication.getSerializer().serialize(requestObject),
                        responseType,
                        listener,
                        new DefaultErrorListener(errorListener)));
    }

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final TypeReference<RESP> responseType,
                                        Response.Listener<RESP> listener,
                                        Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithTypeReference<>(Request.Method.POST,
                        ServerInfo.getUrl(relativePath),
                        SmsApplication.getSerializer().serialize(requestObject),
                        responseType,
                        listener,
                        new DefaultErrorListener(errorListener)));
    }

    public static <REQ, RESP> void put(String relativePath, REQ requestObject,
                                       Class<RESP> responseType,
                                       Response.Listener<RESP> listener,
                                       Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithClass<>(Request.Method.PUT,
                        ServerInfo.getUrl(relativePath),
                        SmsApplication.getSerializer().serialize(requestObject),
                        responseType,
                        listener,
                        new DefaultErrorListener(errorListener)));
    }


    public static <RESP> void get(String relativePath,
                                  final TypeReference<RESP> responseType,
                                  Response.Listener<RESP> listener,
                                  Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithTypeReference<>(Request.Method.GET,
                        ServerInfo.getUrl(relativePath),
                        null,
                        responseType,
                        listener,
                        new DefaultErrorListener(errorListener)));
    }

    public static <RESP> void get(String relativePath,
                                  final Class<RESP> responseType,
                                  Response.Listener<RESP> listener,
                                  Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequestWithClass<>(Request.Method.GET,
                        ServerInfo.getUrl(relativePath),
                        null,
                        responseType,
                        listener,
                        new DefaultErrorListener(errorListener)));
    }

}
