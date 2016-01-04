package com.ctofunds.android.module.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.AuthenticationRequest;
import com.ctof.sms.api.AuthenticationResponse;
import com.ctof.sms.api.Expert;
import com.ctof.sms.api.UpdateExpertRequest;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.constants.Constants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.service.AccountService;

/**
 * Created by qianhao.zhou on 1/4/16.
 */
public class ChangePasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.modify_password);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Expert expert = SmsApplication.getAccountService().getExpertAccount();
        if (expert == null) {
            new AlertDialog.Builder(this).setCancelable(false).setTitle(R.string.not_login).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }).show();
            return;
        }
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDialog(R.string.wait_tips);
                final UpdateExpertRequest updateExpertRequest = new UpdateExpertRequest();
                final String oldPassword = ((EditText) findViewById(R.id.password)).getText().toString();
                final String newPassword = ((EditText) findViewById(R.id.new_password)).getText().toString();
                final String newPasswordConfirm = ((EditText) findViewById(R.id.new_password_confirm)).getText().toString();
                if (oldPassword.length() == 0 || newPassword.length() == 0 || newPasswordConfirm.length() == 0) {
                    showToast(R.string.missing_password);
                    return;
                }
                if (!newPassword.equals(newPasswordConfirm)) {
                    showToast(R.string.password_not_same);
                    return;
                }
                updateExpertRequest.setOldPassword(oldPassword);
                updateExpertRequest.setPassword(newPassword);
                ApiHandler.put(String.format(ApiConstants.EXPERT, expert.getId()), updateExpertRequest, Expert.class, new Response.Listener<Expert>() {
                    @Override
                    public void onResponse(Expert response) {
                        Log.i(getTag(), "change password succeed");
                        final AccountService accountService = SmsApplication.getAccountService();
                        accountService.clearAccount();
                        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
                        authenticationRequest.setEmail(response.getEmail());
                        authenticationRequest.setPassword(newPassword);
                        authenticationRequest.setExpiration(Constants.TOKEN_EXPIRATION);
                        ApiHandler.post(ApiConstants.LOGIN, authenticationRequest, AuthenticationResponse.class, new Response.Listener<AuthenticationResponse>() {
                            @Override
                            public void onResponse(AuthenticationResponse response) {
                                Log.i(getTag(), "login succeed");
                                accountService.setToken(response.getToken());
                                accountService.setExpertAccount(response.getExpert());
                                showToast("修改密码成功");
                                setResult(RESULT_OK);
                                dismissProgressDialog();
                                finish();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(getTag(), "login failed");
                                dismissProgressDialog();
                            }
                        });
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i(getTag(), "change password failed");
                        dismissProgressDialog();
                    }
                });
            }
        });
    }
}
