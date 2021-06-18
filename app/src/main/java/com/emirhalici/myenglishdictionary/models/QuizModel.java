package com.emirhalici.myenglishdictionary.models;

import java.util.ArrayList;

public final class QuizModel {
    private String question, answer;
    private ArrayList<String> options;
    private int id;

    public QuizModel(String question, String answer, ArrayList<String> options, int id) {
        this.question = question;
        this.answer = answer;
        this.options = options;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        //return "QuizModel{" +
        //        "question='" + question + '\'' +
        //        ", answer='" + answer + '\'' +
        //        ", options=" + options +
        //        '}';
        String optionString = "";
        for (String optionstr: options
             ) {
            optionString = optionString + optionstr + "\n\n";
        }
        return question + "\n\n" + answer + "\n\nOptions\n\n" + optionString;
    }
}
