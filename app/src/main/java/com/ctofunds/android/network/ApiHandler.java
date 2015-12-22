package com.ctofunds.android.network;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.module.login.LoginActivity;
import com.ctofunds.android.utility.Environment;
import com.ctofunds.android.utility.ServerInfo;
import com.google.common.collect.Maps;
import com.google.gson.JsonSyntaxException;

import java.util.Collections;
import java.util.Map;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class ApiHandler {

    private ApiHandler() {
    }

    private static final class ApiRequest<T> extends JsonRequest<T> {

        private final Class<T> responseType;
        private final byte[] requestBody;
        private static final Map<String, String> HEADERS;
        static {
            Map<String, String> headers = Maps.newHashMap();
            headers.put("User-Agent", Environment.getInstance().getUserAgent());
            String token = SmsApplication.getInstance().getAccountService().getToken();
            if (token != null) {
                headers.put("X-AUTH-TOKEN", token);
            }
            HEADERS = Collections.unmodifiableMap(headers);
        }

        public ApiRequest(int method, String url, byte[] requestBody, Class<T> responseType, Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(method, url, null, listener, errorListener);
            this.responseType = responseType;
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

        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            if (response.statusCode == 200) {
                try {
                    return Response.success(SmsApplication.getSerializer().deserialize(responseType, response.data), null);
                } catch (JsonSyntaxException jse) {
                    return Response.error(new VolleyError("数据格式错误"));
                }
            } else if (response.statusCode == 401) {
                Intent intent = new Intent();
                intent.setClass(SmsApplication.getInstance().getApplicationContext(), LoginActivity.class);
                return Response.error(new AuthFailureError(intent));
            } else {
                if (response.data != null && response.data.length > 0) {
                    return Response.error(new VolleyError(new String(response.data)));
                } else {
                    return Response.error(new VolleyError("服务器错误 code:" + response.statusCode));
                }
            }
        }
    }

    private static final Response.ErrorListener DEFAULT_ERROR_LISTENER = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof AuthFailureError) {
                AuthFailureError authFailure = (AuthFailureError) error;
                if (authFailure.getResolutionIntent() != null) {
                    SmsApplication.getInstance().startActivity(authFailure.getResolutionIntent());
                    return;
                }
            }
            Context applicationContext = SmsApplication.getInstance().getApplicationContext();
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
                                        final Class<RESP> responseType,
                                        Response.Listener<RESP> listener,
                                        Response.ErrorListener errorListener) {
        SmsApplication.getNormalRequestQueue().add(
                new ApiRequest<>(Request.Method.POST,
                        getUrl(relativePath),
                        SmsApplication.getSerializer().serialize(requestObject),
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
