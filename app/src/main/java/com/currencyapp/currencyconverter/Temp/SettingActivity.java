package com.currencyapp.currencyconverter.Temp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.widget.Toast;

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

        Preference preference = findPreference("mail");
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                sendEmail();

                return true;
            }
        });

        Preference preference1 = findPreference("removead");
        preference1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                rateit();

                return true;
            }
        });
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"patelniky7@gmail.com"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SettingActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void rateit() {


        String proPackageName="com.currencyapp.currencyconverterpro";

        Uri uri = Uri.parse("market://details?id=" + proPackageName);
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + proPackageName)));
        }



        Log.d("URI",uri.toString());
    }

}



