package com.emirhalici.myenglishdictionary.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.emirhalici.myenglishdictionary.fragments.AddFragment;
import com.emirhalici.myenglishdictionary.fragments.FavouritesFragment;
import com.emirhalici.myenglishdictionary.fragments.HomeFragment;
import com.emirhalici.myenglishdictionary.fragments.StudyListFragment;

public class StateAdapter extends FragmentStateAdapter {

    public StateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new FavouritesFragment();
            case 2:
                return new StudyListFragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
