package com.example.samuel.sawft.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Samuel on 17/09/2017.
 */

public class SectionStatePagerAdapter extends FragmentStatePagerAdapter{
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private HashMap<Fragment,Integer> mFragments = new HashMap<>();
    private HashMap<String,Integer> mFragmentNumbers = new HashMap<>();
    private HashMap<Integer,String> mFragmentNames = new HashMap<>();
    public SectionStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragments(Fragment fm,String fmName){
        fragments.add(fm);
        mFragments.put(fm,fragments.size()-1);
        mFragmentNumbers.put(fmName,fragments.size());
        mFragmentNames.put(fragments.size()-1,fmName);


    }

    public Integer getFragmentNumber(Fragment fragment){
        if(mFragmentNumbers.containsKey(fragment)){
           return mFragmentNumbers.get(fragment);
        }
        else{
            return null;
        }

    }

    public Integer getFragmentNumber(String fragmentName){
        if(mFragmentNumbers.containsKey(fragmentName)){
            return mFragmentNumbers.get(fragmentName);
        }
        else{
            return null;
        }

    }
    public String getFragmentName(Integer fragmentNo){
        if(mFragmentNames.containsKey(fragmentNo)){
            return mFragmentNames.get(fragmentNo);
        }
        else{
            return  null;
        }
    }
}
