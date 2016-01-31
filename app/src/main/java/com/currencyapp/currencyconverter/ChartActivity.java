package com.currencyapp.currencyconverter;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.currencyapp.currencyconverter.util.CountryUtil;
import com.currencyapp.currencyconverter.util.YahooAPi;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;


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
    InterstitialAd mInterstitialAd;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        viewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showAd();

    }

    private void viewPager() {

        try {

            ads();
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
                    if (counter == 1) {
                        showAd();

                    } else {
                        if (counter == 8) {
                            counter = 0;
                        } else {

                        }
                    }
                    counter++;
                    //Toast.makeText(ChartActivity.this, String.valueOf(counter), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            changeRateFlag();

        } catch (Exception e) {

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showAd();
    }

    private void ads() {

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(CountryUtil.adInterstitial);

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();

            }
        });

        requestNewInterstitial();

    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        mPosition = intent.getIntExtra(Intent.EXTRA_UID, 2);
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

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("SEE_YOUR_LOGCAT_TO_GET_YOUR_DEVICE_ID")
                .build();

        mInterstitialAd.loadAd(adRequest);


    }

    private void showAd() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


}
