package com.currencyapp.currencyconverter.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.currencyapp.currencyconverter.Country;
import com.currencyapp.currencyconverter.Model.Rate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by raghav on 12/1/16.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;
    SQLiteDatabase sqLiteDatabase;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "countryDB";
    private static final String TABLE_country = "country";
    private static final String TABLE_rate = "rate";

    private static final String KEY_ID = "id";
    private static final String KEY_SHORT_NAME = "shortName";
    private static final String KEY_FULL_NAME = "fullName";
    private static final String KEY_isSelected = "isSelected";

    private static final String KEY_id = "id";
    private static final String KEY_idName = "idName ";
    private static final String KEY_Name = "Name";
    private static final String KEY_Rate = "Rates";
    private static final String KEY_Date = "Dates";
    private static final String KEY_Time = "Times";
    private static final String KEY_Ask = "Asks";
    private static final String KEY_Bid = "Bids";


    private static final String flag = "flag_";
    private static final String usd = "USD";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_country + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_SHORT_NAME + " TEXT,"
                + KEY_FULL_NAME + " TEXT," + KEY_isSelected + " TEXT" + ")";

        String CREATE_CONTACTS_Rate = "CREATE TABLE " + TABLE_rate + "(" +
                KEY_id + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_idName + " TEXT," +
                KEY_Name + " TEXT," +
                KEY_Rate + " TEXT," +
                KEY_Date + " TEXT, " +
                KEY_Time + " TEXT, " +
                KEY_Ask + " TEXT, " +
                KEY_Bid + " TEXT " + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_CONTACTS_Rate);

        saveAllCoutries(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_country);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_rate);
        onCreate(db);

    }

    public void addCounrty(Country country) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SHORT_NAME, country.shortName);
        values.put(KEY_FULL_NAME, country.fullName); // Contact Name
        values.put(KEY_isSelected, String.valueOf(country.isSelected));
        db.insert(TABLE_country, null, values);
        db.close();
    }

    private void saveAllCoutries(SQLiteDatabase db) {

        StringBuilder stringBuilder = new StringBuilder();

        ArrayList<Country> countries = Country.getAllCountries(context);
        if (countries.size() > 3) {

            Country countryFrom = countries.get(0);
            Country countryTo = countries.get(1);
            CountryUtil.setFromCountry(context, countryFrom);
            CountryUtil.setToCountry(context, countryTo);
        }
        for (int i = 0; i < countries.size(); i++) {
            String v = "\"" + usd + countries.get(i).shortName.toUpperCase() + "\"";
            ContentValues values = new ContentValues();
            values.put(KEY_SHORT_NAME, countries.get(i).shortName);
            values.put(KEY_FULL_NAME, countries.get(i).fullName); // Contact Name
            values.put(KEY_isSelected, String.valueOf(countries.get(i).isSelected));
            db.insert(TABLE_country, null, values);
            if (countries.size() - 1 == i) {
                stringBuilder.append(v);
            } else {
                stringBuilder.append(v + ",");
            }

        }

        CountryUtil.setToAllCountry(context, stringBuilder.toString());

    }

    public void saveAllRate(List<Rate> rates) {

        boolean isDateSet = false;
        SQLiteDatabase db = open();
        db.execSQL("delete from " + TABLE_rate);

        for (Rate rate : rates) {
            ContentValues values = new ContentValues();
            values.put(KEY_idName, rate.id);
            values.put(KEY_Name, rate.Name);
            values.put(KEY_Rate, rate.Rate);
            values.put(KEY_Date, rate.Date);
            values.put(KEY_Time, rate.Time);
            values.put(KEY_Ask, rate.Ask);
            db.insert(TABLE_rate, null, values);
            if (!isDateSet) {
                if (rate.Date != null && rate.Time != null) {
                    //"1/23/2016"
                    SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy");
                    SimpleDateFormat formatter2 = new SimpleDateFormat("dd MMM yyyy");
                    try {
                        Date date = formatter.parse(rate.Date);
                        String formatted = formatter2.format(date);
                        formatted = formatted + " " + rate.Time;
                        CountryUtil.setDateAndTime(context, formatted);
                        isDateSet = true;

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        close(db);


    }


    public ArrayList<Rate> getRateArrayList(String s) {
        ArrayList<Rate> rateArrayList = new ArrayList<>();

        String selectQuery = "select * from " + TABLE_rate + " where " + KEY_idName + " in ( " + s + " )";


        Log.d("search", selectQuery);

        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                Rate rate = new Rate();
                // rate.setId(cursor.getString(cursor.getColumnIndex(KEY_idName)));
                rate.setName(cursor.getString(cursor.getColumnIndex(KEY_Name)));
                rate.setRate(cursor.getString(cursor.getColumnIndex(KEY_Rate)));
                rate.setDate(cursor.getString(cursor.getColumnIndex(KEY_Date)));
                rate.setTime(cursor.getString(cursor.getColumnIndex(KEY_Time)));
                rate.setAsk(cursor.getString(cursor.getColumnIndex(KEY_Ask)));
                rate.setBid(cursor.getString(cursor.getColumnIndex(KEY_Bid)));
                rateArrayList.add(rate);
            } while (cursor.moveToNext());
        }

        // return contact list
        close(db);
        return rateArrayList;
    }

    public Rate getRate(String s) {
        Rate rate = new Rate();

        String selectQuery = "select * from " + TABLE_rate + " where " + KEY_idName + " in ( \"" + s + "\" )";

        Log.d("search", selectQuery);

        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);


        try {


            if (cursor.moveToFirst()) {

                // rate.setId(cursor.getString(cursor.getColumnIndex(KEY_idName)));
                rate.setName(cursor.getString(cursor.getColumnIndex(KEY_Name)));
                rate.setRate(cursor.getString(cursor.getColumnIndex(KEY_Rate)));
                rate.setDate(cursor.getString(cursor.getColumnIndex(KEY_Date)));
                rate.setTime(cursor.getString(cursor.getColumnIndex(KEY_Time)));
                rate.setAsk(cursor.getString(cursor.getColumnIndex(KEY_Ask)));
                rate.setBid(cursor.getString(cursor.getColumnIndex(KEY_Bid)));

            }


        } catch (Exception e) {

            Log.d("search", selectQuery);
        }
        // return contact list

        close(db);
        return rate;
    }


    public ArrayList<Rate> getRateList(String s) {
        ArrayList<Rate> rates = new ArrayList<>();

        String selectQuery = "select * from " + TABLE_rate + " where " + KEY_idName + " in ( \"" + s + "\" )";

        Log.d("search", selectQuery);

        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);


        try {


            if (cursor.moveToFirst()) {
                // rate.setId(cursor.getString(cursor.getColumnIndex(KEY_idName)));

                Rate rate = new Rate();
                rate.setName(cursor.getString(cursor.getColumnIndex(KEY_Name)));
                rate.setRate(cursor.getString(cursor.getColumnIndex(KEY_Rate)));
                rate.setDate(cursor.getString(cursor.getColumnIndex(KEY_Date)));
                rate.setTime(cursor.getString(cursor.getColumnIndex(KEY_Time)));
                rate.setAsk(cursor.getString(cursor.getColumnIndex(KEY_Ask)));
                rate.setBid(cursor.getString(cursor.getColumnIndex(KEY_Bid)));
                rates.add(rate);

            }


        } catch (Exception e) {

            Log.d("search", selectQuery);
        }
        // return contact list

        close(db);
        return rates;
    }


    public List<Country> getAllContries() {
        List<Country> contactList = new ArrayList<Country>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_country;

        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Country contact = new Country();
                contact.setShortName(cursor.getString(cursor.getColumnIndex(KEY_SHORT_NAME)));
                contact.setFullName(cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME)));
                contact.setIsSelected(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_isSelected))));
                int resourceId = context.getResources().getIdentifier(flag + contact.getShortName().toLowerCase(), "drawable", MyApplication.getInstance().getPackageName());
                contact.setImageId(resourceId);

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        close(db);
        return contactList;
    }

    public ArrayList<Country> getAllContries(boolean isSelected) {
        ArrayList<Country> contactList = new ArrayList<Country>();
        // Select All Query
        //SELECT  * FROM country WHERE isSelected='true'
        String selectQuery = "SELECT  * FROM " + TABLE_country + " WHERE " + KEY_isSelected + "=" + String.format(" \'%s\' ", String.valueOf(isSelected));


        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Country contact = new Country();
                contact.setShortName(cursor.getString(cursor.getColumnIndex(KEY_SHORT_NAME)));
                contact.setFullName(cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME)));
                contact.setIsSelected(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_isSelected))));
                int resourceId = context.getResources().getIdentifier(flag + contact.getShortName().toLowerCase(), "drawable", MyApplication.getInstance().getPackageName());
                contact.setImageId(resourceId);

                contactList.add(contact);
            } while (cursor.moveToNext());
        }


        // return contact list
        close(db);
        return contactList;
    }

    public ArrayList<Country> searchContries(String s) {
        ArrayList<Country> contactList = new ArrayList<Country>();
        // Select All Query
        //SELECT  * FROM country WHERE isSelected='true'
        String selectQuery = "select * from country where " + KEY_SHORT_NAME + " like '" + s + "%' or " + KEY_FULL_NAME + " like '" + s + "%' ";

        Log.d("search", selectQuery);

        SQLiteDatabase db = open();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Country contact = new Country();
                contact.setShortName(cursor.getString(cursor.getColumnIndex(KEY_SHORT_NAME)));
                contact.setFullName(cursor.getString(cursor.getColumnIndex(KEY_FULL_NAME)));
                contact.setIsSelected(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(KEY_isSelected))));
                int resourceId = context.getResources().getIdentifier(flag + contact.getShortName().toLowerCase(), "drawable", MyApplication.getInstance().getPackageName());
                contact.setImageId(resourceId);

                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        close(db);
        // return contact list
        return contactList;
    }

    // Updating single contact

    public void updateCourtryTofalse() {

        SQLiteDatabase db = open();
        String updateQuery = "update country set isSelected=\'false\'";
        db.execSQL(updateQuery);
        close(db);

    }

    public void updateCountry(Country country) {

        SQLiteDatabase db = open();


        ContentValues values = new ContentValues();
        values.put(KEY_SHORT_NAME, country.shortName);
        values.put(KEY_FULL_NAME, country.fullName); // Contact Name
        values.put(KEY_isSelected, String.valueOf(country.isSelected));

        int i = db.update(TABLE_country, values, KEY_SHORT_NAME + " = ?", new String[]{String.valueOf(country.getShortName())});

        close(db);

    }


    private SQLiteDatabase open() {
        return this.getWritableDatabase();
    }

    private void close(SQLiteDatabase sqLiteDatabase) {
        if (sqLiteDatabase.isOpen()) {
            sqLiteDatabase.close();
        }
    }
}