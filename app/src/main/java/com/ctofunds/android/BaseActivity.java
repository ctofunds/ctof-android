package com.ctofunds.android;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ctofunds.android.event.TokenExpireEvent;
import com.ctofunds.android.module.login.LoginActivity;

/**
 * Created by qianhao.zhou on 12/16/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String getTag() {
        return this.getClass().getSimpleName();
    }

    private volatile boolean isActive;

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmsApplication.getEventBus().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SmsApplication.getEventBus().unregister(this);
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
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getTag(), "requestCode:" + requestCode + " resultCode:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected final boolean isActive() {
        return this.isActive;
    }

    public final void onEventMainThread(TokenExpireEvent event) {
        if (isActive()) {
            dismissProgressDialog();
            new AlertDialog.Builder(this).setCancelable(true).setTitle("请重新登录").setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }
}
