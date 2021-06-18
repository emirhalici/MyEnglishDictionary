package com.emirhalici.myenglishdictionary.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class WordModel {

    private int id;
    private String word, type, definition, example;

    public WordModel(int id, String word, String type, String definition, String example) {
        this.id = id;
        this.word = word;
        this.type = type;
        this.definition = definition;
        this.example = example;
    }

    @Override
    public String toString() {
        return "WordModel{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", type='" + type + '\'' +
                ", definition='" + definition + '\'' +
                ", example='" + example + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
