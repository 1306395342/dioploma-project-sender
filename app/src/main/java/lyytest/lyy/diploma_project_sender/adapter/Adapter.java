package lyytest.lyy.diploma_project_sender.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Adapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> fragments;
    private ArrayList<String> list;

    public Adapter(FragmentManager fm, ArrayList<Fragment> fragments, ArrayList<String> list) {
        super(fm);
        this.fragments = fragments;
        this.list = list;
    }

    public Fragment getItem(int position) {
        return fragments.get(position);
    }


    public int getCount() {
        return fragments.size();
    }


    public CharSequence getPageTitle(int position) {
        return list.get(position);
    }
}