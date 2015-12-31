package com.ctofunds.android;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void throwIfNotOnMainThread() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            Log.e(getTag(), "operation must be invoked from the main thread.");
            throw new IllegalStateException("operation must be invoked from the main thread.");
        }
    }

    private ProgressDialog progressDialog;

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int resId) {
        Toast.makeText(this, this.getApplicationContext().getResources().getString(resId), Toast.LENGTH_SHORT).show();
    }

    protected void showProgressDialog(final int resId) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(BaseActivity.this);
                }
                if (!progressDialog.isShowing()) {
                    progressDialog.setTitle(null);
                    progressDialog.setMessage(BaseActivity.this.getApplicationContext().getResources().getString(resId));
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }
        });
    }

    protected void showProgressDialog(final String title, final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(BaseActivity.this);
                }
                if (!progressDialog.isShowing()) {
                    progressDialog.setTitle(title);
                    progressDialog.setMessage(msg);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }
        });

    }

    protected void dismissProgressDialog() {
        progressDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getTag(), "requestCode:" + requestCode + " resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
