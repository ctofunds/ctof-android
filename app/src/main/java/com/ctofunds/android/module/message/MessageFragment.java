package com.ctofunds.android.module.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ctofunds.android.BaseFragment;
import com.ctofunds.android.R;

/**
 * Created by qianhao.zhou on 12/17/15.
 */
public class MessageFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_messages, null);
    }
}
