package com.currencyapp.currencyconverter.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.currencyapp.currencyconverter.Country;
import com.currencyapp.currencyconverter.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by raghav on 12/1/16.
 */
public class CountryUtil {


    public static final String isDefaultSet = "isDefaultSet";
    public static final String fromCountry = "fromCountry";
    public static final String ToCountry = "ToCountry";
    public static final String AllCountry = "AllCountry";
    public static final String FromValue = "FromValue";
    public static final String DateTime = "DateTime";
    public static final String UpdateData = "UpdateData";
    public static final String IsfirstTime = "IsfirstTime";
    static Gson gson = MyApplication.getInstance().getGson();
    static SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");

    public static void setDefault(Context context, boolean isDefault) {
        Prefs.with(context).save(isDefaultSet, isDefault);
    }

    public static boolean getDefault(Context context) {
        return Prefs.with(context).getBoolean(isDefaultSet, false);
    }

    public static void setFromValue(Context context, String value) {
        Prefs.with(context).save(FromValue, value);
    }

    public static String getFromValue(Context context) {
        return Prefs.with(context).getString(FromValue, "1");
    }

    public static void setDateAndTime(Context context, String value) {
        Prefs.with(context).save(DateTime, value);
    }

    public static String getDateAndTime(Context context) {

        Date date = new Date();
        return Prefs.with(context).getString(DateTime, sdf.format(date));
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


        if (type == 0) {
            toolbar.setBackgroundColor(resource.getColor(R.color.default_toolbar));
            mainLinearLayout.setBackgroundColor(resource.getColor(R.color.default_bg));
            fromHolderCard.setCardBackgroundColor(resource.getColor(R.color.default_card));
            toHolderCard.setCardBackgroundColor(resource.getColor(R.color.default_card));
            txtFrom.setTextColor(resource.getColor(R.color.black));
            txtTo.setTextColor(resource.getColor(R.color.black));
            edtFrom.setBackgroundDrawable(resource.getDrawable(R.drawable.rect_edit_text));
            edtTo.setBackgroundDrawable(resource.getDrawable(R.drawable.rect_edit_text));
        } else {

            fromHolderCard.setCardBackgroundColor(resource.getColor(R.color.white));
            toHolderCard.setCardBackgroundColor(resource.getColor(R.color.white));
            txtFrom.setTextColor(resource.getColor(R.color.black));
            txtTo.setTextColor(resource.getColor(R.color.black));
            edtFrom.setBackgroundDrawable(resource.getDrawable(R.drawable.rect_edit_text_white));
            edtTo.setBackgroundDrawable(resource.getDrawable(R.drawable.rect_edit_text_white));

            switch (type) {


                case 1:
                    toolbar.setBackgroundColor(resource.getColor(R.color.yellow_bg));
                    mainLinearLayout.setBackgroundColor(resource.getColor(R.color.yellow_bg));

                    break;


                case 2:
                    toolbar.setBackgroundColor(resource.getColor(R.color.green_bg));
                    mainLinearLayout.setBackgroundColor(resource.getColor(R.color.green_bg));

                    break;


                case 3:
                    toolbar.setBackgroundColor(resource.getColor(R.color.default_toolbar));
                    mainLinearLayout.setBackgroundColor(resource.getColor(R.color.offline_bg));

                    break;
            }
        }

    }


}