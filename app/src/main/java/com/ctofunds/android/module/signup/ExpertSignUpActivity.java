package com.ctofunds.android.module.signup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.AuthenticationResponse;
import com.ctof.sms.api.CreateExpertRequest;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.service.AccountService;
import com.ctofunds.android.utility.StringUtils;

/**
 * Created by qianhao.zhou on 12/27/15.
 */
public class ExpertSignUpActivity extends BaseActivity {

    private EditText nameField;
    private EditText emailField;
    private EditText passwordField;
    private EditText passwordConfirmField;
    private EditText invitationCodeField;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.expert_signup);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nameField = (EditText) findViewById(R.id.name);
        emailField = (EditText) findViewById(R.id.email);
        passwordField = (EditText) findViewById(R.id.password);
        passwordConfirmField = (EditText) findViewById(R.id.confirm_password);
        invitationCodeField = (EditText) findViewById(R.id.invitation_code);

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();
                String passwordConfirm = passwordConfirmField.getText().toString();
                String invitationCode = invitationCodeField.getText().toString();
                if (StringUtils.isEmpty(name)) {
                    showToast(R.string.missing_name);
                    return;
                }
                if (StringUtils.isEmpty(email)) {
                    showToast(R.string.missing_email);
                    return;
                }
                if (!StringUtils.isEmailValid(email)) {
                    showToast(R.string.invalid_email);
                    return;
                }
                if (StringUtils.isEmpty(password) || StringUtils.isEmpty(passwordConfirm)) {
                    showToast(R.string.missing_password);
                    return;
                }
                if (!password.equals(passwordConfirm)) {
                    showToast(R.string.password_not_same);
                    return;
                }
                if (password.length() < 6) {
                    showToast(R.string.invalid_password_length);
                    return;
                }
                if (StringUtils.isEmpty(invitationCode)) {
                    showToast(R.string.missing_invitation_code);
                    return;
                }
                CreateExpertRequest createExpertRequest = new CreateExpertRequest();
                createExpertRequest.setName(name);
                createExpertRequest.setEmail(email);
                createExpertRequest.setPassword(password);
                createExpertRequest.setInviteCode(invitationCode);
                showProgressDialog(R.string.wait_tips);
                ApiHandler.post(ApiConstants.EXPERTS, createExpertRequest, AuthenticationResponse.class, new Response.Listener<AuthenticationResponse>() {
                    @Override
                    public void onResponse(AuthenticationResponse response) {
                        dismissProgressDialog();
                        showToast(R.string.sign_up_succeed);
                        AccountService accountService = SmsApplication.getAccountService();
                        accountService.clearAccount();
                        accountService.setToken(response.getToken());
                        accountService.setExpertAccount(response.getExpert());
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dismissProgressDialog();
                    }
                });

            }
        });
    }
}
