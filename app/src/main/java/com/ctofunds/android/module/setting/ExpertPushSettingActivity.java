package com.ctofunds.android.module.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.ctof.sms.api.Expert;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;

/**
 * Created by qianhao.zhou on 12/29/15.
 */
public class ExpertPushSettingActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        ActionBar supportActionBar = getSupportActionBar();
        ((TextView) toolbar.findViewById(R.id.toolbar_title)).setText(R.string.modify_push_settings);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.arrow_left);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateView(SmsApplication.getAccountService().getExpertAccount());
    }

    private void updateView(Expert expert) {
        final View notifyNewTopicImmediately = findViewById(R.id.notify_new_topic_immediately);
        final View notifyNewTopicOnFreeTime = findViewById(R.id.notify_new_topic_on_free_time);
        ((TextView) notifyNewTopicImmediately.findViewById(R.id.label)).setText(R.string.notify_immediately);
        ((TextView) notifyNewTopicOnFreeTime.findViewById(R.id.label)).setText(R.string.notify_on_freetime);
        if (expert.getNotifyOnlyFreeTime()) {
            notifyNewTopicOnFreeTime.findViewById(R.id.icon).setVisibility(View.VISIBLE);
            notifyNewTopicImmediately.findViewById(R.id.icon).setVisibility(View.GONE);
        } else {
            notifyNewTopicOnFreeTime.findViewById(R.id.icon).setVisibility(View.GONE);
            notifyNewTopicImmediately.findViewById(R.id.icon).setVisibility(View.VISIBLE);
        }
        notifyNewTopicOnFreeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyNewTopicImmediately.findViewById(R.id.icon).setVisibility(View.GONE);
                notifyNewTopicOnFreeTime.findViewById(R.id.icon).setVisibility(View.VISIBLE);
            }
        });
        notifyNewTopicImmediately.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyNewTopicImmediately.findViewById(R.id.icon).setVisibility(View.VISIBLE);
                notifyNewTopicOnFreeTime.findViewById(R.id.icon).setVisibility(View.GONE);
            }
        });

        ((Switch) findViewById(R.id.notify_new_topic).findViewById(R.id.switch_button)).setChecked(expert.getNotifyNewTopicByPush());

        ((TextView) findViewById(R.id.notify_topic_got_accepted_by_email).findViewById(R.id.label)).setText(R.string.notify_by_email);
        ((Switch) findViewById(R.id.notify_topic_got_accepted_by_email).findViewById(R.id.switch_button)).setChecked(expert.getNotifyReplyAcceptedByEmail());
        ((TextView) findViewById(R.id.notify_topic_got_accepted_by_push).findViewById(R.id.label)).setText(R.string.notify_by_push);
        ((Switch) findViewById(R.id.notify_topic_got_accepted_by_push).findViewById(R.id.switch_button)).setChecked(expert.getNotifyReplyAcceptedByPush());

        ((TextView) findViewById(R.id.notify_new_comment_by_email).findViewById(R.id.label)).setText(R.string.notify_by_email);
        ((Switch) findViewById(R.id.notify_new_comment_by_email).findViewById(R.id.switch_button)).setChecked(expert.getNotifyCommentNewCommentByEmail() && expert.getNotifyReplyNewCommentByEmail());
        ((TextView) findViewById(R.id.notify_new_comment_by_push).findViewById(R.id.label)).setText(R.string.notify_by_push);
        ((Switch) findViewById(R.id.notify_new_comment_by_push).findViewById(R.id.switch_button)).setChecked(expert.getNotifyCommentNewCommentByPush() && expert.getNotifyReplyNewCommentByPush());

    }
}
