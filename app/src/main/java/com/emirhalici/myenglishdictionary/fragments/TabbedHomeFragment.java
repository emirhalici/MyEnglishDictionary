package com.emirhalici.myenglishdictionary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.adapters.PagerAdapter;
import com.emirhalici.myenglishdictionary.adapters.StateAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class TabbedHomeFragment extends Fragment {

    public TabbedHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            TabLayout tabLayout = view.findViewById(R.id.tabLayout);
            TabItem tabDictionary = view.findViewById(R.id.tab_dictionary);
            TabItem tabFavourite = view.findViewById(R.id.tab_favourites);
            TabItem tabStudyList = view.findViewById(R.id.tab_studyList);
            ViewPager2 viewPager = view.findViewById(R.id.viewPager);

            StateAdapter stateAdapter = new StateAdapter(getChildFragmentManager(), getLifecycle());
            viewPager.setAdapter(stateAdapter);
            String[] tabText = {"Dictionary", "Favourites", "Study List"};
            new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabText[position])
            ).attach();

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_tabbed, container, false);
    }
}