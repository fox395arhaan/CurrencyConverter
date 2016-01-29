package com.currencyapp.currencyconverter.Temp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.currencyapp.currencyconverter.CurrencyFragment;
import com.currencyapp.currencyconverter.FavDeailsFragment;
import com.currencyapp.currencyconverter.R;
import com.currencyapp.currencyconverter.util.CountryUtil;

import java.util.ArrayList;
import java.util.List;

public class TempActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    public AppCompatTextView lastUpdated;
    public ImageView refresh;

    private int[] tabIcons = {
            R.drawable.ccex,
            R.drawable.like,

    };
    private boolean isOffline = false;

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        init();
        initToolBar();
    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;

    }

    private void getUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        isOffline = sharedPrefs.getBoolean("pref_offline", false);


    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserSettings();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:

                startActivity(new Intent(this, SettingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lastUpdated = (AppCompatTextView) toolbar.findViewById(R.id.lastUpdated);
        refresh = (ImageView) toolbar.findViewById(R.id.refresh);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        lastUpdated.setText(String.format("Last Updated %s", CountryUtil.getDateAndTime(this)));

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                if (fragment instanceof CurrencyFragment) {

                    if (!isOffline) {
                        CurrencyFragment currencyFragment = (CurrencyFragment) fragment;
                        currencyFragment.callWebServiceAll();
                    } else {

                        Toast.makeText(TempActivity.this, "Turn off the offline mode.", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });


    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrencyFragment(), "CURRENCY");
        adapter.addFragment(new FavDeailsFragment(), "FAVORITES");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void setLastUpdatedText() {

        lastUpdated.setText(String.format("Last Updated %s", CountryUtil.getDateAndTime(this)));

    }

}
