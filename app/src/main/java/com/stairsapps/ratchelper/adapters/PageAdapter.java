package com.stairsapps.ratchelper.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.stairsapps.ratchelper.FakeSMS;
import com.stairsapps.ratchelper.Inspectors;

/**
 * Created by filip on 6/20/2016.
 */
public class PageAdapter extends FragmentStatePagerAdapter{

    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                FakeSMS tab2 = new FakeSMS();
                return tab2;
            case 1:
                Inspectors tab1 = new Inspectors();
                return  tab1;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
