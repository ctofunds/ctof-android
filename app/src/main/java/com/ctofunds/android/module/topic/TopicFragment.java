package com.ctofunds.android.module.topic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ctofunds.android.BaseFragment;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.widget.Banner.CircleFlowIndicator;
import com.ctofunds.android.widget.Banner.ViewFlow;
import com.ctofunds.android.widget.CircleImageView;

import java.util.ArrayList;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class TopicFragment extends BaseFragment {

    private ViewFlow mViewFlow;
    private CircleFlowIndicator mFlowIndicator;
    private ArrayList<String> imageUrlList = new ArrayList<String>();
    private LinearLayout mTodayStartLayout;
    private ArrayList<TodayStarObj> mTodayStarList = new ArrayList<TodayStarObj>();
    private LinearLayout mTopTopicLayout;
    private ArrayList<TodayStarObj> mTopTopicList = new ArrayList<TodayStarObj>();
    private LayoutInflater mInflater;
    private ArrayList<BannerPageObj> mBannerList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflater = inflater;
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_topic, null);
        initView(root);


        BannerPageObj data = new BannerPageObj();
        data.imageUrl = "http://b.hiphotos.baidu.com/image/pic/item/d01373f082025aaf95bdf7e4f8edab64034f1a15.jpg";
        data.title = "“HiCTO解决了初创互联网企业的痛点！”";
        data.subTitle = "Max Levechin, CTO of paypal";
        mBannerList.add(data);
        data = new BannerPageObj();
        data.imageUrl = "http://g.hiphotos.baidu.com/image/pic/item/6159252dd42a2834da6660c459b5c9ea14cebf39.jpg";
        data.title = "“解决了初创互联网企业的痛点！”";
        data.subTitle = "Levechin, CTO of paypal";
        mBannerList.add(data);
        data = new BannerPageObj();
        data.imageUrl = "http://d.hiphotos.baidu.com/image/pic/item/adaf2edda3cc7cd976427f6c3901213fb80e911c.jpg";
        data.title = "“初创互联网企业的痛点！”";
        data.subTitle = "CTO of paypal";
        mBannerList.add(data);
        initBanner();


        fakeData();
        updateView();

        return root;
    }

    private void initView(ViewGroup root) {
        mViewFlow = (ViewFlow) root.findViewById(R.id.viewflow);
        mFlowIndicator = (CircleFlowIndicator) root.findViewById(R.id.viewflowindic);

        // 设定首页滚动广告条的高度
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = new Point();
        wm.getDefaultDisplay().getSize(screenSize);
        FrameLayout viewFlowWraper = (FrameLayout)root.findViewById(R.id.viewflowwraper);
        int pxHeight = 567*screenSize.x/750;
        viewFlowWraper.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pxHeight));
        viewFlowWraper.requestLayout();

        mTodayStartLayout = (LinearLayout) root.findViewById(R.id.today_star_layout);
        mTopTopicLayout = (LinearLayout) root.findViewById(R.id.top_topic_layout);
    }

    private void initBanner() {

        mViewFlow.setAdapter(new BannerPagerAdapter(getContext(), mBannerList).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(mBannerList.size()); // 实际图片张数，

        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
//        mViewFlow.startAutoFlowTimer(); // 启动自动播放
    }

    private void fakeData() {
        mTodayStarList.add(new TodayStarObj("https://wwc.alicdn.com/avatar/getAvatar.do?userId=559393633&width=60&height=60&type=sns", "迷宫", "startup", "“HiCTO高效解决了我们在开发中遇到的问题”", "行业:医疗互联网 规模:50人 B轮"));
        mTodayStarList.add(new TodayStarObj("https://wwc.alicdn.com/avatar/getAvatar.do?userId=2038635733&width=60&height=60&type=sns", "未来", "expert", "“HiCTO高效解决了我们在开发中遇到的问题”", "加入时间:2015年9月 已解决问题:10条"));

        mTopTopicList.add(new TodayStarObj("https://wwc.alicdn.com/avatar/getAvatar.do?userId=559393633&width=60&height=60&type=sns", "迷宫", "startup", "“HiCTO高效解决了我们在开发中遇到的问题”", "行业:医疗互联网 规模:50人 B轮"));
        mTopTopicList.add(new TodayStarObj("https://wwc.alicdn.com/avatar/getAvatar.do?userId=2038635733&width=60&height=60&type=sns", "未来", "expert", "“HiCTO高效解决了我们在开发中遇到的问题”", "加入时间:2015年9月 已解决问题:10条"));
    }

    private void updateView() {
        if (mTodayStarList.size()>0) {
            mTodayStartLayout.setVisibility(View.VISIBLE);
            while (mTodayStartLayout.getChildCount()>1) {
                mTodayStartLayout.removeViewAt(mTodayStartLayout.getChildCount()-1);
            }

            for (int i=0; (i<mTodayStarList.size() && i<2); i++) {
                View viewItem = mInflater.inflate(R.layout.item_today_star, null);
                CircleImageView avatarImageView = (CircleImageView) viewItem.findViewById(R.id.avatar_imageview);
                TextView nameLabel = (TextView) viewItem.findViewById(R.id.name_label);
                TextView roleLabel = (TextView) viewItem.findViewById(R.id.role_label);
                TextView contentLabel = (TextView) viewItem.findViewById(R.id.content_label);
                TextView tagLabel = (TextView) viewItem.findViewById(R.id.tag_label);

                TodayStarObj data = mTodayStarList.get(i);
                avatarImageView.setImageUrl(data.avatarImageUrl, SmsApplication.getImageLoader());
                nameLabel.setText(data.name);
                if (data.role.equals("startup")) {
                    roleLabel.setText("企业");
                    roleLabel.setBackgroundColor(Color.parseColor("#AADE60"));
                } else {
                    roleLabel.setText("专家");
                    roleLabel.setBackgroundColor(Color.parseColor("#70C7EF"));
                }
                contentLabel.setText(data.content);
                tagLabel.setText(data.tagString);

                mTodayStartLayout.addView(viewItem);
            }

            View moreItem = mInflater.inflate(R.layout.item_show_more, null);
            ((TextView)moreItem.findViewById(R.id.textLabel)).setText("查看全部");
            mTodayStartLayout.addView(moreItem);
        } else {
            mTodayStartLayout.setVisibility(View.GONE);
        }


        if (mTopTopicList.size()>0) {
            mTopTopicLayout.setVisibility(View.VISIBLE);
            while (mTopTopicLayout.getChildCount()>1) {
                mTopTopicLayout.removeViewAt(mTopTopicLayout.getChildCount()-1);
            }

            for (int i=0; (i<mTopTopicList.size() && i<2); i++) {
                View viewItem = mInflater.inflate(R.layout.item_today_star, null);
                CircleImageView avatarImageView = (CircleImageView) viewItem.findViewById(R.id.avatar_imageview);
                TextView nameLabel = (TextView) viewItem.findViewById(R.id.name_label);
                TextView roleLabel = (TextView) viewItem.findViewById(R.id.role_label);
                TextView contentLabel = (TextView) viewItem.findViewById(R.id.content_label);
                TextView tagLabel = (TextView) viewItem.findViewById(R.id.tag_label);

                TodayStarObj data = mTopTopicList.get(i);
                avatarImageView.setImageUrl(data.avatarImageUrl, SmsApplication.getImageLoader());
                nameLabel.setText(data.name);
                if (data.role.equals("startup")) {
                    roleLabel.setText("企业");
                    roleLabel.setBackgroundColor(Color.parseColor("#AADE60"));
                } else {
                    roleLabel.setText("专家");
                    roleLabel.setBackgroundColor(Color.parseColor("#70C7EF"));
                }
                contentLabel.setText(data.content);
                tagLabel.setText(data.tagString);

                mTopTopicLayout.addView(viewItem);
            }

            View moreItem = mInflater.inflate(R.layout.item_show_more, null);
            ((TextView)moreItem.findViewById(R.id.textLabel)).setText("更多精彩问答");
            mTopTopicLayout.addView(moreItem);
        } else {
            mTopTopicLayout.setVisibility(View.GONE);
        }
    }

    class TodayStarObj {
        public String avatarImageUrl;
        public String name;
        public String role;
        public String content;
        public String tagString;

        public TodayStarObj(String avatarImageUrl, String name, String role, String content, String tagString) {
            this.avatarImageUrl = avatarImageUrl;
            this.name = name;
            this.role = role;
            this.content = content;
            this.tagString = tagString;
        }
    }

}
