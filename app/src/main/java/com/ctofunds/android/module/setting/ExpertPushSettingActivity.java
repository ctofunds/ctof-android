package com.ctofunds.android.module.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ctof.sms.api.Expert;
import com.ctof.sms.api.UpdateExpertRequest;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;

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

    private void updateView(final Expert expert) {
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

        findViewById(R.id.confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateExpertRequest request = new UpdateExpertRequest();
                request.setNotifyNewTopicByPush(((Switch) findViewById(R.id.notify_new_topic).findViewById(R.id.switch_button)).isChecked());
                request.setNotifyOnlyFreeTime(notifyNewTopicOnFreeTime.findViewById(R.id.icon).getVisibility() == View.VISIBLE);
                request.setNotifyReplyAcceptedByEmail(((Switch) findViewById(R.id.notify_topic_got_accepted_by_email).findViewById(R.id.switch_button)).isChecked());
                request.setNotifyReplyAcceptedByPush(((Switch) findViewById(R.id.notify_topic_got_accepted_by_push).findViewById(R.id.switch_button)).isChecked());
                request.setNotifyCommentNewCommentByEmail(((Switch) findViewById(R.id.notify_new_comment_by_email).findViewById(R.id.switch_button)).isChecked());
                request.setNotifyReplyNewCommentByEmail(((Switch) findViewById(R.id.notify_new_comment_by_email).findViewById(R.id.switch_button)).isChecked());
                request.setNotifyCommentNewCommentByPush(((Switch) findViewById(R.id.notify_new_comment_by_push).findViewById(R.id.switch_button)).isChecked());
                request.setNotifyReplyNewCommentByPush(((Switch) findViewById(R.id.notify_new_comment_by_push).findViewById(R.id.switch_button)).isChecked());

                showProgressDialog(R.string.wait_tips);
                ApiHandler.put(String.format(ApiConstants.GET_EXPERT, expert.getId()), request, Expert.class, new Response.Listener<Expert>() {
                    @Override
                    public void onResponse(Expert response) {
                        SmsApplication.getAccountService().setExpertAccount(response);
                        dismissProgressDialog();
                        showToast(R.string.setting_updated);
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
