package com.currencyapp.currencyconverter;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.currencyapp.currencyconverter.Model.Rate;
import com.currencyapp.currencyconverter.Model.YahooFinanceReal;
import com.currencyapp.currencyconverter.Temp.MainActivity;
import com.currencyapp.currencyconverter.util.CountryUtil;
import com.currencyapp.currencyconverter.util.DatabaseHandler;
import com.currencyapp.currencyconverter.util.Interfaces;
import com.currencyapp.currencyconverter.util.MyApplication;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavDeailsFragment extends Fragment {


    private Button allCurrencies;
    private ImageView imgCancel;
    private RecyclerView mRecyclerView;
    private AddToFavAdapter mAdapter;
    private ArrayList<Country> countries;
    private DatabaseHandler databaseHandler;
    private boolean isOffline;
    private static final String usd = "USD";
    private ArrayList<Rate> rates;
    private Country fromCountry;
    private Rate fromRate;
    private Interfaces.YahoofinanceReal yahoofinanceReal;
    // private ProgressDialog progressDialog;

    int counter = 0;
    double fromRateValue = 1.0;
    private MainActivity mainActivity;

    public FavDeailsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        countries = new ArrayList<>();
        databaseHandler = new DatabaseHandler(getActivity());
        //progressDialog = ProgressDialog.show(getActivity(), "Please wait", "Updating Rates");
        // progressDialog.dismiss();
        mainActivity = (MainActivity) getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_fav_deails, container, false);

        init(rootView);
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            countries = databaseHandler.getAllContries(true);

            getFavCountryList(false);

        }
    }

    private void init(View rootView) {


        mAdapter = new AddToFavAdapter(countries);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);

    }


    public void getFavCountryList(final boolean isAnimated) {


        getUserSettings();

        new android.os.Handler().post(new Runnable() {
            @Override
            public void run() {

                try {
                    //progressDialog.show();
                    fromCountry = CountryUtil.getFromCountry(getActivity());
                    fromRate = databaseHandler.getRate(usd + fromCountry.shortName.toUpperCase());
                    fromRateValue = Double.parseDouble(fromRate.Rate);
                    if (fromRateValue == 0) {
                        fromRateValue = 1;
                    }
                    String countryName = null;
                    if (!isOffline) {

                        if (!CountryUtil.isConnected(getActivity())) {
                            // CountryUtil.showErrorAlert(getActivity(), "No Internet Connection.Please enable offline mode.");
                            // progressDialog.dismiss();
                            isOffline = true;
                            countryName = getCountryQuery(usd);
                            getRateOffLine(countryName);

                        } else {
                            //countryName = getCountryQuery(fromCountry.shortName.toUpperCase());
                            countryName = getCountryQuery(usd);
                            getRateOffLine(countryName);
                        }

                    } else {

                        countryName = getCountryQuery(usd);
                        getRateOffLine(countryName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


    }

    private void getRateOnline(String countryName, final boolean isAnimate) {

        final ImageView imageView = mainActivity.refresh;
        final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.rotation);
        imageView.startAnimation(animation);

        String query = "select * from yahoo.finance.xchange where pair in (" + countryName + ")";
        yahoofinanceReal = MyApplication.getRetrofit().create(Interfaces.YahoofinanceReal.class);
        Call<YahooFinanceReal> yahooFinanceRealCall = yahoofinanceReal.getCurrency(query);
        yahooFinanceRealCall.enqueue(new Callback<YahooFinanceReal>() {
            @Override
            public void onResponse(Response<YahooFinanceReal> response, Retrofit retrofit) {
                try {
                    YahooFinanceReal yahooFinanceReal = response.body();
                    if (yahooFinanceReal != null && yahooFinanceReal.query.results.rate.size() > 0) {
                        rates = yahooFinanceReal.query.results.rate;
                        mAdapter.setCountries(countries);
                    }
                } catch (Exception e) {
                    //progressDialog.dismiss();
                }
                // progressDialog.dismiss();
                CountryUtil.setDateAndTime(getActivity());
                mainActivity.setLastUpdatedText();
                imageView.clearAnimation();

            }

            @Override
            public void onFailure(Throwable t) {

                imageView.clearAnimation();

            }
        });

    }

    private void getRateOffLine(String countryName) {


        try {
            Log.e(FavDeailsFragment.class.getName(), "getRateOffLine");
            if (databaseHandler == null) {
                databaseHandler = new DatabaseHandler(getActivity());
            }
            rates = databaseHandler.getRateArrayList(countryName);
            if (rates.size() > 0) {

                mAdapter.setCountries(countries);
            } else {

                Toast.makeText(getActivity(), "Turn on data Connection and Refresh the data.", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {

        }
    }

    private String getCountryQuery(String s) {


        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < countries.size(); i++) {
            String v = "\"" + s + countries.get(i).shortName.toUpperCase() + "\"";
            if (countries.size() - 1 == i) {
                stringBuilder.append(v);
            } else {
                stringBuilder.append(v + ",");
            }

        }

        Log.v("data", stringBuilder.toString());

        return stringBuilder.toString();
    }

    private void getUserSettings() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        isOffline = sharedPrefs.getBoolean("pref_offline", false);


    }

    private class AddToFavAdapter extends RecyclerView.Adapter<AddToFavViewHolder> {

        ArrayList<Country> countries;


        public void setCountries(ArrayList<Country> countries) {
            this.countries = new ArrayList<>();
            this.countries = countries;
            notifyDataSetChanged();
        }

        public AddToFavAdapter(ArrayList<Country> countries) {

            this.countries = countries;
        }

        @Override
        public AddToFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav_details, null);
            AddToFavViewHolder addToFavViewHolder = new AddToFavViewHolder(view);
            return addToFavViewHolder;
        }

        @Override
        public void onBindViewHolder(final AddToFavViewHolder holder, final int position) {

            final Country country = countries.get(position);
            Rate rate = rates.get(position);
            holder.flag.setImageResource(CountryUtil.getResourceId(getActivity(), "flag_" + country.shortName.toLowerCase()));
            holder.tvShortName.setText(String.format("%s-%s", country.shortName, country.fullName));
            double v = Double.valueOf(CountryUtil.getFromValue(getActivity()));

            if (!isOffline) {

                Double finalerate = 0.0;
                Double finalerate2 = 0.0;
                if (rate.Rate != null && !rate.Rate.equalsIgnoreCase("N/A")) {

                    finalerate = Double.valueOf(rate.Rate);
                    finalerate = finalerate / fromRateValue;
                    finalerate2 = v * finalerate;
                }

                holder.tvbaseValue.setText(String.format("%s %s= %.4f %s", "1", fromCountry.shortName.toUpperCase(), finalerate, country.shortName.toUpperCase()));
                holder.tvValue.setText(String.format("%.4f", finalerate2));
            } else {
                double Fromrate = calCulateRate(rate);
                holder.tvbaseValue.setText(String.format("%s %s= %.4f %s", "1", fromCountry.shortName.toUpperCase(), Fromrate, country.shortName.toUpperCase()));
                holder.tvValue.setText(String.format("%.4f", v * Fromrate));
            }


        }

        @Override
        public int getItemCount() {
            return countries.size();
        }


    }

    private double calCulateRate(Rate rate) {
        double finalRate = 0;
        if (rate.Rate != null && !rate.Rate.equalsIgnoreCase("N/A")) {

            finalRate = Double.valueOf(rate.Rate);
            if (!fromCountry.shortName.equalsIgnoreCase(usd)) {
                double fRate = Double.valueOf(fromRate.Rate);
                double toRate = Double.valueOf(rate.Rate);
                finalRate = (toRate / fRate);

            }

        }
        return finalRate;
    }

    private class AddToFavViewHolder extends RecyclerView.ViewHolder {

        TextView tvShortName, tvValue, tvbaseValue;

        ImageView flag;
        CardView mainHolder;

        public AddToFavViewHolder(View itemView) {
            super(itemView);
            tvShortName = (TextView) itemView.findViewById(R.id.tvShortName);
            tvValue = (TextView) itemView.findViewById(R.id.tvValue);
            tvbaseValue = (TextView) itemView.findViewById(R.id.tvbaseValue);

            flag = (ImageView) itemView.findViewById(R.id.flag);
            mainHolder = (CardView) itemView.findViewById(R.id.mainHolder);
        }
    }


}
