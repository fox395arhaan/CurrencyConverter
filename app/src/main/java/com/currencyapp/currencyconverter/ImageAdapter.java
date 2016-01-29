package com.currencyapp.currencyconverter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghav on 26/1/16.
 */
public class ImageAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public ImageAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
//        extends FragmentStatePagerAdapter {
//    private final int mSize = 6;
//
//    public ImageAdapter(FragmentManager fm) {
//        super(fm);
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return ImageFragment.newInstance(position);
//    }
//
//    @Override
//    public int getCount() {
//        return mSize;
//    }
//}

