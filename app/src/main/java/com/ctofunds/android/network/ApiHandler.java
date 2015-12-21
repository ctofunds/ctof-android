package com.ctofunds.android.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.serializer.JacksonSerializer;
import com.ctofunds.android.serializer.Serializer;
import com.ctofunds.android.utility.ServerInfo;
import com.google.gson.JsonSyntaxException;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class ApiHandler {

    private ApiHandler() {
    }

    private static final class GsonRequest<T> extends JsonRequest<T> {

        private final Class<T> responseType;
        private final byte[] requestBody;

        public GsonRequest(int method, String url, byte[] requestBody, Class<T> responseType, Response.Listener<T> listener, Response.ErrorListener errorListener) {
            super(method, url, null, listener, errorListener);
            this.responseType = responseType;
            this.requestBody = requestBody;
        }

        @Override
        public byte[] getBody() {
            return requestBody;
        }


        @Override
        protected Response<T> parseNetworkResponse(NetworkResponse response) {
            if (response.statusCode == 200) {
                try {
                    return Response.success(serializer.deserialize(responseType, response.data), null);
                } catch (JsonSyntaxException jse) {
                    return Response.error(new VolleyError("数据格式错误"));
                }
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
            Context applicationContext = SmsApplication.getInstance().getApplicationContext();
            String errorMsg = error.getMessage();
            if (errorMsg == null) {
                errorMsg = "服务器错误";
            }
            Toast.makeText(applicationContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    private static final Serializer serializer = new JacksonSerializer();

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final Class<RESP> responseType,
                                        Response.Listener<RESP> listener) {
        post(relativePath, requestObject, responseType, listener, null);
    }

    public static <REQ, RESP> void post(String relativePath, REQ requestObject,
                                        final Class<RESP> responseType,
                                        Response.Listener<RESP> listener,
                                        Response.ErrorListener errorListener) {
        SmsApplication.getInstance().getNormalRequestQueue().add(
                new GsonRequest<>(Request.Method.POST,
                        getUrl(relativePath),
                        serializer.serialize(requestObject),
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
