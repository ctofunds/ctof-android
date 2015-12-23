package com.ctofunds.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

    protected void showToast(int resId) {
        Toast.makeText(this, this.getApplicationContext().getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getTag(), "requestCode:" + requestCode + " resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
