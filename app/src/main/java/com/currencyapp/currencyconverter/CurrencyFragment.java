package com.currencyapp.currencyconverter;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.currencyapp.currencyconverter.Model.Rate;
import com.currencyapp.currencyconverter.Model.YahooFinanceReal;
import com.currencyapp.currencyconverter.Temp.FlagDialog;
import com.currencyapp.currencyconverter.Temp.MainActivity;
import com.currencyapp.currencyconverter.util.CountryUtil;
import com.currencyapp.currencyconverter.util.DatabaseHandler;
import com.currencyapp.currencyconverter.util.Interfaces;
import com.currencyapp.currencyconverter.util.MyApplication;
import com.currencyapp.currencyconverter.util.YahooAPi;
import com.currencyapp.currencyconverter.widget.CustomEditTextView;
import com.currencyapp.currencyconverter.widget.CustomTextView;

import java.io.InputStream;
import java.net.URL;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyFragment extends Fragment implements View.OnClickListener {


    FragmentManager manager;
    FlagDialog fromflagDialog, toflagDialog;
    TextView tapToLarge;
    CustomTextView txtFrom, txtTo;
    ImageView imgFrom, imgTo, im_chart, imgViceversa;
    Interfaces.YahoofinanceReal yahoofinanceReal;
    Interfaces.AllCurrencies allCurrencies;
    Country fromCountry;
    Country toCountry;
    CustomEditTextView edtFrom, edtTo;
    private TextView oneday, fiveday, threemonths, sixmoths, oneyear, twoyear, fiveyear;
    private LinearLayout chartHolder, fromHolder, toHolder, mainHolder;
    private TextView txtoffLineMode;
    private String url = "http://chart.finance.yahoo.com/z?s=";
    private String sub_url = "&q=l&m=on&z=m";
    private Intent intent;

    ProgressBar mProgressBar;
    DatabaseHandler databaseHandler;
    private String usd = "USDUSD";
    boolean isOffline = false;
    Toolbar toolbar;
    private String mUSD = "USD";
    private View rootView;
    //private ProgressDialog mProgressDialog;
    MainActivity mainActivity;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private ImageAdapter mImageAdapter;
    CardView fromHolderCard, toHolderCard;


    public CurrencyFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager = getFragmentManager();
        fromflagDialog = new FlagDialog();
        toflagDialog = new FlagDialog();
        databaseHandler = new DatabaseHandler(getActivity());
//        mProgressDialog = ProgressDialog.show(getActivity(), "Please wait", "Updating Rates");
//        mProgressDialog.dismiss();


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_currency, container, false);
        init(rootView);

        return rootView;


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d("onActivityCreated", "onStart");
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        checkData();

    }

    private void checkData() {
        try {
            fromCountry = CountryUtil.getFromCountry(getActivity());
            toCountry = CountryUtil.getToCountry(getActivity());
            setCountryNameandFlag(fromCountry, 0);
            setCountryNameandFlag(toCountry, 1);
            getUserSettings();
            changeRateFlag();
            offlineModeData(true);
//            if (!isOffline) {
//                if (!CountryUtil.isConnected(getActivity())) {
//
//                    offlineModeData(true);
//
//                } else {
//                    callWebService(true);
//                    if (!CountryUtil.getIsfirstTime(getActivity())) {
//
//                        // callWebServiceAll();
//                    }
//                }
//            } else {
//                offlineModeData(true);
//            }
        } catch (Exception e) {

            edtTo.setText("0");
        }
    }

    private void init(View rootView) {

        txtFrom = (CustomTextView) rootView.findViewById(R.id.txtFrom);
        txtTo = (CustomTextView) rootView.findViewById(R.id.txtTo);
        tapToLarge = (TextView) rootView.findViewById(R.id.tapToLarge);
        edtFrom = (CustomEditTextView) rootView.findViewById(R.id.edtFrom);
        edtTo = (CustomEditTextView) rootView.findViewById(R.id.edtTo);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        edtTo.setEnabled(false);
        toHolder = (LinearLayout) rootView.findViewById(R.id.toHolder);
        chartHolder = (LinearLayout) rootView.findViewById(R.id.chartHolder);
        fromHolder = (LinearLayout) rootView.findViewById(R.id.fromHolder);
        txtoffLineMode = (TextView) rootView.findViewById(R.id.txtoffLineMode);
        mainHolder = (LinearLayout) rootView.findViewById(R.id.mainHolder);
        fromHolderCard = (CardView) rootView.findViewById(R.id.fromHolderCard);
        toHolderCard = (CardView) rootView.findViewById(R.id.toHolderCard);


        //check layout is being used by android

        Log.d("check layout", mainHolder.getTag().toString());


        edtFrom.setText(CountryUtil.getFromValue(getActivity()));
        edtTo.setText(CountryUtil.getToValue(getActivity()));
        int position = edtFrom.getText().length();
        edtFrom.setSelection(position);
        im_chart = (ImageView) rootView.findViewById(R.id.img_chart);
        imgFrom = (ImageView) rootView.findViewById(R.id.imgFrom);
        imgViceversa = (ImageView) rootView.findViewById(R.id.imgViceversa);
        imgTo = (ImageView) rootView.findViewById(R.id.imgTo);
        oneday = (TextView) rootView.findViewById(R.id.textView_oneday);
        fiveday = (TextView) rootView.findViewById(R.id.textView_fiveday);
        threemonths = (TextView) rootView.findViewById(R.id.textView_threemonth);
        sixmoths = (TextView) rootView.findViewById(R.id.textView_sixmonth);
        oneyear = (TextView) rootView.findViewById(R.id.textView_oneyear);
        twoyear = (TextView) rootView.findViewById(R.id.textView_twoyear);
        fiveyear = (TextView) rootView.findViewById(R.id.textView_fiveyear);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);


        oneday.setOnClickListener(this);
        fiveday.setOnClickListener(this);
        threemonths.setOnClickListener(this);
        sixmoths.setOnClickListener(this);
        oneyear.setOnClickListener(this);
        twoyear.setOnClickListener(this);
        fiveyear.setOnClickListener(this);


        yahoofinanceReal = MyApplication.getRetrofit().create(Interfaces.YahoofinanceReal.class);
        allCurrencies = MyApplication.getRetrofitAll().create(Interfaces.AllCurrencies.class);


        //viewpager

        mImageAdapter = new ImageAdapter(getChildFragmentManager());


        for (int i = 0; i < YahooAPi.maps.length; i++) {

            String s = YahooAPi.maps[i];
            mImageAdapter.addFragment(ImageFragment.newInstance(i, s), s);
        }

        mViewPager.setAdapter(mImageAdapter);
        mViewPager.setCurrentItem(2);
        mViewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                changeRateFlag();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        fromflagDialog.setSetCountryListener(new FlagDialog.SetCountryListener() {
            @Override
            public void setCountry(Country country) {

                CountryUtil.setFromCountry(getActivity(), country);
                changeRate(country, 0);
            }
        });

        toflagDialog.setSetCountryListener(new FlagDialog.SetCountryListener() {
            @Override
            public void setCountry(Country country) {

                // Toast.makeText(getActivity(), country.fullName, Toast.LENGTH_SHORT).show();
                CountryUtil.setToCountry(getActivity(), country);
                changeRate(country, 1);
            }
        });

        edtFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String value = s.toString();

                if (value != null && value.length() > 0) {

                    try {

                        if (value.startsWith(".")) {

                            value = 0 + value;
                            edtFrom.setText(value);
                            edtFrom.setSelection(edtFrom.getText().length());
                        }
                        CountryUtil.setFromValue(getActivity(), value);
                        getRate();


                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        edtTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });


        fromHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromflagDialog.show(manager, "Flag");
            }
        });
        toHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toflagDialog.show(manager, "Flag");
            }
        });

        imgViceversa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromCountry = CountryUtil.getToCountry(getActivity());
                toCountry = CountryUtil.getFromCountry(getActivity());

                CountryUtil.setFromCountry(getActivity(), fromCountry);
                CountryUtil.setToCountry(getActivity(), toCountry);

                changeRateFlag();
                rotateViewClockwise(imgViceversa);
                setCountryNameandFlag(fromCountry, 0);
                setCountryNameandFlag(toCountry, 1);

                getRate();
            }
        });

        tapToLarge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent chartActivity = new Intent(getActivity(), ChartActivity.class);
                chartActivity.putExtra(Intent.EXTRA_UID, mViewPager.getCurrentItem());
                getActivity().startActivity(chartActivity);

            }
        });
    }


    private void changeRate(Country country, int i) {

        try {
            getRate();
            setCountryNameandFlag(country, i);
            changeRateFlag();
        } catch (Exception e) {

        }


    }


    private void changeRateFlag() {

        fromCountry = CountryUtil.getFromCountry(getActivity());
        toCountry = CountryUtil.getToCountry(getActivity());

        Fragment fragment = mImageAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment instanceof ImageFragment) {

            ImageFragment imageFragment = (ImageFragment) fragment;
            String time = YahooAPi.maps[mViewPager.getCurrentItem()];
            String url = String.format("http://chart.finance.yahoo.com/z?s=%s%s=x&t=%s&q=l&m=on&z=l", fromCountry.shortName, toCountry.shortName, time);
            imageFragment.changeImage(url);

        }

    }

    private void setCountryNameandFlag(Country country, int i) {
        if (country != null) {
            if (i == 0) {
                txtFrom.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
                imgFrom.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_in_left));
                txtFrom.setText(country.shortName);
                imgFrom.setImageResource(CountryUtil.getResourceId(getActivity(), "flag_" + country.shortName.toLowerCase()));
            } else if (i == 1) {
                txtTo.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right));
                imgTo.startAnimation(AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right));
                txtTo.setText(country.shortName);
                imgTo.setImageResource(CountryUtil.getResourceId(getActivity(), "flag_" + country.shortName.toLowerCase()));
            }
        }

    }

    private void callWebService(final boolean isEdtFrom) {


        try {
            chartHolder.setVisibility(View.GONE);
            txtoffLineMode.setVisibility(View.GONE);
            //   Toast.makeText(getActivity(), "online", Toast.LENGTH_SHORT).show();

            String query = getQuery(true);
            Call<YahooFinanceReal> yahooFinanceRealCall = yahoofinanceReal.getCurrency(query);
            CountryUtil.setFromValue(getActivity(), edtFrom.getText().toString());
            yahooFinanceRealCall.enqueue(new Callback<YahooFinanceReal>() {
                @Override
                public void onResponse(Response<YahooFinanceReal> response, Retrofit retrofit) {


                    try {
                        YahooFinanceReal yahooFinanceReal = response.body();
                        if (yahooFinanceReal != null) {
                            Rate rate = yahooFinanceReal.query.results.rate.get(0);

                            if (isEdtFrom) {
                                double fromValue = Double.valueOf(edtFrom.getText().toString());
                                double toValue = Double.valueOf(rate.Rate);
                                double totalValue = fromValue * toValue;
                                edtTo.setText(String.format("%.4f", totalValue));
                                CountryUtil.setToValue(getActivity(), String.valueOf(totalValue));

                            } else {

                                double toValue = Double.valueOf(edtTo.getText().toString());
                                double fromValue = Double.valueOf(rate.Rate);
                                double totalValue = fromValue * toValue;
                                edtFrom.setText(String.format("%.4f", totalValue));
                            }

                        } else {

                            edtTo.setText("0");
                        }
                        CountryUtil.setDateAndTime(getActivity());
                        mainActivity.setLastUpdatedText();
                    } catch (Exception e) {
                        edtTo.setText("0");
                    }


                }

                @Override
                public void onFailure(Throwable t) {
                    mProgressBar.setVisibility(View.GONE);
                }
            });

        } catch (Exception e) {
            edtTo.setText("0");
        }

    }


    public void callWebServiceAll() {


        Log.e("callWebServiceAll", "callWebServiceAll");

        final ImageView imageView = mainActivity.refresh;
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);

        imageView.startAnimation(animation);


        final String query = getQuery(false);
        Call<YahooFinanceReal> yahooFinanceRealCall = yahoofinanceReal.getCurrency(query);
        yahooFinanceRealCall.enqueue(new Callback<YahooFinanceReal>() {
            @Override
            public void onResponse(Response<YahooFinanceReal> response, Retrofit retrofit) {
                try {
                    YahooFinanceReal yahooFinanceReal = response.body();
                    if (yahooFinanceReal != null && yahooFinanceReal.query.results.rate.size() > 0) {
                        databaseHandler.saveAllRate(yahooFinanceReal.query.results.rate);
                        CountryUtil.setIsfirstTime(getActivity(), true);
                        CountryUtil.setDateAndTime(getActivity());
                        mainActivity.setLastUpdatedText();
                        offlineModeData(true);
                    }

                } catch (Exception e) {

                }

                imageView.clearAnimation();

            }

            @Override
            public void onFailure(Throwable t) {

                imageView.clearAnimation();

            }
        });
    }


    public void offlineModeData(boolean isFromEdt) {

        try {
            Log.e(CurrencyFragment.class.getName(), "offlineModeData");
            chartHolder.setVisibility(View.GONE);
            fromCountry = CountryUtil.getFromCountry(getActivity());
            toCountry = CountryUtil.getToCountry(getActivity());

            Rate fromCountryRate = databaseHandler.getRate(mUSD + fromCountry.shortName.toUpperCase());
            Rate toCountryRate = databaseHandler.getRate(mUSD + toCountry.shortName.toUpperCase());

            double fromRate = Double.valueOf(fromCountryRate.Rate);
            double toRate = Double.valueOf(toCountryRate.Rate);
            double finalRate = 0;
            if (isFromEdt) {
                double v = Double.valueOf(edtFrom.getText().toString());
                finalRate = v * (toRate / fromRate);
                if (Double.isNaN(finalRate)) {
                    edtTo.setText(String.format("%.4f", 0.0));
                } else {
                    edtTo.setText(String.format("%.4f", finalRate));
                }
                CountryUtil.setToValue(getActivity(), String.valueOf(finalRate));
            } else {
                finalRate = fromRate / toRate;
                edtFrom.setText(String.format("%.4f", finalRate));
            }
        } catch (Exception e) {
            edtTo.setText("0");
        }
    }

    @NonNull
    private String getQuery(boolean isFirstTime) {

        String finalString = null;
        String reqString = null;
        String query = null;

        fromCountry = CountryUtil.getFromCountry(getActivity());
        toCountry = CountryUtil.getToCountry(getActivity());

        if (!isOffline) {


            if (!isFirstTime) {
                finalString = CountryUtil.getToAllCountry(getActivity());
                query = "select * from yahoo.finance.xchange where pair in (" + finalString + " )";
            } else {
                finalString = fromCountry.shortName.toUpperCase() + toCountry.shortName.toUpperCase();
                reqString = "\"" + finalString + "\"" + "," + "\"" + usd + "\"";
                query = "select * from yahoo.finance.xchange where pair in (" + reqString + ")";
            }
        } else {

            finalString = "\"" + mUSD + fromCountry.shortName.toUpperCase() + "," + mUSD + toCountry.shortName.toUpperCase() + "\"";
            query = "select * from rate  where Name in (" + finalString + " )";
        }

        return query;
    }


    private void getUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isOffline = sharedPrefs.getBoolean("pref_offline", false);

        String theme = sharedPrefs.getString("prefTheme", "0");
        int a = Integer.valueOf(theme);
        if (isOffline) {
            if (a == 0) {
                a = 3;
            }
        }
        CountryUtil.setTheme(a, getActivity(), mainActivity.getToolbar(), mainHolder, fromHolderCard, toHolderCard, txtFrom, txtTo, imgViceversa, edtFrom, edtTo);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textView_oneday:
                //map("1d");
                new MAPS("1d", im_chart).execute();
                break;
            case R.id.textView_fiveday:
                //map("5d");
                new MAPS("5d", im_chart).execute();
                break;
            case R.id.textView_threemonth:
                //map("3m");
                new MAPS("3m", im_chart).execute();
                break;
            case R.id.textView_sixmonth:
                intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("spinnerone", fromCountry.shortName.toUpperCase());
                intent.putExtra("spinnertwo", toCountry.shortName.toUpperCase());
                intent.putExtra("Time", "6m");
                startActivity(intent);
                break;
            case R.id.textView_oneyear:
                intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("spinnerone", fromCountry.shortName.toUpperCase());
                intent.putExtra("spinnertwo", toCountry.shortName.toUpperCase());
                intent.putExtra("Time", "1y");
                startActivity(intent);
                break;
            case R.id.textView_twoyear:
                intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("spinnerone", fromCountry.shortName.toUpperCase());
                intent.putExtra("spinnertwo", toCountry.shortName.toUpperCase());
                intent.putExtra("Time", "2y");
                startActivity(intent);
                break;
            case R.id.textView_fiveyear:
                intent = new Intent(getActivity(), ChartActivity.class);
                intent.putExtra("spinnerone", fromCountry.shortName.toUpperCase());
                intent.putExtra("spinnertwo", toCountry.shortName.toUpperCase());
                intent.putExtra("Time", "5y");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    public void favClick(View view) {
        startActivity(new Intent(getActivity(), FavDetailsActivity.class));
    }

    public class MAPS extends AsyncTask<String, String, String> {
        String time;
        ImageView img;
        Bitmap bitmap_skip;

        public MAPS(String string, ImageView iv) {
            time = string;
            img = iv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                fromCountry = CountryUtil.getFromCountry(getActivity());
                toCountry = CountryUtil.getToCountry(getActivity());

                String url = String.format("http://chart.finance.yahoo.com/z?s=%s%s=x&t=%s&q=l&m=on&z=m", fromCountry.shortName, toCountry.shortName, time);
                Log.d("url", url);


                bitmap_skip = BitmapFactory.decodeStream((InputStream)
                        new URL(url)
                                .getContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            img.setImageBitmap(bitmap_skip);
            super.onPostExecute(result);
            mProgressBar.setVisibility(View.GONE);
        }

    }


    public void rotateViewClockwise(ImageView imageview) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ObjectAnimator imageViewObjectAnimator = ObjectAnimator.ofFloat(imageview, "rotation", 0f, 180f);
            imageViewObjectAnimator.setDuration(500);
            imageViewObjectAnimator.start();
        }

    }

    private void getRate() {
        if (!isOffline) {
            if (CountryUtil.isConnected(getActivity())) {
                offlineModeData(true);
            } else {
                offlineModeData(true);
            }

        } else {
            offlineModeData(true);
        }
    }
}
