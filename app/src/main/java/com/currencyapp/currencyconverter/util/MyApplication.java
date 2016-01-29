package com.currencyapp.currencyconverter.util;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by raghav on 12/1/16.
 */
public class MyApplication extends Application {


    static MyApplication myApplication;
    SQLiteDatabase sqLiteDatabase;
    Gson gson;

    public static Retrofit getRetrofit() {
        return retrofit;
    }

    private static Retrofit retrofit;
    private OkHttpClient okHttpClient;

    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        sqLiteDatabase = databaseHandler.getWritableDatabase();
        myApplication = this;

        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(60, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(YahooAPi.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    public static synchronized MyApplication getInstance() {


        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

    public Gson getGson() {
        if (this.gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static Context getContext() {
        return myApplication;
    }
}
