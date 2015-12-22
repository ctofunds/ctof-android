package com.ctofunds.android.module.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.ctofunds.android.BaseActivity;
import com.ctofunds.android.R;
import com.ctofunds.android.SmsApplication;
import com.ctofunds.android.constants.Constants;
import com.ctofunds.android.module.home.HomeFragment;
import com.ctofunds.android.module.message.MessageFragment;
import com.ctofunds.android.module.profile.ProfileFragment;
import com.ctofunds.android.module.topic.TopicFragment;
import com.ctofunds.android.service.AccountService;
import com.ctofunds.android.utility.Environment;
import com.google.common.collect.Lists;

import java.util.List;

public class MainActivity extends BaseActivity {

    private Toolbar myToolbar;
    private List<View> tabButtons = Lists.newArrayList();

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
        View overlay = inflater.inflate(R.layout.layout_main_title_bar, null);
        getSupportActionBar().setCustomView(overlay, layout);

        tabButtons.clear();
        tabButtons.add(findViewById(R.id.home));
        tabButtons.add(findViewById(R.id.messages));
        tabButtons.add(findViewById(R.id.topic));
        tabButtons.add(findViewById(R.id.profile));

        findViewById(R.id.home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectedStatus(v);
                showFragment(FRAGMENT_HOME);
            }
        });
        findViewById(R.id.messages).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectedStatus(v);
                showFragment(FRAGMENT_MESSAGE);
            }
        });
        findViewById(R.id.topic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectedStatus(v);
                showFragment(FRAGMENT_TOPIC);
            }
        });
        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSelectedStatus(v);
                showFragment(FRAGMENT_PROFILE);
            }
        });
        showFragment(FRAGMENT_HOME);
        resetSelectedStatus(null);
        refreshNavigationBar();
    }

    private void refreshNavigationBar() {
        AccountService accountService = SmsApplication.getAccountService();
        if (accountService.getExpertAccount() != null) {
            findViewById(R.id.place_holder).setVisibility(View.GONE);
            findViewById(R.id.ask).setVisibility(View.GONE);
        } else {
            findViewById(R.id.place_holder).setVisibility(View.INVISIBLE);
            findViewById(R.id.ask).setVisibility(View.VISIBLE);
            Button askButton = (Button) findViewById(R.id.ask);
            askButton.getLayoutParams().height = Environment.getInstance().screenWidthPixels() / 5;
            askButton.getLayoutParams().width = Environment.getInstance().screenWidthPixels() / 5;
            askButton.requestLayout();
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

    private void showFragment(int type) {
        Fragment fragment = null;
        switch (type) {
            case FRAGMENT_HOME:
                fragment = new HomeFragment();
                break;
            case FRAGMENT_MESSAGE:
                fragment = new MessageFragment();
                break;
            case FRAGMENT_TOPIC:
                fragment = new TopicFragment();
                break;
            case FRAGMENT_PROFILE:
                fragment = new ProfileFragment();
                break;
        }
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragment, fragment);
            fragmentTransaction.commit();
        }
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
