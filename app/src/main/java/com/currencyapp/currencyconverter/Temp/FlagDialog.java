package com.currencyapp.currencyconverter.Temp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.currencyapp.currencyconverter.AddToFavActivity;
import com.currencyapp.currencyconverter.Country;
import com.currencyapp.currencyconverter.R;
import com.currencyapp.currencyconverter.util.CountryUtil;
import com.currencyapp.currencyconverter.util.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by raghav on 11/1/16.
 */
public class FlagDialog extends DialogFragment {

    private Button allCurrencies;
    private ImageView imgCancel;
    private RecyclerView mRecyclerView;
    private AddToFavAdapter mAdapter;
    private ArrayList<Country> countries;
    private DatabaseHandler databaseHandler;
    private SetCountryListener setCountryListener;

    public void setSetCountryListener(SetCountryListener setCountryListener) {
        this.setCountryListener = setCountryListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHandler = new DatabaseHandler(getActivity());
        countries = databaseHandler.getAllContries(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.flag_dialog, container, false);
        init(rootView);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    private void init(View rootView) {

        imgCancel = (ImageView) rootView.findViewById(R.id.imgCancel);
        allCurrencies = (Button) rootView.findViewById(R.id.allCurrencies);
        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        allCurrencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                startActivity(new Intent(getActivity(), AddToFavActivity.class));

            }
        });


        mAdapter = new AddToFavAdapter(countries);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setCountryClick(new AddToFavActivity.CountryClick() {
            @Override
            public void changeCountry(int position, Country country, CheckBox checkBox) {

                setCountryListener.setCountry(country);
                dismiss();
            }
        });
    }

    private class AddToFavAdapter extends RecyclerView.Adapter<AddToFavViewHolder> {

        ArrayList<Country> countries;
        AddToFavActivity.CountryClick countryClick;

        public void setCountryClick(AddToFavActivity.CountryClick countryClick) {
            this.countryClick = countryClick;
        }

        public AddToFavAdapter(ArrayList<Country> countries) {

            this.countries = countries;
        }

        @Override
        public AddToFavViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_fav, null);
            AddToFavViewHolder addToFavViewHolder = new AddToFavViewHolder(view);
            return addToFavViewHolder;
        }

        @Override
        public void onBindViewHolder(final AddToFavViewHolder holder, final int position) {

            final Country country = countries.get(position);
            holder.flag.setImageResource(CountryUtil.getResourceId(getActivity(), "flag_" + country.shortName.toLowerCase()));
            holder.tvFullName.setText(country.fullName);
            holder.tvShortName.setText(country.shortName);
            holder.mainHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countryClick.changeCountry(position, country, holder.checkBox);
                }
            });


        }

        @Override
        public int getItemCount() {
            return countries.size();
        }


    }

    private class AddToFavViewHolder extends RecyclerView.ViewHolder {

        TextView tvShortName, tvFullName;
        CheckBox checkBox;
        ImageView flag;
        CardView mainHolder;

        public AddToFavViewHolder(View itemView) {
            super(itemView);
            tvShortName = (TextView) itemView.findViewById(R.id.tvShortName);
            tvFullName = (TextView) itemView.findViewById(R.id.tvFullName);
            checkBox = (CheckBox) itemView.findViewById(R.id.chkSelected);
            checkBox.setVisibility(View.GONE);
            flag = (ImageView) itemView.findViewById(R.id.flag);
            mainHolder = (CardView) itemView.findViewById(R.id.mainHolder);
        }
    }

    public interface SetCountryListener {
        void setCountry(Country country);

    }
}
