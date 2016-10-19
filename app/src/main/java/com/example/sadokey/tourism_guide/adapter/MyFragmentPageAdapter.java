package com.example.sadokey.tourism_guide.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Ahmed AboMazin on 9/2/2016.
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {

    List<Fragment> mylist;


    public MyFragmentPageAdapter(FragmentManager fm,List<Fragment> mylist ) {
        super(fm);
        this.mylist=mylist;
    }

    @Override
    public Fragment getItem(int position) {
        return mylist.get(position);
    }

    @Override
    public int getCount() {
        return mylist.size();
    }
}
