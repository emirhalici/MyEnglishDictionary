package com.emirhalici.myenglishdictionary.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emirhalici.myenglishdictionary.adapters.AllWordsAdapter;
import com.emirhalici.myenglishdictionary.utils.DatabaseHelper;
import com.emirhalici.myenglishdictionary.activities.MainActivity;
import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.models.WordModel;

import java.util.ArrayList;
import java.util.Objects;

public class HomeFragment extends Fragment {
    public String sortType;
    public ArrayList<String> typeFilter;
    //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    //String sortType = sharedPreferences.getString("sortType","word_asc");
    // PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("sortType","word_asc");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.rv_home);
        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        ArrayList<WordModel> wordList = databaseHelper.getEveryWord();

        Bundle args = getArguments();
        try {
            if (args != null) {
                sortType = args.getString("sortType"); }
            else {
                sortType = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("sortType","word_asc");
            }
        } catch (NullPointerException e) {
            sortType = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("sortType","word_asc");
        }
        if (args!=null) {
            try {
                if (args.containsKey("typeFilter")) {
                    typeFilter = args.getStringArrayList("typeFilter");
                    wordList = databaseHelper.getbyType(typeFilter);
                    Log.e("HomeFragment",sortType + " typefilter");

                }
            } catch (NullPointerException e) {Log.e("HomeFragment",sortType + " typefilter nullpointerexception");}
        }
        MainActivity activity = (MainActivity) getActivity();

        if (Objects.equals(sortType, "date_desc")) {
            wordList = databaseHelper.sortByDate();
            //Collections.reverse(wordList);
        } else if (Objects.equals(sortType, "word_asc")) {
            wordList = databaseHelper.sortByWordName(wordList);
        } else if (Objects.equals(sortType, "word_desc")) {
            wordList = databaseHelper.sortByWordNameD(wordList);
        } else if (Objects.equals(sortType, "type_asc")) {
            wordList = databaseHelper.sortByWordType(wordList);
        } else if (Objects.equals(sortType, "type_desc")) {
            wordList = databaseHelper.sortByWordTypeD(wordList);
        }

        assert activity != null;
        if (activity.finalWordList!=null && !activity.finalWordList.isEmpty() && activity.finalWordList!=databaseHelper.getEveryWord() && activity.finalWordList.size()!=databaseHelper.getEveryWord().size()) {
            activity.applyQueryTextChange();
            wordList = activity.finalWordList;
        }

        //mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AllWordsAdapter(wordList, getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

}
