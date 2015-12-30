package com.ctofunds.android.module.topic;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    private ListView mTodayStartListView;
    private ArrayList<TodayStarObj> mTodayStarList = new ArrayList<TodayStarObj>();
    private LayoutInflater mInflater;
    private TodayStarAdapter mTodayStartAdapter;
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


        fakeTodayStarList();
        mTodayStartAdapter.notifyDataSetChanged();

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

        mTodayStartListView = (ListView) root.findViewById(R.id.todayStarListView);
        mTodayStartAdapter = new TodayStarAdapter();
        mTodayStartListView.setAdapter(mTodayStartAdapter);
    }

    private void initBanner() {

        mViewFlow.setAdapter(new BannerPagerAdapter(getContext(), mBannerList).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(mBannerList.size()); // 实际图片张数，

        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
//        mViewFlow.startAutoFlowTimer(); // 启动自动播放
    }

    private void fakeTodayStarList() {
        mTodayStarList.add(new TodayStarObj("https://wwc.alicdn.com/avatar/getAvatar.do?userId=559393633&width=60&height=60&type=sns", "迷宫", "startup", "“HiCTO高效解决了我们在开发中遇到的问题”", "行业:医疗互联网 规模:50人 B轮"));
        mTodayStarList.add(new TodayStarObj("https://wwc.alicdn.com/avatar/getAvatar.do?userId=2038635733&width=60&height=60&type=sns", "未来", "expert", "“HiCTO高效解决了我们在开发中遇到的问题”", "加入时间:2015年9月 已解决问题:10条"));
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

    class TodayStarAdapter extends BaseAdapter {

        private View moreItem;
        final int TYPE_ITEM = 0;
        final int TYPE_MORE = 1;

        public TodayStarAdapter() {
            moreItem = mInflater.inflate(R.layout.item_show_more, null);
            ((TextView)moreItem.findViewById(R.id.textLabel)).setText("查看全部");
        }

        @Override
        public int getCount() {
            return mTodayStarList.size()+1;
        }

        @Override
        public Object getItem(int position)
        {
            if (position<mTodayStarList.size()) {
                return mTodayStarList.get(position);
            } else {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            if (position<mTodayStarList.size()) {
                return TYPE_ITEM;
            } else {
                return TYPE_MORE;
            }
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int type = getItemViewType(position);

            if (type == TYPE_ITEM) {
                TodayStarViewHolder holder;
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_today_star, null);
                    holder = new TodayStarViewHolder();
                    holder.avatarImageView = (CircleImageView) convertView.findViewById(R.id.avatar_imageview);
                    holder.nameLabel = (TextView) convertView.findViewById(R.id.name_label);
                    holder.roleLabel = (TextView) convertView.findViewById(R.id.role_label);
                    holder.contentLabel = (TextView) convertView.findViewById(R.id.content_label);
                    holder.tagLabel = (TextView) convertView.findViewById(R.id.tag_label);
                    convertView.setTag(holder);
                } else {
                    holder = (TodayStarViewHolder)convertView.getTag();
                }
                TodayStarObj data = mTodayStarList.get(position);
                holder.avatarImageView.setImageUrl(data.avatarImageUrl, SmsApplication.getImageLoader());
                holder.nameLabel.setText(data.name);
                if (data.role.equals("startup")) {
                    holder.roleLabel.setText("企业");
                    holder.roleLabel.setBackgroundColor(Color.parseColor("#AADE60"));
                } else {
                    holder.roleLabel.setText("专家");
                    holder.roleLabel.setBackgroundColor(Color.parseColor("#70C7EF"));
                }
                holder.contentLabel.setText(data.content);
                holder.tagLabel.setText(data.tagString);
                return convertView;
            } else {
                return moreItem;
            }
        }

        private class TodayStarViewHolder {
            public CircleImageView avatarImageView;
            public TextView nameLabel;
            public TextView roleLabel;
            public TextView contentLabel;
            public TextView tagLabel;
        }
    }
}
