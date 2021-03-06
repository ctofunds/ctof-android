package com.ctofunds.android.module.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ctof.sms.api.Employee;
import com.ctof.sms.api.Expert;
import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.Constants;
import com.ctofunds.android.module.home.HomeFragment;
import com.ctofunds.android.module.message.MessageFragment;
import com.ctofunds.android.module.profile.EmployeeProfileFragment;
import com.ctofunds.android.module.profile.ExpertProfileFragment;
import com.ctofunds.android.module.setting.ExpertSettingActivity;
import com.ctofunds.android.module.topic.TopicFragment;
import com.ctofunds.android.service.AccountService;
import com.ctofunds.android.utility.Environment;
import com.google.common.collect.Lists;

import java.util.List;

public class MainActivity extends BaseActivity {

    private Toolbar myToolbar;
    private List<View> tabButtons = Lists.newArrayList();
    private ImageView toolbarImage;
    private TextView toolbarText;
    private TextView toolbarTextRight;

    private BroadcastReceiver logoutReceiver;
    private View homeView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        myToolbar.setContentInsetsAbsolute(0, 0);
        if (myToolbar != null) {
            setSupportActionBar(myToolbar);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);

        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        ActionBar.LayoutParams layout = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View title = inflater.inflate(R.layout.layout_main_title_bar, null);
        getSupportActionBar().setCustomView(title, layout);
        toolbarImage = (ImageView) title.findViewById(R.id.toolbar_image);
        toolbarTextRight = (TextView) title.findViewById(R.id.toolbar_right_image);
        toolbarText = (TextView) title.findViewById(R.id.toolbar_title);

        tabButtons.clear();
        homeView = findViewById(R.id.home);
        tabButtons.add(homeView);
        tabButtons.add(findViewById(R.id.messages));
        tabButtons.add(findViewById(R.id.topic));
        tabButtons.add(findViewById(R.id.profile));

        homeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTabClicked(v);
            }
        });
        findViewById(R.id.messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTabClicked(v);
            }
        });
        findViewById(R.id.topic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTabClicked(v);
            }
        });
        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTabClicked(v);
            }
        });
        handleTabClicked(homeView);
        refreshNavigationBar();

        logoutReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.d(getTag(), "action:" + intent.getAction() + " received");
                refreshNavigationBar();
                handleTabClicked(homeView);
            }
        };
        registerReceiver(logoutReceiver, new IntentFilter(Constants.LOGOUT));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(logoutReceiver);
    }

    private void refreshNavigationBar() {
        AccountService accountService = SmsApplication.getAccountService();
        if (accountService.getEmployeeAccount() != null) {
            findViewById(R.id.place_holder).setVisibility(View.INVISIBLE);
            findViewById(R.id.ask).setVisibility(View.VISIBLE);
            Button askButton = (Button) findViewById(R.id.ask);
            askButton.getLayoutParams().height = Environment.getInstance().screenWidthPixels() / 5;
            askButton.getLayoutParams().width = Environment.getInstance().screenWidthPixels() / 5;
            askButton.requestLayout();
        } else {
            findViewById(R.id.place_holder).setVisibility(View.GONE);
            findViewById(R.id.ask).setVisibility(View.GONE);
        }
    }

    private void resetSelectedStatus(View selected) {
        for (View view : tabButtons) {
            view.setSelected(view == selected);
        }
        if (selected == null && !tabButtons.isEmpty()) {
            tabButtons.get(0).setSelected(true);
        }
    }

    private Fragment getFragment(int viewId) {
        AccountService accountService = SmsApplication.getAccountService();
        Expert expertAccount = accountService.getExpertAccount();
        Employee employeeAccount = accountService.getEmployeeAccount();
        switch (viewId) {
            case R.id.home:
                toolbarImage.setVisibility(View.VISIBLE);
                toolbarTextRight.setVisibility(View.GONE);
                toolbarText.setVisibility(View.GONE);
                return new HomeFragment();
            case R.id.messages:
                toolbarImage.setVisibility(View.GONE);
                toolbarTextRight.setVisibility(View.GONE);
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(R.string.messages);
                return new MessageFragment();
            case R.id.topic:
                toolbarImage.setVisibility(View.GONE);
                toolbarTextRight.setVisibility(View.GONE);
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(R.string.topic);
                return new TopicFragment();
            case R.id.profile:
                if (!accountService.hasLogin()) {
                    showToast("还未登录");
                    return null;
                }
                toolbarImage.setVisibility(View.GONE);
                toolbarTextRight.setVisibility(View.VISIBLE);
                toolbarTextRight.setText(R.string.action_settings);
                toolbarText.setVisibility(View.VISIBLE);
                toolbarText.setText(R.string.my_profile);
                if (expertAccount != null) {
                    ExpertProfileFragment expertProfileFragment = new ExpertProfileFragment();
                    Bundle args = new Bundle();
                    args.putLong("id", expertAccount.getId());
                    expertProfileFragment.setArguments(args);
                    toolbarTextRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this.getApplicationContext(), ExpertSettingActivity.class);
                            startActivity(intent);
                        }
                    });
                    return expertProfileFragment;
                } else if (employeeAccount != null) {
                    toolbarTextRight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showToast("未完成,进入企业设置页面");
                        }
                    });
                    return new EmployeeProfileFragment();
                }
            default:
                return null;
        }
    }

    private void handleTabClicked(View view) {
        if (view.isSelected()) {
            return;
        }
        Fragment fragment = getFragment(view.getId());
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment, fragment);
            fragmentTransaction.commitAllowingStateLoss();
            resetSelectedStatus(view);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setCancelable(true).setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setTitle(R.string.exit_confirm).create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.REQUEST_LOGIN:
                refreshNavigationBar();
                return;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
