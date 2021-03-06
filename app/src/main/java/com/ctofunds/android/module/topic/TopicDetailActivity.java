package com.ctofunds.android.module.topic;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.ctof.sms.api.Code;
import com.ctof.sms.api.Reply;
import com.ctof.sms.api.ReplyPageableResult;
import com.ctof.sms.api.Startup;
import com.ctof.sms.api.Topic;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.ApiConstants;
import com.ctofunds.android.network.ApiHandler;
import com.ctofunds.android.utility.ImageUtils;
import com.ctofunds.android.widget.CircleImageView;
import com.ctofunds.android.widget.ListViewForScrollView;
import com.ctofunds.android.widget.adapater.AdapterItem;
import com.ctofunds.android.widget.adapater.DynamicLoadingAdapter;
import com.google.common.collect.Lists;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianhao.zhou on 1/5/16.
 */
public class TopicDetailActivity extends BaseActivity {

    private static final String ENCODING = "utf-8";
    private static final String MIME_TYPE = "text/html;charset=utf-8";

    private ArrayList<Reply> replies = Lists.newArrayList();

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

    private void updateTopicDetailView(final Topic topic) {
        final Startup startup = topic.getStartup();
        final Code city = SmsApplication.getCodeService().getCity();
        final Code domain = SmsApplication.getCodeService().getDomain();
        final Code investmentStatus = SmsApplication.getCodeService().getInvestmentStatus();
        final LayoutInflater layoutInflater = getLayoutInflater();
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

//        String html = SmsApplication.getMarkdownParser().toHtml(topic.getContent());
//        WebView webView = (WebView) findViewById(R.id.content);
//        webView.getSettings().setDefaultTextEncodingName(ENCODING);
//        webView.loadData(html, MIME_TYPE, null);

        if (topic.getResolvedReplyId() != null) {
            findViewById(R.id.container).setBackgroundColor(getResources().getColor(R.color.light_grey));
        } else {
            findViewById(R.id.container).setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }
        ((TextView) findViewById(R.id.cto_coins)).setText(topic.getCtoCoins().toString());
        ((TextView) findViewById(R.id.title)).setText(topic.getTitle());
        ((ExpandableTextView) findViewById(R.id.content)).setText(topic.getContent());
        ViewGroup tagContainer = (ViewGroup) findViewById(R.id.tags);
        List<String> tags = topic.getTags();
        if (tags != null && tags.size() > 0) {
            tagContainer.removeAllViews();
            for (int i = 0; i < tags.size(); ++i) {
                layoutInflater.inflate(R.layout.item_expertise, tagContainer, true);
            }
            for (int i = 0; i < tags.size(); ++i) {
                ((TextView) tagContainer.getChildAt(i)).setText(tags.get(i));
            }
        }

        ((TextView) findViewById(R.id.comment_count)).setText(getResources().getString(R.string.comment_count, topic.getCommentCount()));
        findViewById(R.id.comment_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("评论详情, topic id:" + topic.getId());
            }
        });
        String timeLabel = SmsApplication.getTimeUtils().toString(topic.getServerTime() - topic.getCreateTime());
        ((TextView) findViewById(R.id.time_label)).setText(timeLabel + getResources().getString(R.string.ago));

        initReplies(topic);
    }

    private void initReplies(Topic topic) {
        Integer replyCount = topic.getReplyCount();
        ((TextView) findViewById(R.id.reply_count)).setText(getResources().getString(R.string.expert_replies, replyCount));
        ListViewForScrollView replies = (ListViewForScrollView) findViewById(R.id.replies);
        replies.setFocusable(false);
        if (replyCount > 0) {
            ReplyItemAdapter adapter = new ReplyItemAdapter(getApplicationContext(), topic);
            replies.setAdapter(adapter);
            adapter.init();
            replies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (view.getTag() instanceof ViewHolder) {
                        showToast("show reply detail id:" + ((ViewHolder) view.getTag()).replyId);
                    }
                }
            });
        }
    }

    private static class ViewHolder {
        long replyId;
        CircleImageView authorAvatar;
        TextView authorName;
        ExpandableTextView content;
        TextView timeLabel;
        ViewGroup comments;
        TextView commentCount;
        ViewGroup resolvedCommentContainer;
        TextView resolvedComment;
        ImageView resolvedIcon;
    }

    private static class ReplyItemAdapter extends DynamicLoadingAdapter<Reply> {

        private volatile boolean endOfList = false;
        private int currentPage = 0;
        private final int pageSize = 10;
        private final Topic topic;

        public ReplyItemAdapter(Context context, Topic topic) {
            super(context);
            this.topic = topic;
        }

        @Override
        protected View onGetDataView(Reply reply, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView != null && convertView.getTag() instanceof ViewHolder) {
                viewHolder = (ViewHolder) convertView.getTag();
            } else {
                convertView = getLayoutInflater().inflate(R.layout.item_reply, null);
                viewHolder = new ViewHolder();
                viewHolder.authorAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
                viewHolder.authorName = (TextView) convertView.findViewById(R.id.name);
                viewHolder.content = (ExpandableTextView) convertView.findViewById(R.id.content);
                viewHolder.timeLabel = (TextView) convertView.findViewById(R.id.time_label);
                viewHolder.commentCount = (TextView) convertView.findViewById(R.id.comment_count);
                viewHolder.comments = (ViewGroup) convertView.findViewById(R.id.comments);
                viewHolder.resolvedCommentContainer = (ViewGroup) convertView.findViewById(R.id.resolved_comment_container);
                viewHolder.resolvedComment = (TextView) convertView.findViewById(R.id.resolved_comment);
                viewHolder.resolvedIcon = (ImageView) convertView.findViewById(R.id.resolved_icon);
                convertView.setTag(viewHolder);
            }
            if (reply.getAuthor().getAvatar() != null) {
                viewHolder.authorAvatar.setImageUrl(reply.getAuthor().getAvatar(), SmsApplication.getImageLoader());
            }
            viewHolder.replyId = reply.getId();
            viewHolder.authorName.setText(reply.getAuthor().getName());
            viewHolder.content.setText(reply.getContent());
            String time = SmsApplication.getTimeUtils().toString(reply.getServerTime() - reply.getCreateTime());
            viewHolder.timeLabel.setText(time + getContext().getResources().getString(R.string.ago));
            if (reply.getCommentCount() > 0) {
                viewHolder.comments.setVisibility(View.VISIBLE);
                viewHolder.commentCount.setText(getContext().getResources().getString(R.string.see_more_comments, reply.getCommentCount()));
            } else {
                viewHolder.comments.setVisibility(View.GONE);
            }
            long resolvedReplyId = topic.getResolvedReplyId() != null ? topic.getResolvedReplyId() : -1;
            if (resolvedReplyId == reply.getId()) {
                viewHolder.resolvedIcon.setVisibility(View.VISIBLE);
                if (topic.getResolvedComment() != null) {
                    viewHolder.resolvedCommentContainer.setVisibility(View.VISIBLE);
                    viewHolder.resolvedComment.setText(topic.getResolvedComment());
                } else {
                    viewHolder.resolvedCommentContainer.setVisibility(View.GONE);
                }
            } else {
                viewHolder.resolvedIcon.setVisibility(View.GONE);
                viewHolder.resolvedCommentContainer.setVisibility(View.GONE);
            }
            return convertView;
        }

        @Override
        protected View onGetProgressView(View convertView) {
            return getLayoutInflater().inflate(R.layout.item_list_loading, null);
        }

        @Override
        protected View onGetErrorView(View convertView) {
            return getLayoutInflater().inflate(R.layout.item_list_error, null);
        }

        @Override
        protected View onGetEmptyView(View convertView) {
            return getLayoutInflater().inflate(R.layout.item_list_empty, null);
        }

        @Override
        protected void onSubmitRequest() {
            ApiHandler.get(String.format(ApiConstants.REPLIES, topic.getId(), currentPage, pageSize), ReplyPageableResult.class, new Response.Listener<ReplyPageableResult>() {
                @Override
                public void onResponse(final ReplyPageableResult response) {
                    endOfList = response.getLast();
                    currentPage++;
                    onRequestSucceed();
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            addAll(response.getContent());
                        }
                    });
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    showError();
                    onRequestFailed();
                }
            });
        }

        @Override
        protected boolean hasMore() {
            AdapterItem.Type type = getType();
            if (type == AdapterItem.Type.ERROR) {
                return false;
            }
            if (type == AdapterItem.Type.EMPTY) {
                return false;
            }
            return !endOfList;
        }
    }

}
