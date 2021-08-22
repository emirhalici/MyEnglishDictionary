package com.emirhalici.myenglishdictionary.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.emirhalici.myenglishdictionary.utils.DatabaseHelper;
import com.emirhalici.myenglishdictionary.R;
import com.emirhalici.myenglishdictionary.models.WordModel;
import com.emirhalici.myenglishdictionary.adapters.AllWordsAdapter;

import java.util.ArrayList;

public class QuizEndFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String QUESTION_COUNT = "questionCount",
            CORRECT_COUNT = "correctCount", WRONG_COUNT = "wrongCount", WORD_ID_LIST = "wordIdList";

    private int questionCount, correctCount, wrongCount;
    private ArrayList<Integer> wordIdList;

    public QuizEndFragment() {
        // Required empty public constructor
    }


    public static QuizEndFragment newInstance(int questionCount, int correctCount, int wrongCount, ArrayList<Integer> wordIdList) {
        QuizEndFragment fragment = new QuizEndFragment();
        Bundle args = new Bundle();
        args.putInt(QUESTION_COUNT, questionCount);
        args.putInt(CORRECT_COUNT, correctCount);
        args.putInt(WRONG_COUNT, wrongCount);
        args.putIntegerArrayList(WORD_ID_LIST, wordIdList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList(WORD_ID_LIST,wordIdList);
        outState.putInt(QUESTION_COUNT, questionCount);
        outState.putInt(CORRECT_COUNT, correctCount);
        outState.putInt(WRONG_COUNT, wrongCount);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null) {
            wordIdList = savedInstanceState.getIntegerArrayList(WORD_ID_LIST);
            questionCount = savedInstanceState.getInt(QUESTION_COUNT);
            correctCount = savedInstanceState.getInt(CORRECT_COUNT);
            wrongCount = savedInstanceState.getInt(WRONG_COUNT);
        }
        else if (getArguments() != null) {
            wordIdList = getArguments().getIntegerArrayList(WORD_ID_LIST);
            questionCount = getArguments().getInt(QUESTION_COUNT);
            correctCount = getArguments().getInt(CORRECT_COUNT);
            wrongCount = getArguments().getInt(WRONG_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_end, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tv_question = view.findViewById(R.id.tv_question);
        TextView tv_correct = view.findViewById(R.id.tv_correct);
        TextView tv_wrong = view.findViewById(R.id.tv_wrong);
        TextView tv_success = view.findViewById(R.id.tv_success);
        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager mLayoutManager;
        mRecyclerView = view.findViewById(R.id.rv_quizend);
        super.onViewCreated(view, savedInstanceState);

        tv_question.setText(getString(R.string.QuestionCount, String.valueOf(questionCount)));
        tv_correct.setText(getString(R.string.CorrectCount, String.valueOf(correctCount)));
        tv_wrong.setText(getString(R.string.WrongCount, String.valueOf(wrongCount)));
        if (questionCount==0) {questionCount=1;}
        int successRate = correctCount * 100;
        successRate = successRate / questionCount;
        Log.e("Quiz End Fragment", String.valueOf(successRate));
        tv_success.setText(getString(R.string.SuccessRate, String.valueOf(successRate)));

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        ArrayList<WordModel> wordList = new ArrayList<>();

        for (int wordId: wordIdList) {
            WordModel wordModel = databaseHelper.getOne(wordId);
            wordList.add(wordModel);
        }

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AllWordsAdapter(wordList, getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }
}