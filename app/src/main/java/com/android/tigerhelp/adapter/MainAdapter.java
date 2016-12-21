package com.android.tigerhelp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Charles on 2016/12/21.
 */

public class MainAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public MainAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        if (list == null) {
            list = new ArrayList<>();
        } else {
            this.list = list;
        }
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {

        return list.size() > 0 ? list.size() : 0;
    }
}
