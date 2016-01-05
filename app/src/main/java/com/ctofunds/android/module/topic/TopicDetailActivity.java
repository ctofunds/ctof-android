package com.ctofunds.android.module.topic;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.ctof.sms.api.Code;
import com.ctof.sms.api.Startup;
import com.ctof.sms.api.Topic;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.utility.ImageUtils;

/**
 * Created by qianhao.zhou on 1/5/16.
 */
public class TopicDetailActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.topic_detail);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final int id = getIntent().getIntExtra("id", -1);

        if (id > 0) {
            ApiHandler.get(String.format(ApiConstants.TOPIC, id), Topic.class, new Response.Listener<Topic>() {
                @Override
                public void onResponse(Topic response) {
                    updateTopicDetailView(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(getTag(), "topic id:" + id, error);
                    showUnableGetTopicDetail();
                }
            });
        } else {
            Log.e(getTag(), "invalid topic id:" + id);
            showUnableGetTopicDetail();
        }
    }

    private void showUnableGetTopicDetail() {
        new AlertDialog.Builder(this).setTitle(R.string.fail_to_get_topic_detail).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setCancelable(false).show();
    }

    private void updateTopicDetailView(Topic topic) {
        Startup startup = topic.getStartup();
        Code city = SmsApplication.getCodeService().getCity();
        Code domain = SmsApplication.getCodeService().getDomain();
        Code investmentStatus = SmsApplication.getCodeService().getInvestmentStatus();
        if (startup != null) {
            if (startup.getLogo() != null) {
                ((NetworkImageView) findViewById(R.id.startup_logo)).setImageUrl(ImageUtils.getAvatarUrl(startup.getLogo()), SmsApplication.getImageLoader());
            }
            ((TextView) findViewById(R.id.startup_name)).setText(startup.getName());
            ((TextView) findViewById(R.id.location)).setText(city.getMapping().get(startup.getCity().toString()));
            ((TextView) findViewById(R.id.investment_status)).setText(domain.getMapping().get(startup.getDomain().toString()));
            ((TextView) findViewById(R.id.domain)).setText(investmentStatus.getMapping().get(startup.getInvestmentStatus().toString()));
            ((TextView) findViewById(R.id.topic_count)).setText(startup.getTopicCount().toString());

        }
    }

}
