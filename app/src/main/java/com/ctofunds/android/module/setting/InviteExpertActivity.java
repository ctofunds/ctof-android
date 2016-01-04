package com.ctofunds.android.module.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.ctof.sms.api.Expert;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.utility.StringUtils;
import com.google.common.collect.Lists;

import java.util.ArrayList;

/**
 * Created by qianhao.zhou on 12/28/15.
 */
public class InviteExpertActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_expert);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.invite);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Expert expert = SmsApplication.getAccountService().getExpertAccount();
        boolean hasInvitationCode = expert != null && (expert.getInviteQuota() - expert.getInviteUsed()) > 0;
        if (hasInvitationCode) {
            final int invitationCount = expert.getInviteQuota() - expert.getInviteUsed();
            final LayoutInflater layoutInflater = getLayoutInflater();
            updateRemainingInvitationCount(invitationCount);
            final LinearLayout emailList = (LinearLayout) findViewById(R.id.email_list);
            for (int i = 0; i < invitationCount; i++) {
                layoutInflater.inflate(R.layout.item_email_input, emailList, true);
            }
            findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArrayList<String> emails = Lists.newArrayList();
                    for (int i = 0, size = emailList.getChildCount(); i < size; ++i) {
                        EditText emailField = (EditText) emailList.getChildAt(i).findViewById(R.id.email);
                        String email = emailField.getText().toString();
                        if (StringUtils.isEmpty(email)) {
                            continue;
                        }
                        if (StringUtils.isEmailValid(email)) {
                            emails.add(email);
                        } else {
                            showToast(R.string.invalid_email);
                            return;
                        }
                    }
                    if (!emails.isEmpty()) {
                        ApiHandler.post(ApiConstants.INVITE, emails, Void.class, new Response.Listener<Void>() {
                            @Override
                            public void onResponse(Void response) {
                                int remaining = invitationCount - emails.size();
                                updateRemainingInvitationCount(remaining);
                                showToast(R.string.email_sent);
                            }
                        }, null);
                    }
                }
            });
        } else {
            findViewById(R.id.invite_without_quota).setVisibility(View.VISIBLE);
            findViewById(R.id.email_list).setVisibility(View.GONE);
            findViewById(R.id.invite_with_quota).setVisibility(View.GONE);
            findViewById(R.id.send).setVisibility(View.GONE);
        }
    }

    private void updateRemainingInvitationCount(int count) {
        ((TextView) findViewById(R.id.count)).setText(count + "");
    }
}
