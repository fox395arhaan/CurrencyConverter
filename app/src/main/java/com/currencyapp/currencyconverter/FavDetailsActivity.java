package com.currencyapp.currencyconverter;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

public class FavDetailsActivity extends AppCompatActivity {


    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_details);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);

        FavDeailsFragment favDeailsFragment = new FavDeailsFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.frameLayout, favDeailsFragment).commit();

    }
}
