package com.example.snehamishra.auth2login;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class TabbedHomeScreen extends AppCompatActivity {

    private Button navigateToLogout;
    private sectionPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_home_screen);
        mSectionsPagerAdapter = new sectionPageAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);

        setupViewPager(mViewPager);
        navigateToLogout = findViewById(R.id.bn_back);
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bn_back:
                loadSignOutPage();
                break;

        }

    }

    private void loadSignOutPage() {

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

    }


    private void setupViewPager(ViewPager viewPager){
        sectionPageAdapter adapter = new sectionPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1(), "HOME");
        adapter.addFragment(new Tab2(), "ABOUT");
        adapter.addFragment(new Tab3(), "CONTACT US");

        viewPager.setAdapter(adapter);
    }
}

