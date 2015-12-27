package com.ctofunds.android.module.topic;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ctofunds.android.BaseFragment;
import com.ctofunds.android.R;
import com.ctofunds.android.utility.DisplayUtil;
import com.ctofunds.android.widget.Banner.CircleFlowIndicator;
import com.ctofunds.android.widget.Banner.ImagePagerAdapter;
import com.ctofunds.android.widget.Banner.ViewFlow;

import java.util.ArrayList;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class TopicFragment extends BaseFragment {

    private ViewFlow mViewFlow;
    private CircleFlowIndicator mFlowIndicator;
    private ArrayList<String> imageUrlList = new ArrayList<String>();
    ArrayList<String> linkUrlArray= new ArrayList<String>();
    ArrayList<String> titleList= new ArrayList<String>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_topic, null);
        initView(root);

        imageUrlList.add("http://b.hiphotos.baidu.com/image/pic/item/d01373f082025aaf95bdf7e4f8edab64034f1a15.jpg");
        imageUrlList.add("http://g.hiphotos.baidu.com/image/pic/item/6159252dd42a2834da6660c459b5c9ea14cebf39.jpg");
        imageUrlList.add("http://d.hiphotos.baidu.com/image/pic/item/adaf2edda3cc7cd976427f6c3901213fb80e911c.jpg");
        linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/44301359");
        linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/43486527");
        linkUrlArray.add("http://blog.csdn.net/finddreams/article/details/43194799");
        titleList.add("Android开发面试经——4.常见Android进阶笔试题（更新中...）");
        titleList.add("Android控件GridView之仿支付宝钱包首页带有分割线的GridView九宫格的完美实现");
        titleList.add("Android动画之仿美团加载数据等待时，小人奔跑进度动画对话框（附顺丰快递员奔跑效果） ");
        initBanner(imageUrlList);

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
    }

    public int DPFromPixels(int pixels){
        return (int)(pixels / getResources().getDisplayMetrics().density);
    }

    private void initBanner(ArrayList<String> imageUrlList) {

        mViewFlow.setAdapter(new ImagePagerAdapter(getContext(), imageUrlList,
                linkUrlArray, titleList).setInfiniteLoop(true));
        mViewFlow.setmSideBuffer(imageUrlList.size()); // 实际图片张数，
        // 我的ImageAdapter实际图片张数为3

        mViewFlow.setFlowIndicator(mFlowIndicator);
        mViewFlow.setTimeSpan(4500);
        mViewFlow.setSelection(imageUrlList.size() * 1000); // 设置初始位置
        mViewFlow.startAutoFlowTimer(); // 启动自动播放
    }

    private void resizeFragment(int newWidth, int newHeight) {
        View view = getView();
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(newWidth, newHeight);
        view.setLayoutParams(p);
        view.requestLayout();
    }


}
