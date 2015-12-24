package com.ctofunds.android.module.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.Code;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.module.main.MainActivity;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.service.CodeService;
import com.ctofunds.android.utility.Environment;
import com.ctofunds.android.utility.ServerInfo;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Environment.getInstance();
        ServerInfo.getInstance().setServerType(ServerInfo.ServerType.DEV);
        final CodeService codeService = SmsApplication.getCodeService();
        if (codeService.isReady()) {
            Log.d(getTag(), "code service ready, start now");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startMain();
                }
            }, 100);
        } else {
            Log.d(getTag(), "code service not ready, load it from api");
            ApiHandler.get(ApiConstants.CODES, new TypeReference<List<Code>>() {
            }, new Response.Listener<List<Code>>() {
                @Override
                public void onResponse(List<Code> response) {
                    Log.d(getTag(), "code response:" + response);
                    codeService.reload(response);
                    startMain();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showToast("网络异常，请重新启动");
                }
            });
        }
    }

    private void startMain() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_splash_in, R.anim.anim_splash_out);
        finish();
    }
}
