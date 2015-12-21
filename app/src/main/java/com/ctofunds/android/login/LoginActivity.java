package com.ctofunds.android.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.ctof.sms.api.AuthenticationRequest;
import com.ctof.sms.api.AuthenticationResponse;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.utility.StringUtils;

/**
 * Created by qianhao.zhou on 12/18/15.
 */
public class LoginActivity extends BaseActivity {

    private Toolbar toolbar;

    private EditText emailField;
    private EditText passwordField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.login);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        findViewById(R.id.forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("忘记密码");
            }
        });
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                if (StringUtils.isEmpty(email)) {
                    showToast("email 不能为空");
                    return;
                }
                if (StringUtils.isEmpty(password)) {
                    showToast("email 不能为空");
                    return;
                }
                AuthenticationRequest authenticationRequest = new AuthenticationRequest();
                authenticationRequest.setEmail(email);
                authenticationRequest.setPassword(password);
                authenticationRequest.setExpiration(120);
                ApiHandler.post(ApiConstants.LOGIN, authenticationRequest, AuthenticationResponse.class, new Response.Listener<AuthenticationResponse>() {
                    @Override
                    public void onResponse(AuthenticationResponse response) {
                        Log.d(getTag(), "response:" + response);
                    }
                });
            }
        });
        findViewById(R.id.register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("注册账号");
            }
        });
    }

}
