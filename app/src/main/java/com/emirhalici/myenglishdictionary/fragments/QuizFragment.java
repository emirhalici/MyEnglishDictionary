package com.emirhalici.myenglishdictionary.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.emirhalici.myenglishdictionary.utils.QuizHelper;
import com.emirhalici.myenglishdictionary.models.QuizModel;
import com.emirhalici.myenglishdictionary.R;
import com.google.android.material.color.MaterialColors;

import java.util.ArrayList;

public class QuizFragment extends Fragment {

    static void setButtonsClickable(Button btn_id1, Button btn_id2, Button btn_id3, Button btn_id4, boolean isClickable) {
        btn_id1.setClickable(isClickable);
        btn_id2.setClickable(isClickable);
        btn_id3.setClickable(isClickable);
        btn_id4.setClickable(isClickable);
    }

    void setCorrectButtonGreen(Button false_btn, Button btn1, Button btn2, Button btn3, Button btn4, String answer) {
        false_btn.setBackgroundColor(getResources().getColor(R.color.red, getContext().getTheme()));
        //false_btn.setBackgroundColor(MaterialColors.getColor(btn4, R.attr.colorPrimary));
        false_btn.setTextColor(getResources().getColor(R.color.white, getContext().getTheme()));
        //false_btn.setTextColor(MaterialColors.getColor(btn4, R.attr.colorOnPrimary));

        if (answer==btn1.getText()) {
            btn1.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
            btn1.setTextColor(Color.WHITE); }
        else if (answer==btn2.getText()) {
            btn2.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
            btn2.setTextColor(Color.WHITE); }
        else if (answer==btn3.getText()) {
            btn3.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
            btn3.setTextColor(Color.WHITE); }
        else if (answer==btn4.getText()) {
            btn4.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
            btn4.setTextColor(Color.WHITE); }
    }

