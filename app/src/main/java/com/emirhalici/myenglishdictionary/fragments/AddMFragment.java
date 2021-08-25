package com.emirhalici.myenglishdictionary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.emirhalici.myenglishdictionary.utils.DatabaseHelper;
import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.models.WordModel;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMFragment extends Fragment {

    private static int wordId = -1;
    public String word, example, type;
    public static String WORD="word", DEFINITION="definition", EXAMPLE="example", TYPE="type", WORDID="wordId";

    public AddMFragment() {
        // Required empty public constructor
    }

    public static AddMFragment newInstance(Integer wordId) {
        AddMFragment fragment = new AddMFragment();
        Bundle args = new Bundle();
        args.putInt("wordId", wordId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (wordId==1) {
            // add manually
            // get the strings from textviews end put it in outstate
            outState.putString(WORD, tf_word.getEditText().getText().toString());
            outState.putString(DEFINITION, tf_definition.getEditText().getText().toString());
            outState.putString(EXAMPLE, tf_example.getEditText().getText().toString());
            outState.putString(TYPE, tf_type.getEditText().getText().toString());
            outState.putInt(WORDID, -1);
        } else {
            // edit word
            outState.putInt(WORDID, wordId);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) { wordId = getArguments().getInt(WORDID); }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    Button btn_add, btn_cancel;
    TextInputLayout tf_word, tf_type, tf_definition, tf_example;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btn_add = view.findViewById(R.id.btn_add);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        tf_word = view.findViewById(R.id.tf_word);
        tf_definition = view.findViewById(R.id.tf_definition);
        tf_example = view.findViewById(R.id.tf_example);
        tf_type = view.findViewById(R.id.tf_type);

        /*
         * This fragment is used 2 times. if wordId=-1, it is for adding
         * new words manually. if wordId is some integer fragment is used
         * to edit words.
         */
        if (wordId!=-1) {
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            // if fragment is recovered from rotation, set wordid
            if (savedInstanceState!=null) {
                wordId = savedInstanceState.getInt(WORDID);
            }
            // set the strings to their respective textviews
            WordModel wordModel = databaseHelper.getOne(wordId);
            btn_add.setText(getString(R.string.AddWordAlertDialogEdit));
            tf_word.getEditText().setText(wordModel.getWord());
            tf_type.getEditText().setText(wordModel.getType());
            tf_example.getEditText().setText(wordModel.getExample());
            tf_definition.getEditText().setText(wordModel.getDefinition());
        } else {
            // if fragment is recovered from rotation, set textviews.
            if (savedInstanceState!=null) {
                Toast.makeText(getContext(), savedInstanceState.getString(WORD) ,Toast.LENGTH_SHORT).show();
                tf_word.getEditText().setText(savedInstanceState.getString(WORD));
                tf_type.getEditText().setText(savedInstanceState.getString(TYPE));
                tf_example.getEditText().setText(savedInstanceState.getString(EXAMPLE));
                tf_definition.getEditText().setText(savedInstanceState.getString(DEFINITION));
            }
        }

        btn_cancel.setOnClickListener(v -> getFragmentManager().popBackStack());
        btn_add.setOnClickListener(v -> {
            String word = tf_word.getEditText().getText().toString();
            String definition = tf_definition.getEditText().getText().toString();
            String example = tf_example.getEditText().getText().toString();
            String type = tf_type.getEditText().getText().toString();
            if (!word.isEmpty() && !definition.isEmpty() && !example.isEmpty() && !type.isEmpty()) {
                WordModel myWord = new WordModel(wordId, word, type, definition, example);
                DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
                if (wordId==-1) {
                    boolean addBool = databaseHelper.addOne(myWord);
                    if (addBool) {
                        Toast.makeText(getContext(), getString(R.string.AddWordSuccess), Toast.LENGTH_SHORT).show();
                        getFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).
                                replace(R.id.fragment_container, new AddFragment()).addToBackStack(null).commit();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.AddWordFail), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    boolean addBool = databaseHelper.editOne(myWord);
                    if (addBool) {
                        Toast.makeText(getContext(), getString(R.string.EditWordSuccess), Toast.LENGTH_SHORT).show();
                        //getFragmentManager().beginTransaction().replace(R.id.fragment_container, new AddFragment()).commit();
                    } else {
                        Toast.makeText(getContext(),getString(R.string.EditWordFailed), Toast.LENGTH_SHORT).show();
                    }

                    // new fragmewnt
                    getFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out).
                            replace(R.id.fragment_container, DisplayWordFragment.newInstance(wordId,word, type, example, definition)).addToBackStack(null).commit();

                }

            } else {
                Toast.makeText(getContext(), getString(R.string.AddWordEmptyBoxesError), Toast.LENGTH_SHORT).show();
            }

        });



    }
}

