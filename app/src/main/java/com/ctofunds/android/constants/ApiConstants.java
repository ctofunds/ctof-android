package com.ctofunds.android.constants;

/**
 * Created by qianhao.zhou on 12/21/15.
 */
public final class ApiConstants {

    private ApiConstants() {
    }

    public static final String ALIYUN_ENDPOINT = "http://oss-cn-hangzhou.aliyuncs.com/";
    public static final String ALIYUN_HOST = "http://shangmashi.oss-cn-hangzhou.aliyuncs.com/";
    public static final String ALIYUN_IMAGE_HOST = "http://shangmashi.img-cn-hangzhou.aliyuncs.com/";

    public static final String LOGIN = "api/v1/auth/login";//post
    public static final String CODES = "api/v1/codes";//code
    public static final String GET_EXPERT = "api/v1/experts/%d";//query expert
    public static final String CREATE_EXPERT = "api/v1/experts";//create expert
    public static final String INVITE = "api/v1/invites";//invite employee or expert
    public static final String REQUEST_TOKEN = "api/v1/sts";
}
