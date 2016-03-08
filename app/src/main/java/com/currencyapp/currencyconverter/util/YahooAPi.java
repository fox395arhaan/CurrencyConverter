package com.currencyapp.currencyconverter.util;

/**
 * Created by raghav on 17/1/16.
 */
public class YahooAPi {

    public static String ALLCURRENCIES = "/webservice/v1/symbols/allcurrencies/quote?format=json";
    public static String BaseUrl = "https://query.yahooapis.com";
    //public static String BaseUrl="http://finance.yahoo.com";
    public static String BaseUrlAll="http://finance.yahoo.com";

    public static String[] maps = new String[]{"1d", "5d", "1m", "3m", "6m", "1y", "2y"};


}
