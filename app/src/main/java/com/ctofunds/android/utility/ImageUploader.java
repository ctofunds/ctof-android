package com.ctofunds.android.utility;

import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.android.volley.Request;
import com.android.volley.toolbox.RequestFuture;
import com.ctof.sms.api.StsResponse;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.exception.ImageUploaderException;
import com.ctofunds.android.network.ApiRequestWithClass;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by qianhao.zhou on 12/30/15.
 */
public class ImageUploader {

    private static class SingletonHolder {
        private static ImageUploader instance = new ImageUploader();
    }

    public static final ImageUploader getInstance() {
        return SingletonHolder.instance;
    }


    private final OSSClient ossClient;

    private ImageUploader() {
        this.ossClient = new OSSClient(SmsApplication.getInstance().getApplicationContext(), ApiConstants.ALIYUN_ENDPOINT, new OSSFederationCredentialProvider() {
            @Override
            public OSSFederationToken getFederationToken() {
                RequestFuture<StsResponse> future = RequestFuture.newFuture();
                ApiRequestWithClass<StsResponse> request = new ApiRequestWithClass<>(Request.Method.POST, ServerInfo.getUrl(ApiConstants.REQUEST_TOKEN), null, StsResponse.class, future, future);
                SmsApplication.getImageRequestQueue().add(request);
                try {
                    StsResponse stsResponse = future.get(2, TimeUnit.SECONDS);
                    return new OSSFederationToken(stsResponse.getAccessKeyId(), stsResponse.getAccessKeySecret(), stsResponse.getToken(), stsResponse.getExpiresIn());
                } catch (Exception e) {
                    Log.d("ImageUploader", "failed to get token", e);
                    return null;
                }
            }
        });
    }

    public void upload(byte[] data, final String relativePath, final Callback callback) {
        PutObjectRequest put = new PutObjectRequest("shangmashi", relativePath, data);
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentMD5(BinaryUtil.calculateBase64Md5(data));
            put.setMetadata(metadata);
            put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
                @Override
                public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                    if (callback != null) {
                        callback.onProgress(currentSize, totalSize);
                    }
                }
            });

            ossClient.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                @Override
                public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                    Log.d("PutObject", "UploadSuccess");
                    Log.d("ETag", result.getETag());
                    Log.d("RequestId", result.getRequestId());
                    if (callback != null) {
                        callback.onSuccess(ApiConstants.ALIYUN_IMAGE_HOST + relativePath);
                    }
                }

                @Override
                public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                    if (clientException != null) {
                        //for instance, network failure
                        clientException.printStackTrace();
                    }
                    if (serviceException != null) {
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                    }
                    if (callback != null) {
                        callback.onFailure(new ImageUploaderException(clientException));
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ImageUploader", "fail to upload data to location:" + relativePath, e);
        }
    }

    public static final String composeAvatarUrl(long expertId, String postfix) {
        return String.format("expert/%d/avatar/" + System.currentTimeMillis() + "." + postfix, expertId);
    }

    public interface Callback {
        void onProgress(long current, long total);

        void onSuccess(String url);

        void onFailure(ImageUploaderException exception);
    }
}
