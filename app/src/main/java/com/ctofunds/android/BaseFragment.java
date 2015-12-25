package com.ctofunds.android;

import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * Created by qianhao.zhou on 12/22/15.
 */
public abstract class BaseFragment extends Fragment {

    protected void showProgressDialog(String title, String msg) {
        ((BaseActivity) getActivity()).showProgressDialog(title, msg);
    }

    protected void dismissProgressDialog() {
        ((BaseActivity) getActivity()).dismissProgressDialog();
    }

    protected void showToast(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(getActivity().getApplicationContext(), getActivity().getApplicationContext().getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }
}
