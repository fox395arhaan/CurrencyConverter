package com.currencyapp.currencyconverter.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.currencyapp.currencyconverter.Country;
import com.currencyapp.currencyconverter.R;
import com.google.gson.Gson;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by raghav on 12/1/16.
 */
public class CountryUtil {


    public static final String adInterstitial = "ca-app-pub-6733180445570119/8404505582";
    public static final String isDefaultSet = "isDefaultSet";
    public static final String fromCountry = "fromCountry";
    public static final String ToCountry = "ToCountry";
    public static final String AllCountry = "AllCountry";
    public static final String FromValue = "FromValue";
    public static final String DateTime = "DateTime";
    public static final String DatetimeDiff = "DatetimeDiff";
    public static final String UpdateData = "UpdateData";
    public static final String IsfirstTime = "IsfirstTime";
    public static final String LastEnteredValue = "LastEnteredValue";
    public static final String Tovalue = "Tovalue";
    static Gson gson = MyApplication.getInstance().getGson();
    static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm aa");

    public static void setDefault(Context context, boolean isDefault) {
        Prefs.with(context).save(isDefaultSet, isDefault);
    }

    public static boolean getDefault(Context context) {
        return Prefs.with(context).getBoolean(isDefaultSet, false);
    }

    public static void setLastEnteredValue(Context context, String value) {
        Prefs.with(context).save(LastEnteredValue, value);
    }

    public static String getLastEnteredValue(Context context) {
        return Prefs.with(context).getString(LastEnteredValue, "1");
    }


    public static void setFromValue(Context context, String value) {
        Prefs.with(context).save(FromValue, value);
    }

    public static String getFromValue(Context context) {
        return Prefs.with(context).getString(FromValue, "1");
    }

    public static void setToValue(Context context, String value) {
        Prefs.with(context).save(Tovalue, value);
    }

    public static String getToValue(Context context) {
        return Prefs.with(context).getString(Tovalue, "1");
    }


    public static void setDateAndTime(Context context) {
        Date date = new Date();
        String value = sdf.format(date);
        Prefs.with(context).save(DateTime, value);
    }

    public static String getDateAndTime(Context context) {

        Date date = new Date();
        return Prefs.with(context).getString(DateTime, sdf.format(date));
    }

    public static void setDateAndTimeDiff(Context context) {
        Date date = new Date();
        String value = sdf.format(date);
        Prefs.with(context).save(DatetimeDiff, value);
    }

    public static String getDateAndTimeDiff(Context context) {

        Date date = new Date();
        return Prefs.with(context).getString(DatetimeDiff, sdf.format(date));
    }


    public static void setIsfirstTime(Context context, boolean isDefault) {
        Prefs.with(context).save(isDefaultSet, isDefault);
    }

    public static boolean getIsfirstTime(Context context) {
        return Prefs.with(context).getBoolean(isDefaultSet, false);
    }

    public static void setUpdateData(Context context, int i) {
        Prefs.with(context).save(UpdateData, i);
    }

    public static int getUpdateData(Context context) {
        return Prefs.with(context).getInt(UpdateData, 0);
    }

    public static void setFromCountry(Context context, Country country) {
        String fromCountryString = gson.toJson(country);
        Prefs.with(context).save(fromCountry, fromCountryString);
    }

    public static Country getFromCountry(Context context) {

        Country country = new Country();
        String fromCountryString = Prefs.with(context).getString(fromCountry, "");
        if (fromCountryString != null && !fromCountryString.isEmpty()) {
            country = gson.fromJson(fromCountryString, Country.class);
        }

        return country;
    }

    public static void setToCountry(Context context, Country country) {
        String fromCountryString = gson.toJson(country);
        Prefs.with(context).save(ToCountry, fromCountryString);
    }

    public static Country getToCountry(Context context) {

        Country country = new Country();
        String fromCountryString = Prefs.with(context).getString(ToCountry, "");
        if (fromCountryString != null && !fromCountryString.isEmpty()) {
            country = gson.fromJson(fromCountryString, Country.class);
        }

        return country;
    }

    public static void setToAllCountry(Context context, String all) {
        String fromCountryString = all;
        Prefs.with(context).save(AllCountry, fromCountryString);
    }

    public static String getToAllCountry(Context context) {

        String all = "";
        String fromCountryString = Prefs.with(context).getString(AllCountry, "");
        if (fromCountryString != null && !fromCountryString.isEmpty()) {
            all = fromCountryString;
        }

        return all;
    }


