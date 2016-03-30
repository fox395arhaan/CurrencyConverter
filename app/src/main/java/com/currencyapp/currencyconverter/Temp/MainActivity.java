package com.currencyapp.currencyconverter.Temp;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.currencyapp.currencyconverter.CurrencyFragment;
import com.currencyapp.currencyconverter.FavDeailsFragment;
import com.currencyapp.currencyconverter.R;
import com.currencyapp.currencyconverter.util.CountryUtil;
import com.currencyapp.currencyconverter.widget.CustomTextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Node;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MainActivity extends AppCompatActivity {


    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    public CustomTextView lastUpdated;
    public ImageView refresh;

    private int[] tabIcons = {
            R.drawable.ccex,
            R.drawable.ic_christmas_star,

    };
    private boolean isOffline = false;

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        //loadAD();
        init();
        initToolBar();
        Admob();
        SQLiteStudioService.instance().start(this);
        SQLiteStudioService.instance().setPort(9999);
        SQLiteStudioService.instance().addIpToWhiteList("192.168.0.101");
        SQLiteStudioService.instance().setPassword("123");


    }

    private void init() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupTabLayout(tabLayout);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#666666"));
        //tabLayout.setupWithViewPager(viewPager);
        //setupTabIcons();

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
               try {

                SharedPreferences settings = getSharedPreferences("PREFS_NAME", 0);
                boolean mboolean = settings.getBoolean("FIRST_RUN", false);
                if (!mboolean) {
                    // do the thing for the first time
                    settings = getSharedPreferences("PREFS_NAME", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("FIRST_RUN", true);
                    editor.commit();

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date dt = new Date();

                    SharedPreferences.Editor editor2 = getSharedPreferences("user-pref", MODE_PRIVATE).edit();
                    editor2.putString("date", dateFormat.format(dt));
                    editor2.commit();

                    Log.e("## Cur Date", "" + dateFormat.format(dt));

                } else {

                    try {
                        SharedPreferences prefs = getSharedPreferences("user-pref", MODE_PRIVATE);

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                        String oldDate = prefs.getString("date", null);
                        Date newdt =  dateFormat.parse(dateFormat.format(new Date()));
                        Date olddt = dateFormat.parse(oldDate);

                        Log.e("## OLD Date",""+olddt);
                        Log.e("## New Date",""+newdt);

                        DateTime jodaOldDate = new DateTime(olddt);
                        DateTime jodaNewDate = new DateTime(newdt);

                        Log.e("## time diff",""+ Minutes.minutesBetween(jodaOldDate, jodaNewDate).getMinutes() % 60);

                        if(Minutes.minutesBetween(jodaOldDate, jodaNewDate).getMinutes() % 60 >=10){
                            if (val.length() != 0) {


                                checkGPS2();

                                /*mEtAreaOfCity.setText(val);
                                String _location = mEtAreaOfCity.getText().toString();
                                searchByPlaceName(_location);
                                showProgressDialog();
*/
                                DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                                Date dt = new Date();

                                SharedPreferences.Editor editor2 = getSharedPreferences("user-pref", MODE_PRIVATE).edit();
                                editor2.putString("date", dateFormat2.format(dt));
                                editor2.commit();

                                Log.e("## Cur Date chng", "" + dateFormat.format(dt));

                            }



                        }


                    }catch (Exception e){
                        Log.e("## EXc",e.toString());
                    }

                }
            } catch (Exception e) {
                Log.e("#### EXc", e.toString());
            }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
            case R.id.action_share:
                shareIt();
                return true;
            case R.id.action_rate:
                rateit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }


    private void Admob() {

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        lastUpdated = (CustomTextView) toolbar.findViewById(R.id.lastUpdated);
        refresh = (ImageView) toolbar.findViewById(R.id.refresh);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        lastUpdated.setText(String.format("Last Updated %s", CountryUtil.getDateAndTime(this)));

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isOffline) {

                    if (!CountryUtil.isConnected(MainActivity.this)) {
                        Toast.makeText(MainActivity.this, "Turn on data connection.", Toast.LENGTH_SHORT).show();

                    } else {


                        Fragment fragment = adapter.getItem(viewPager.getCurrentItem());
                        if (fragment instanceof CurrencyFragment) {

                            CurrencyFragment currencyFragment = (CurrencyFragment) fragment;
                            currencyFragment.callWebServiceAll();

                        } else if (fragment instanceof FavDeailsFragment) {

                            FavDeailsFragment favDeailsFragment = (FavDeailsFragment) fragment;
                            favDeailsFragment.getFavCountryList(true);

                        }
                    }

                } else {

                    Toast.makeText(MainActivity.this, "Turn on data connection.\n Turn off the offline mode.", Toast.LENGTH_SHORT).show();

                }


            }
        });


    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrencyFragment(), "Currency");
        adapter.addFragment(new FavDeailsFragment(), "Favorites");
        viewPager.setAdapter(adapter);
    }


    public void setupTabLayout(TabLayout tabLayout) {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        tabLayout.requestFocus();
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

            String title = mFragmentTitleList.get(position).toLowerCase();
            String cap = title.substring(0, 1).toUpperCase() + title.substring(1);
            return cap;
        }


        public View getTabView(int position) {
            View tab = LayoutInflater.from(MainActivity.this).inflate(R.layout.tabbar_view, null);
            TextView tabText = (TextView) tab.findViewById(R.id.tabText);
            ImageView tabImage = (ImageView) tab.findViewById(R.id.tabImage);
            tabText.setText(mFragmentTitleList.get(position));
            tabImage.setBackgroundResource(tabIcons[position]);
            if (position == 0) {
                tab.setSelected(true);
            }
            return tab;
        }
    }

    public void setLastUpdatedText() {

        lastUpdated.setText(String.format("Last Updated %s", CountryUtil.getDateAndTime(this)));

    }


//    private void loadAD() {
//        AdRequest adRequest;
//        AdView adView = (AdView) findViewById(R.id.adView);
//        // Request for Ads
//        adRequest = new com.google.android.gms.ads.AdRequest.Builder()
//                .build();
//        adView.loadAd(adRequest);
//
//    }

    private void shareIt() {
        String appPackageName = getPackageName();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, "http://play.google.com/store/apps/details?id=" + appPackageName);
        i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.checkout));
        startActivity(Intent.createChooser(i, "Share"));
    }


    private void rateit() {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }

    }

    @Override
    public void onDestroy() {
        SQLiteStudioService.instance().stop();
        super.onDestroy();
    }

}