    public int qCount, correctA, wrongA = 0;
    ArrayList<String> options;
    String answer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setHasOptionsMenu(false);
        View v = inflater.inflate(R.layout.fragment_quiz, container, false);
        TextView textView = v.findViewById(R.id.textView);
        Button btn_next = v.findViewById(R.id.btn_next);
        Button btn_end = v.findViewById(R.id.btn_end);
        Button btn1 = v.findViewById(R.id.btn1);
        Button btn2 = v.findViewById(R.id.btn2);
        Button btn3 = v.findViewById(R.id.btn3);
        Button btn4 = v.findViewById(R.id.btn4);
        CheckBox checkBox = v.findViewById(R.id.checkBox);
        btn1.setVisibility(View.GONE);
        btn2.setVisibility(View.GONE);
        btn3.setVisibility(View.GONE);
        btn4.setVisibility(View.GONE);
        textView.setVisibility(View.GONE);
        ProgressBar progressBar = v.findViewById(R.id.progressBar);
        // initialize quizhelper and get quizmodel list
        QuizHelper quizHelper = new QuizHelper();
        final ArrayList<QuizModel>[] QuizModelList = new ArrayList[]{null};
        ArrayList<Integer> wrongWordList = new ArrayList<Integer>();

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // END OF TEST
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, QuizEndFragment.newInstance(qCount, correctA, wrongA, wrongWordList), "quizEnd").addToBackStack("quizEnd").commit();
                //
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QuizModelList[0].get(qCount -1).getOptions().get(0)== QuizModelList[0].get(qCount -1).getAnswer()) {
                    btn1.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
                    btn1.setTextColor(Color.WHITE);
                    correctA++;
                } else {
                    setCorrectButtonGreen(btn1, btn1, btn2, btn3, btn4, answer);
                    wrongWordList.add(QuizModelList[0].get(qCount-1).getId());
                    wrongA++;

                }
                setButtonsClickable(btn1, btn2, btn3, btn4, false);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QuizModelList[0].get(qCount -1).getOptions().get(1)== QuizModelList[0].get(qCount -1).getAnswer()) {
                    btn2.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
                    btn2.setTextColor(Color.WHITE);
                    correctA++;
                } else {
                    setCorrectButtonGreen(btn2, btn1, btn2, btn3, btn4, answer);
                    wrongWordList.add(QuizModelList[0].get(qCount-1).getId());
                    wrongA++;
                }
                setButtonsClickable(btn1, btn2, btn3, btn4, false);
            }
        });

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QuizModelList[0].get(qCount -1).getOptions().get(2)== QuizModelList[0].get(qCount -1).getAnswer()) {
                    btn3.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
                    btn3.setTextColor(Color.WHITE);
                    correctA++;
                } else {
                    setCorrectButtonGreen(btn3, btn1, btn2, btn3, btn4, answer);
                    wrongWordList.add(QuizModelList[0].get(qCount-1).getId());
                    wrongA++;
                }
                setButtonsClickable(btn1, btn2, btn3, btn4, false);
            }
        });

        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (QuizModelList[0].get(qCount -1).getOptions().get(3)== QuizModelList[0].get(qCount -1).getAnswer()) {
                    btn4.setBackgroundColor(getResources().getColor(R.color.green, getContext().getTheme()));
                    btn4.setTextColor(Color.WHITE);
                    correctA++;
                } else {
                    setCorrectButtonGreen(btn4, btn1, btn2, btn3, btn4, answer);
                    wrongWordList.add(QuizModelList[0].get(qCount-1).getId());
                    wrongA++;
                }
                setButtonsClickable(btn1, btn2, btn3, btn4, false);
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setButtonsClickable(btn1, btn2, btn3, btn4, true);
                btn1.setTextColor(MaterialColors.getColor(btn1, R.attr.colorPrimary));
                btn1.setBackgroundColor(Color.TRANSPARENT);
                btn2.setTextColor(MaterialColors.getColor(btn1, R.attr.colorPrimary));
                btn2.setBackgroundColor(Color.TRANSPARENT);
                btn3.setTextColor(MaterialColors.getColor(btn1, R.attr.colorPrimary));
                btn3.setBackgroundColor(Color.TRANSPARENT);
                btn4.setTextColor(MaterialColors.getColor(btn1, R.attr.colorPrimary));
                btn4.setBackgroundColor(Color.TRANSPARENT);
                if (checkBox.getVisibility()==View.VISIBLE) {
                    boolean isChecked = checkBox.isChecked();
                    QuizModelList[0] = quizHelper.getQuizModelList(isChecked, getContext());
                    progressBar.setMax(QuizModelList[0].size()-1);
                    progressBar.setProgress(qCount);
                }

                btn_end.setVisibility(View.VISIBLE);
                btn1.setVisibility(View.VISIBLE);
                btn2.setVisibility(View.VISIBLE);
                btn3.setVisibility(View.VISIBLE);
                btn4.setVisibility(View.VISIBLE);
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.GONE);
                btn_next.setText(getString(R.string.Next));
                progressBar.setProgress(qCount);
                if (qCount > QuizModelList[0].size()-2) {
                    //c=0;
                    btn_next.setVisibility(View.GONE);
                    //
                    // END OF TEST
                    //
                }
                try {
                    QuizModel object1 = QuizModelList[0].get(qCount);
                    //textView.setText(c + "\n\n" + String.valueOf(object1.toString()));
                    textView.setText(object1.getQuestion());
                    //btn_next.setVisibility(View.GONE);
                    options = object1.getOptions();
                    answer = object1.getAnswer();
                    btn1.setText(options.get(0));
                    btn2.setText(options.get(1));
                    btn3.setText(options.get(2));
                    btn4.setText(options.get(3));
                } catch (java.lang.IndexOutOfBoundsException e) {
                    btn_next.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("Please add more words.");
                    Log.e("QuizFragment", "IndexOutOfBoundsException");
                    btn1.setVisibility(View.GONE);
                    btn2.setVisibility(View.GONE);
                    btn3.setVisibility(View.GONE);
                    btn4.setVisibility(View.GONE);
                    btn_end.setVisibility(View.GONE);
                }

                qCount++;
            }
        });

        return v;
    }




}
