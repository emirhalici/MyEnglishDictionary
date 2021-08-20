package com.emirhalici.myenglishdictionary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.emirhalici.myenglishdictionary.adapters.AddWordAdapter;
import com.emirhalici.myenglishdictionary.utils.ApiHelper;
import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.models.WordModel;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    Button btn_search, btn_manual;
    TextInputLayout tf_word;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btn_manual = view.findViewById(R.id.btn_manual);
        btn_search = view.findViewById(R.id.btn_search);
        tf_word = view.findViewById(R.id.tf_word);
        mRecyclerView = view.findViewById(R.id.rv);

        btn_manual.setOnClickListener(v -> {
            // getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddMFragment()).commit();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, AddMFragment.newInstance(-1)).addToBackStack(null).commit();
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    public void run() {
                        // a potentially time consuming task

                        JSONObject responseWord = null;
                        String word = tf_word.getEditText().getText().toString();
                        if (word!="") {
                            word.toLowerCase();
                            responseWord = ApiHelper.searchWord(word);
                        }

                        // if null then word is either incorrect or not absent in online dictionary
                        if (responseWord!=null) {
                            ArrayList<WordModel> wordList = new ArrayList<>();
                            try {
                                JSONArray definitions = responseWord.getJSONArray("definitions");
                                for (int i = 0; i < definitions.length(); i++) {
                                    JSONObject def = definitions.getJSONObject(i);
                                    WordModel wordModel = new WordModel(-1, word, def.getString("type"), def.getString("definition"), def.getString("example"));
                                    wordList.add(wordModel);

                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            // Stuff that updates the UI
                                            mRecyclerView.setHasFixedSize(true);
                                            mLayoutManager = new LinearLayoutManager(getContext());
                                            mAdapter = new AddWordAdapter(wordList, getContext());
                                            mRecyclerView.setLayoutManager(mLayoutManager);
                                            mRecyclerView.setAdapter(mAdapter);
                                        }
                                    });
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            // when it's null

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Stuff that updates the UI
                                    if (word.isEmpty()) {
                                        Toast.makeText(getContext(), getString(R.string.AddWordEmptyBox), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), getString(R.string.ToastErrorResponse) , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }).start();

            }
        });
    }

}
