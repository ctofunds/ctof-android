package com.ctofunds.android.module.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.Constants;

/**
 * Created by qianhao.zhou on 12/25/15.
 */
public class ExpertSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.action_settings);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((TextView) findViewById(R.id.invite).findViewById(R.id.label)).setText(R.string.invite);
        ((TextView) findViewById(R.id.modify_profile).findViewById(R.id.label)).setText(R.string.modify_profile);
        ((TextView) findViewById(R.id.modify_password).findViewById(R.id.label)).setText(R.string.modify_password);
        ((TextView) findViewById(R.id.modify_push_setting).findViewById(R.id.label)).setText(R.string.modify_push_settings);
        ((TextView) findViewById(R.id.about_hicto).findViewById(R.id.label)).setText(R.string.about_hicto);

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ExpertSettingActivity.this).setCancelable(true).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SmsApplication.getAccountService().clearAccount();
                        Intent intent = new Intent();
                        intent.setAction(Constants.LOGOUT);
                        sendBroadcast(intent);
                        finish();
                    }
                }).setTitle(R.string.logout_confirm).create().show();
            }
        });
        findViewById(R.id.about_hicto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ExpertSettingActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.invite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ExpertSettingActivity.this, InviteExpertActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.modify_push_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ExpertSettingActivity.this, ExpertPushSettingActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.modify_profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ExpertSettingActivity.this, EditExpertProfileActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.modify_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ExpertSettingActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}
