package com.ctofunds.android.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.ctofunds.android.R;
import com.ctofunds.android.login.LoginActivity;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_home, null);
        root.findViewById(R.id.expert_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomeFragment.this.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}
