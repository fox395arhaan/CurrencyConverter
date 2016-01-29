package com.currencyapp.currencyconverter;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.currencyapp.currencyconverter.util.CountryUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {
    private int mPosition = 0;
    private String mapsID = "3m";
    ImageView mTextView;
    private Country toCountry, fromCountry;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mPosition = bundle.getInt("position");
            mapsID = bundle.getString("mapId");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        mTextView = (ImageView) view.findViewById(R.id.imageView);

        return view;
    }

    public static Fragment newInstance(int position, String mapTime) {
        final ImageFragment imageFragment = new ImageFragment();
        final Bundle args = new Bundle();
        args.putInt("position", position);
        args.putString("mapId", mapTime);
        imageFragment.setArguments(args);


        return imageFragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        fromCountry = CountryUtil.getFromCountry(getActivity());
        toCountry = CountryUtil.getToCountry(getActivity());

        String url = String.format("http://chart.finance.yahoo.com/z?s=%s%s=x&t=%s&q=l&m=on&z=m", fromCountry.shortName, toCountry.shortName, "3m");
        Glide.clear(mTextView);
        Glide.with(getActivity()).load(url).into(mTextView);
        mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chartActivity = new Intent(getActivity(), ChartActivity.class);
                chartActivity.putExtra(Intent.EXTRA_UID, mPosition);
                getActivity().startActivity(chartActivity);
            }
        });
    }


    public void changeImage(String s) {

        if (mTextView != null) {
            Glide.clear(mTextView);
            Glide.with(getActivity()).load(s).into(mTextView);
        }
    }

}