    public static int getResourceId(Context context, String pVariableName) {
        try {
            return context.getResources().getIdentifier(pVariableName, "drawable", context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static void showErrorAlert(Context context, String msg) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Error");
        alert.setMessage(msg);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }


    public static void setTheme(int type, Activity activity,
                                Toolbar toolbar
            , LinearLayout mainLinearLayout
            , CardView fromHolderCard
            , CardView toHolderCard
            , TextView txtFrom
            , TextView txtTo
            , ImageView imgViceversa
            , EditText edtFrom
            , EditText edtTo) {


        Resources resource = activity.getResources();


        fromHolderCard.setCardBackgroundColor(resource.getColor(R.color.white));
        toHolderCard.setCardBackgroundColor(resource.getColor(R.color.white));
        txtFrom.setTextColor(resource.getColor(R.color.textColor));
        txtTo.setTextColor(resource.getColor(R.color.textColor));
        edtFrom.setBackgroundDrawable(resource.getDrawable(R.drawable.rect_edit_text_white));
        edtTo.setBackgroundDrawable(resource.getDrawable(R.drawable.rect_edit_text_white));
        edtFrom.setTextColor(resource.getColor(R.color.textColor));
        edtTo.setTextColor(resource.getColor(R.color.textColor));


        switch (type) {


            case 0:
                toolbar.setBackgroundColor(resource.getColor(R.color.default_toolbar));
                mainLinearLayout.setBackgroundColor(resource.getColor(R.color.offline_bg));
                setStatusBarColor(activity, resource.getColor(R.color.default_statusbar));
                imgViceversa.setColorFilter(resource.getColor(R.color.textColor));
                break;
            case 1:
                toolbar.setBackgroundColor(resource.getColor(R.color.yellow_bg));
                mainLinearLayout.setBackgroundColor(resource.getColor(R.color.yellow_bg));
                setStatusBarColor(activity, resource.getColor(R.color.yellow_statusbar));
                imgViceversa.setColorFilter(resource.getColor(R.color.white));
                break;


            case 2:
                toolbar.setBackgroundColor(resource.getColor(R.color.green_bg));
                mainLinearLayout.setBackgroundColor(resource.getColor(R.color.green_bg));
                setStatusBarColor(activity, resource.getColor(R.color.green_statusbar));
                imgViceversa.setColorFilter(resource.getColor(R.color.white));
                break;


            case 3:
                toolbar.setBackgroundColor(resource.getColor(R.color.default_toolbar));
                mainLinearLayout.setBackgroundColor(resource.getColor(R.color.offline_bg));
                setStatusBarColor(activity, resource.getColor(R.color.default_statusbar));
                imgViceversa.setColorFilter(resource.getColor(R.color.textColor));
                break;
        }


    }


    public static void setAddToFavTheme(int type, Activity activity, Toolbar toolbar, LinearLayout mainLayout, Button btnShow) {


        Resources resource = activity.getResources();

        mainLayout.setBackgroundColor(resource.getColor(R.color.offline_bg));

        switch (type) {


            case 0:
                //mainLayout.setBackgroundColor(resource.getColor(R.color.default_bg));
                toolbar.setBackgroundColor(resource.getColor(R.color.default_toolbar));
                btnShow.setBackgroundColor(resource.getColor(R.color.default_toolbar));
                setStatusBarColor(activity, resource.getColor(R.color.default_statusbar));
                break;

            case 1:
                toolbar.setBackgroundColor(resource.getColor(R.color.yellow_bg));
                btnShow.setBackgroundColor(resource.getColor(R.color.yellow_bg));
                // mainLayout.setBackgroundColor(resource.getColor(R.color.yellow_bg));
                setStatusBarColor(activity, resource.getColor(R.color.yellow_statusbar));
                break;


            case 2:
                toolbar.setBackgroundColor(resource.getColor(R.color.green_bg));
                btnShow.setBackgroundColor(resource.getColor(R.color.green_bg));
                // mainLayout.setBackgroundColor(resource.getColor(R.color.green_bg));
                setStatusBarColor(activity, resource.getColor(R.color.green_statusbar));
                break;


            case 3:
                toolbar.setBackgroundColor(resource.getColor(R.color.default_toolbar));
                btnShow.setBackgroundColor(resource.getColor(R.color.default_toolbar));
                setStatusBarColor(activity, resource.getColor(R.color.default_statusbar));

                break;
        }
    }

    private static void setStatusBarColor(Activity activity, int mStatusBarColor) {

        Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(mStatusBarColor);
        }
    }

    public static boolean isCallWebService(Context context) {

        boolean isCalled = false;
        try {

            SharedPreferences settings = context.getSharedPreferences("PREFS_NAME", 0);
            boolean mboolean = settings.getBoolean("FIRST_RUN", false);
            if (!mboolean) {
                // do the thing for the first time
                settings = context.getSharedPreferences("PREFS_NAME", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("FIRST_RUN", true);
                editor.commit();

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date dt = new Date();

                SharedPreferences.Editor editor2 = context.getSharedPreferences("user-pref", context.MODE_PRIVATE).edit();
                editor2.putString("date", dateFormat.format(dt));
                editor2.commit();

                Log.e("## Cur Date", "" + dateFormat.format(dt));

                isCalled = true;


            } else {

                try {
                    SharedPreferences prefs = context.getSharedPreferences("user-pref", context.MODE_PRIVATE);

                    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

                    String oldDate = prefs.getString("date", null);
                    Date newdt = dateFormat.parse(dateFormat.format(new Date()));
                    Date olddt = dateFormat.parse(oldDate);

                    Log.e("## OLD Date", "" + olddt);
                    Log.e("## New Date", "" + newdt);

                    org.joda.time.DateTime jodaOldDate = new DateTime(olddt);
                    DateTime jodaNewDate = new DateTime(newdt);


                    Log.e("## total min time diff", "" + Minutes.minutesBetween(jodaOldDate, jodaNewDate).getMinutes());
                    Log.e("## min time diff", "" + Minutes.minutesBetween(jodaOldDate, jodaNewDate).getMinutes() / 60);

                    //Log.e("## min time diff",""+ Minutes.minutesBetween(jodaOldDate, jodaNewDate).getMinutes() % 60);

                    if (Minutes.minutesBetween(jodaOldDate, jodaNewDate).getMinutes() >= 15) {

                        DateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                        Date dt = new Date();

                        SharedPreferences.Editor editor2 = context.getSharedPreferences("user-pref", context.MODE_PRIVATE).edit();
                        editor2.putString("date", dateFormat2.format(dt));
                        editor2.commit();

                        Log.e("## Cur Date chng", "" + dateFormat.format(dt));


                        isCalled = true;


                    }


                } catch (Exception e) {
                    Log.e("## EXc", e.toString());
                }

            }
        } catch (Exception e) {
            Log.e("#### EXc", e.toString());
        }

        return isCalled;
    }


}
