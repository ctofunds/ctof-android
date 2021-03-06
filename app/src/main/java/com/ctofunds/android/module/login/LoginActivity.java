package com.ctofunds.android.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.AuthenticationRequest;
import com.ctof.sms.api.AuthenticationResponse;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.constants.Constants;
import com.ctofunds.android.module.signup.ExpertSignUpActivity;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.service.AccountService;
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
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emailField = (EditText) findViewById(R.id.email);
        emailField.setText(getIntent().getStringExtra("email"));
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
                    showToast(R.string.missing_email);
                    return;
                }
                if (StringUtils.isEmpty(password)) {
                    showToast(R.string.missing_password);
                    return;
                }
                AuthenticationRequest authenticationRequest = new AuthenticationRequest();
                authenticationRequest.setEmail(email);
                authenticationRequest.setPassword(password);
                authenticationRequest.setExpiration(Constants.TOKEN_EXPIRATION);
                showProgressDialog(R.string.wait_tips);
                ApiHandler.post(ApiConstants.LOGIN, authenticationRequest, AuthenticationResponse.class, new Response.Listener<AuthenticationResponse>() {
                    @Override
                    public void onResponse(AuthenticationResponse response) {
                        AccountService accountService = SmsApplication.getAccountService();
                        accountService.clearAccount();
                        accountService.setToken(response.getToken());
                        if (response.getEmployee() != null) {
                            accountService.setEmployeeAccount(response.getEmployee());
                            showToast("员工登录成功");
                            setResult(RESULT_OK);
                            dismissProgressDialog();
                            finish();
                        } else {
                            accountService.setExpertAccount(response.getExpert());
                            showToast("专家登录成功");
                            setResult(RESULT_OK);
                            dismissProgressDialog();
                            finish();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();
                    }
                });
            }
        });
        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ExpertSignUpActivity.class);
                startActivity(intent);
            }
        });
    }

}
