package com.ctofunds.android.main;

import android.content.Context;
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
import com.ctofunds.android.home.HomeFragment;
import com.ctofunds.android.message.MessageFragment;
import com.ctofunds.android.profile.ProfileFragment;
import com.ctofunds.android.topic.TopicFragment;
import com.ctofunds.android.utility.Environment;
import com.google.common.collect.Lists;

import java.util.List;

public class MainActivity extends BaseActivity {

    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_MESSAGE = 2;
    private static final int FRAGMENT_TOPIC = 3;
    private static final int FRAGMENT_PROFILE = 4;

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
        Button askButton = (Button) findViewById(R.id.ask);
        askButton.getLayoutParams().height = Environment.getInstance().screenWidthPixels() / 5;
        askButton.getLayoutParams().width = Environment.getInstance().screenWidthPixels() / 5;
        askButton.requestLayout();
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
}
