package com.ctofunds.android.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.main.MainActivity;
import com.ctofunds.android.utility.Environment;
import com.ctofunds.android.utility.ServerInfo;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Environment.getInstance();
                ServerInfo.getInstance().setServerType(ServerInfo.ServerType.DEV);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent();
                        intent.setClass(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_splash_in, R.anim.anim_splash_out);
                        finish();
                    }
                }, 100);
            }
        });
//        SmsApplication.getInstance().getNormalRequestQueue().add(new StringRequest(Request.Method.GET, "http://www.baidu.com", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("splash", "response:" + response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("splash", "error response", error);
//            }
//        }));
    }
}
