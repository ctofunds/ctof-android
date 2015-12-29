/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package com.ctofunds.android.module.topic;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.widget.Banner.BaseWebActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @Description: 图片适配器
 * @author http://blog.csdn.net/finddreams
 */ 
public class BannerPagerAdapter extends BaseAdapter {

	private Context context;
	private List<BannerPageObj> dataList;
	private int size;
	private boolean isInfiniteLoop;



	public BannerPagerAdapter(Context context, List<BannerPageObj> dataList) {
		this.context = context;
		this.dataList = dataList;
		if (dataList != null) {
			this.size = dataList.size();
		}
		isInfiniteLoop = false;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : dataList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		NetworkImageView imageView;
		TextView titleLabel;
		TextView subTitleLabel;

		BannerPageObj data = dataList.get(getPosition(position));

		boolean firstInit = false;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.item_home_banner, null);
			firstInit = true;
		}

		imageView = (NetworkImageView) view.findViewById(R.id.image_view);
		if (firstInit) {
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		}
		titleLabel = (TextView) view.findViewById(R.id.title_label);
		subTitleLabel = (TextView) view.findViewById(R.id.subtitle_label);
		imageView.setImageUrl(data.imageUrl, SmsApplication.getImageLoader());
		titleLabel.setText(data.title);
		subTitleLabel.setText(data.subTitle);


//		holder.imageView.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				String url = linkUrlArray.get(BannerPagerAdapter.this
//						.getPosition(position));
//				String title = urlTitlesList.get(BannerPagerAdapter.this
//						.getPosition(position));
//				/*
//				 * if (TextUtils.isEmpty(url)) {
//				 * holder.imageView.setEnabled(false); return; }
//				 */
//				Bundle bundle = new Bundle();
//
//				bundle.putString("url", url);
//				bundle.putString("title", title);
//				Intent intent = new Intent(context, BaseWebActivity.class);
//				intent.putExtras(bundle);
//
//				context.startActivity(intent);
//				Toast.makeText(context, "点击了第" + getPosition(position) + "美女",
//						Toast.LENGTH_SHORT).show();
//
//			}
//		});

		return view;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public BannerPagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

}
