package com.emirhalici.myenglishdictionary.utils;

import android.content.Context;
import android.util.Log;

import com.emirhalici.myenglishdictionary.models.QuizModel;
import com.emirhalici.myenglishdictionary.models.WordModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QuizHelper {


    public ArrayList<QuizModel> getQuizModelList(boolean isAnswerDefinition, Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        // <==== to do ====>
        // customize words list as pleased by filtering etc.

        ArrayList<WordModel> allWords = databaseHelper.getEveryWord();
        ArrayList<WordModel> allWordsShuffled = databaseHelper.getEveryWord();
        Collections.shuffle(allWordsShuffled);
        ArrayList<QuizModel> returnQuizList = new ArrayList<>();
        Random random = new Random();

        // create new quizmodels, options selected randomly
        // each option must be different
        // set option size
        // leaving it constant for now

        int optionSize = 4;
        if (allWordsShuffled.size()<=optionSize+1) {
            return returnQuizList;
        }
        for (WordModel mainWord: allWordsShuffled.subList(0,allWordsShuffled.size()-optionSize-1)
             ) {
            ArrayList<String> options = new ArrayList<>();
            //String question=mainWord.getWord(), answer=mainWord.getDefinition();
            String question = isAnswerDefinition ? mainWord.getWord() : mainWord.getDefinition();
            String answer = isAnswerDefinition ? mainWord.getDefinition() : mainWord.getWord();
            int id = mainWord.getId();
            options.add(answer);
            Log.d("QuizHelper", "MainWord question: " + question + " and MainWord answer: " + answer);
            while (options.size()<optionSize) {
                int randomIndex; WordModel wordModel; String option;
                do {
                    randomIndex = random.nextInt(allWords.size());
                    wordModel = allWords.get(randomIndex);
                    option = isAnswerDefinition ? wordModel.getDefinition() : wordModel.getWord();
                } while (wordModel.getWord()==answer || options.contains(option));
                options.add(option);
            }
            Collections.shuffle(options);
            QuizModel quizModel = new QuizModel(question, answer, options, id);
            returnQuizList.add(quizModel);
        }
        Log.e("QuizHelper", "the size of quizmodel is " + returnQuizList.size());
        return returnQuizList;
    }
}
