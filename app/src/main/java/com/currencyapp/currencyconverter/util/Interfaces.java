package com.currencyapp.currencyconverter.util;

import com.currencyapp.currencyconverter.Model.Allcurrencies;
import com.currencyapp.currencyconverter.Model.YahooFinanceReal;
import com.currencyapp.currencyconverter.Model.check;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by raghav on 17/1/16.
 */
public class Interfaces {

    public interface YahoofinanceReal {

        @GET("v1/public/yql?format=json&diagnostics=true&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&callback=")
            //Call<YahooFinanceReal> getCurrency(@Query("q") String q,@Query("diagnostics") boolean diagnostics,@Query("env") String env);
        Call<YahooFinanceReal> getCurrency(@Query("q") String q);
    }

    public interface Check {
        String pathUrl = "notes/1";

        @GET(pathUrl)
        Call<check> getCheckCall();

    }

    public interface AllCurrencies{
        String pathUrl = "/webservice/v1/symbols/allcurrencies/quote?format=json";

        @GET(pathUrl)
        Call<Allcurrencies> getAllcurrenciesCall();

    }
}
