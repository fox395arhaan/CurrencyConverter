package com.currencyapp.currencyconverter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.currencyapp.currencyconverter.util.DatabaseHandler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class AddToFavActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private AddToFavAdapter mAdapter;
    private Context mContext;
    Button btnShow;
    List<Country> countries;
    DatabaseHandler databaseHandler;
    EditText editText;
    Set<Country> selectedCountry;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_fav);
        init();
    }

    private void init() {

        initToolBar();
        selectedCountry = new HashSet<>();
        editText = (EditText) findViewById(R.id.edtSearch);
        databaseHandler = new DatabaseHandler(this);
        countries = databaseHandler.getAllContries();
        mAdapter = new AddToFavAdapter(countries);
        mContext = this.getApplicationContext();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);


        btnShow = (Button) findViewById(R.id.btnShow);


        selectedCountry.addAll(databaseHandler.getAllContries(true));

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProgressDialog progressDialog = ProgressDialog.show(AddToFavActivity.this, "Save data", "adding country to favourite list");


                try {

                    if (selectedCountry.size() >= 2) {

                        openDatabase();
                        databaseHandler.updateCourtryTofalse();
                        for (Country country : selectedCountry) {

                            databaseHandler.updateCountry(country);
                        }
                        finish();
                    } else {
                        Toast.makeText(AddToFavActivity.this, "Please select two or more countries", Toast.LENGTH_LONG).show();
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }


            }


        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() > 0) {
                    openDatabase();

                    countries = databaseHandler.searchContries(s.toString());
                    mAdapter.setCoutry(countries);

                } else {
                    countries = databaseHandler.getAllContries();
                    mAdapter.setCoutry(countries);

                }

                if (countries.size() == 0) {
                    Toast.makeText(AddToFavActivity.this, "No Countries found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter.setCountryClick(new CountryClick() {
            @Override
            public void changeCountry(int position, Country country, CheckBox checkBox) {
                if (country.isSelected) {


                    country.isSelected = false;
                    checkBox.setChecked(false);
                    if (selectedCountry.contains(country)) {
                        selectedCountry.remove(country);
                    }

                    //  databaseHandler.updateCountry(country);
                } else {
                    country.isSelected = true;
                    checkBox.setChecked(true);
                    if (!selectedCountry.contains(country)) {
                        selectedCountry.add(country);
                    }
                    //databaseHandler.updateCountry(country);

                }
            }
        });
    }

    private void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add to Favourite");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void openDatabase() {
        if (databaseHandler == null) {
            databaseHandler = new DatabaseHandler(AddToFavActivity.this);
        }
    }

    private class AddToFavAdapter extends RecyclerView.Adapter<AddToFavViewHolder> {

        List<Country> countries;
        CountryClick countryClick;

        public void setCountryClick(CountryClick countryClick) {
            this.countryClick = countryClick;
        }

        public AddToFavAdapter(List<Country> countries) {
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


            if (selectedCountry.contains(country)) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
            holder.flag.setImageResource(country.imageId);
            holder.tvFullName.setText(country.fullName);
            holder.tvShortName.setText(country.shortName);
            //holder.checkBox.setChecked(country.isSelected);

            holder.mainHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    countryClick.changeCountry(position, country, holder.checkBox);
                }
            });
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    countryClick.changeCountry(position, country, holder.checkBox);
//                    if (country.isSelected) {
//
//
//                        country.isSelected = false;
//                        holder.checkBox.setChecked(false);
//                        if (selectedCountry.contains(country)) {
//                            selectedCountry.remove(country);
//                        }
//
//                        //  databaseHandler.updateCountry(country);
//                    } else {
//                        country.isSelected = true;
//                        holder.checkBox.setChecked(true);
//                        if (!selectedCountry.contains(country)) {
//                            selectedCountry.add(country);
//                        }
//                        //databaseHandler.updateCountry(country);
//
//                    }
                }
            });


        }


        @Override
        public int getItemCount() {
            return countries.size();
        }


        public List<Country> getCountries() {
            return countries;
        }

        public void setCoutry(List<Country> countries) {
            this.countries.clear();
            this.countries = countries;
            notifyDataSetChanged();

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
            flag = (ImageView) itemView.findViewById(R.id.flag);
            mainHolder = (CardView) itemView.findViewById(R.id.mainHolder);
        }
    }


    public interface CountryClick {

        void changeCountry(int position, Country country, CheckBox checkBox);
    }

}
