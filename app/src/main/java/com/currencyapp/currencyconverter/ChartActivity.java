package com.currencyapp.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.RelativeLayout;

import com.currencyapp.currencyconverter.util.CountryUtil;
import com.currencyapp.currencyconverter.util.YahooAPi;
import com.google.android.gms.ads.AdSize;


public class ChartActivity extends AppCompatActivity {

    private String time, spinnerone, spinnertwo;
    com.google.android.gms.ads.AdRequest adRequest;
    private String url = "http://chart.finance.yahoo.com/z?s=";
    //GBPINR=x&t=3m&q=l&l=on&z=m";
    private String sub_url = "&q=l&l=on&z=m";
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ImageAdapter mImageAdapter;
    int mPosition = 2;
    private Country fromCountry;
    private Country toCountry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        viewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeRateFlag();
    }

    private void viewPager() {

        getDataFromIntent();
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        mImageAdapter = new ImageAdapter(getSupportFragmentManager());

        for (int i = 0; i < YahooAPi.maps.length; i++) {

            String s = YahooAPi.maps[i];
            mImageAdapter.addFragment(ImageFragment.newInstance(i, s), s);
        }

        mViewPager.setAdapter(mImageAdapter);
        mViewPager.setCurrentItem(mPosition);
        mViewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeRateFlag();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(Intent.EXTRA_UID, 2);
    }


    private void addmob() {
        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(ChartActivity.this);
        adView.setAdUnitId("ca-app-pub-6733180445570119/3279220380");
        adView.setAdSize(AdSize.BANNER);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.addmob_chart);
        layout.addView(adView);
        layout.setGravity(Gravity.CENTER);
        com.google.android.gms.ads.AdRequest request = new com.google.android.gms.ads.AdRequest.Builder().build();
        adView.loadAd(request);
    }

    private void changeRateFlag() {

        fromCountry = CountryUtil.getFromCountry(this);
        toCountry = CountryUtil.getToCountry(this);

        Fragment fragment = mImageAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment instanceof ImageFragment) {

            ImageFragment imageFragment = (ImageFragment) fragment;
            String time = YahooAPi.maps[mViewPager.getCurrentItem()];
            String url = String.format("http://chart.finance.yahoo.com/z?s=%s%s=x&t=%s&q=l&m=on&z=l", fromCountry.shortName, toCountry.shortName, time);
            imageFragment.changeImage(url);

        }

    }


}
