package com.ctofunds.android.home;

import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ctofunds.android.R;
import com.ctofunds.android.fragment.FragmentTab;

public class HomeActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("simple").setIndicator("Simple"),
                FragmentTab.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("detailed").setIndicator("Detailed"),
                FragmentTab.class, null);


    }
}
