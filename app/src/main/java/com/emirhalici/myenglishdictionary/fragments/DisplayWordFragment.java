package com.emirhalici.myenglishdictionary.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emirhalici.myenglishdictionary.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DisplayWordFragment extends Fragment {

    private static final String WORD = "word", TYPE = "type", EXAMPLE = "example", DEFINITION = "definition", ID = "id";
    String word, type, example, definition;
    int id;

    public DisplayWordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(WORD, word);
        outState.putString(TYPE, type);
        outState.putString(EXAMPLE, example);
        outState.putString(DEFINITION, definition);
        outState.putInt(ID, id);
    }

    public static DisplayWordFragment newInstance(int pid, String pword, String ptype, String pexample, String pdefinition) {
        DisplayWordFragment fragment = new DisplayWordFragment();
        Bundle args = new Bundle();
        args.putString(WORD, pword);
        args.putString(TYPE, ptype);
        args.putString(EXAMPLE, pexample);
        args.putString(DEFINITION, pdefinition);
        args.putInt(ID, pid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            word = savedInstanceState.getString(WORD);
            type = savedInstanceState.getString(TYPE);
            example = savedInstanceState.getString(EXAMPLE);
            definition = savedInstanceState.getString(DEFINITION);
            id = savedInstanceState.getInt(ID);
        } else if (getArguments() != null) {
            word = getArguments().getString(WORD);
            type = getArguments().getString(TYPE);
            example = getArguments().getString(EXAMPLE);
            definition = getArguments().getString(DEFINITION);
            id = getArguments().getInt(ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_display_word, container, false);
        TextView tv_wordName = v.findViewById(R.id.tv_wordName);
        TextView tv_wordType = v.findViewById(R.id.tv_wordType);
        TextView tv_wordDef = v.findViewById(R.id.tv_wordDef);
        TextView tv_wordEx = v.findViewById(R.id.tv_wordEx);

        tv_wordDef.setText(definition);
        tv_wordEx.setText(example);
        tv_wordName.setText(word);
        tv_wordType.setText(type);

        FloatingActionButton fab_edit = v.findViewById(R.id.fab_edit);

        fab_edit.setOnClickListener(v1 -> getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).
                replace(R.id.fragment_container, AddMFragment.newInstance(id)).addToBackStack(null).commit());

        return v;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.findItem(R.id.sort).setVisible(false);
        menu.findItem(R.id.filter).setVisible(false);
        menu.findItem(R.id.compact).setVisible(false);
        menu.findItem(R.id.app_bar_search).setVisible(false);
        menu.findItem(R.id.dark).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}