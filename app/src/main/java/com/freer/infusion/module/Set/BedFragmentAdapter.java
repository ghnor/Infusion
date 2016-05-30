package com.freer.infusion.module.Set;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 2172980000773 on 2016/5/11.
 */
public class BedFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    private List<String> mTitleList = new ArrayList<String>();

    public BedFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        if (position<mFragmentList.size()) {
            return mFragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position < mTitleList.size()) {
            return mTitleList.get(position);
        }
        return null;
    }
}
