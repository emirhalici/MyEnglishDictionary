package com.emirhalici.myenglishdictionary.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.emirhalici.myenglishdictionary.fragments.AboutFragment;
import com.emirhalici.myenglishdictionary.fragments.AddFragment;
import com.emirhalici.myenglishdictionary.fragments.HomeFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new AboutFragment();
            case 2:
                return new AddFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
