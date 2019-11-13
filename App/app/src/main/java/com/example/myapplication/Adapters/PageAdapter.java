package com.example.myapplication.Adapters;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.myapplication.Fragments.*;

public class PageAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Bundle bundle;

    public PageAdapter(FragmentManager fm, int numberOfTabs, Bundle bundle) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.bundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                TimeLineFragment timeLineFragment = new TimeLineFragment();
                timeLineFragment.setArguments(bundle);
                return timeLineFragment;
            case 1:
                SearchFragment searchFragment = new SearchFragment();
                searchFragment.setArguments(bundle);
                return searchFragment;
            case 2:
                ProfileFragment profileFragment = new ProfileFragment();
                profileFragment.setArguments(bundle);
                return profileFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}
