package com.currencyapp.currencyconverter.Temp;

import android.os.Bundle;

import com.currencyapp.currencyconverter.AppCompatPreferenceActivity;
import com.currencyapp.currencyconverter.R;

/**
 * Created by raghav on 20/1/16.
 */
public class SettingActivity extends AppCompatPreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

    }
}



