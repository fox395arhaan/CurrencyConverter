package com.currencyapp.currencyconverter;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class GraphDetailActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    private ImageAdapter mImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_detail);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mImageAdapter = new ImageAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mImageAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                Toast.makeText(GraphDetailActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
