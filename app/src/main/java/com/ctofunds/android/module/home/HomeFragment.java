package com.ctofunds.android.module.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctofunds.android.BaseFragment;
import com.ctofunds.android.R;
import com.ctofunds.android.constants.Constants;
import com.ctofunds.android.module.login.LoginActivity;
import com.ctofunds.android.module.topic.TopicDetailActivity;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class HomeFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        root.findViewById(R.id.expert_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        root.findViewById(R.id.login_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeFragment.this.getActivity(), LoginActivity.class);
                getActivity().startActivityForResult(intent, Constants.REQUEST_LOGIN);

            }
        });
        root.findViewById(R.id.topic_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("id", 4);
                intent.setClass(HomeFragment.this.getActivity(), TopicDetailActivity.class);
                getActivity().startActivity(intent);
            }
        });
        return root;
    }

}
