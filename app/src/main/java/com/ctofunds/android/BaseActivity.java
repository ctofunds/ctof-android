package com.ctofunds.android;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by qianhao.zhou on 12/16/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String getTag() {
        return this.getClass().getSimpleName();
    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
