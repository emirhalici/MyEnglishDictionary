package com.emirhalici.myenglishdictionary.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.Nullable;

import com.emirhalici.myenglishdictionary.models.WordModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(@Nullable Context context) {super(context, "dictionary.db",null, 1);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement =
                "CREATE TABLE dictionary ( ID INTEGER PRIMARY KEY AUTOINCREMENT, WORD TEXT, TYPE TEXT, DEFINITION TEXT, EXAMPLE TEXT)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(WordModel wordModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("WORD", wordModel.getWord());
        cv.put("TYPE", wordModel.getType());
        cv.put("DEFINITION", wordModel.getDefinition());
        cv.put("EXAMPLE", wordModel.getExample());

        long insert = db.insert("dictionary", null, cv);
        if (insert==-1) {
            return false;
        } else {
            return true;
        }
    }

    // delete one word from database. if not found return false, else return true
    public boolean deleteOne(WordModel wordModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM dictionary WHERE ID = " + wordModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        } else {
            return false;
        }
    }


    // edit one word from database.
    // NOT FINISHED - NOT FUNCTIONAL YET, DON'T USE IT
    public boolean editOne(WordModel wordModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "SELECT * FROM dictionary WHERE ID = " + wordModel.getId();
        Cursor cursor = db.rawQuery(queryString, null);
        ContentValues cv = new ContentValues();
        cv.put("WORD", wordModel.getWord());
        cv.put("TYPE", wordModel.getType());
        cv.put("DEFINITION", wordModel.getDefinition());
        cv.put("EXAMPLE", wordModel.getExample());
        int wordId = wordModel.getId();
        int val = db.update("dictionary", cv, "ID=?", new String[]{String.valueOf(wordId)});
        //Log.e("Database Helper", String.valueOf(wordId));
        //WordModel new_word = getOne(wordId);
        //Log.e("Database Helper", new_word.toString());
        if (val>0) {return true;}
        else {return false;}

        //if (cursor.moveToFirst()) {
        //    return true;
        //} else {
        //    return false;
        //}

    }

    public WordModel getOne(int wordId) {

        WordModel wordModel;
        SQLiteDatabase db = this.getReadableDatabase();
        String queryString = "SELECT * FROM dictionary WHERE ID = " + wordId;
        Cursor cursor = db.rawQuery(queryString, null);
        //int w_ID = cursor.getInt(0);
        if (!cursor.moveToFirst())
            cursor.moveToFirst();
        String w_Word = cursor.getString(1);
        String w_Type = cursor.getString(2);
        String w_Text = cursor.getString(3);
        String w_Example = cursor.getString(4);

        wordModel = new WordModel(wordId, w_Word, w_Type,w_Text, w_Example);
        return wordModel;
    }


    public ArrayList<WordModel> getEveryWord() {

        ArrayList<WordModel> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM dictionary";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new customer objects. Put them into the return list.
            do {
                int w_ID = cursor.getInt(0);
                String w_Word = cursor.getString(1);
                String w_Type = cursor.getString(2);
                String w_Text = cursor.getString(3);
                String w_Example = cursor.getString(4);

                WordModel wordModel = new WordModel(w_ID, w_Word, w_Type,w_Text, w_Example);
                returnList.add(wordModel);

            } while (cursor.moveToNext());

        }
        else {
            // failure. do not add anything to the list.
        }

        // close both the cursor and db when done.
        cursor.close();
        db.close();
        return returnList;
    }

    public ArrayList<WordModel> getbyType(ArrayList<String> typeList) {

        ArrayList<WordModel> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM dictionary";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new customer objects. Put them into the return list.
            do {
                // "(W_ID INTEGER PRIMARY KEY AUTOINCREMENT, W_WORD TEXT, W_TYPE TEXT, W_DEFINITION TEXT, W_EXAMPLE TEXT)"
                int w_ID = cursor.getInt(0);
                String w_Word = cursor.getString(1);
                String w_Type = cursor.getString(2);
                String w_Text = cursor.getString(3);
                String w_Example = cursor.getString(4);

                WordModel wordModel = new WordModel(w_ID, w_Word, w_Type,w_Text, w_Example);
                if (typeList.contains(w_Type)) {returnList.add(wordModel);}

            } while (cursor.moveToNext());

        }
        else {
            // failure. do not add anything to the list.
        }

        // close both the cursor and db when done.
        cursor.close();
        db.close();
        return returnList;
    }

    public ArrayList<String> getEveryType() {

        ArrayList<String> returnList = new ArrayList<>();

        // get data from database
        String queryString = "SELECT * FROM dictionary";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            // loop through the cursor (result set) and create new customer objects. Put them into the return list.
            do {
                // "(W_ID INTEGER PRIMARY KEY AUTOINCREMENT, W_WORD TEXT, W_TYPE TEXT, W_DEFINITION TEXT, W_EXAMPLE TEXT)"
                String w_Type = cursor.getString(2);
                if (!returnList.contains(w_Type)) {returnList.add(w_Type);}
            } while (cursor.moveToNext());

        }
        else {
            // failure.
        }
        // close both the cursor and db when done.
        cursor.close();
        db.close();
        return returnList;
    }



    public ArrayList<WordModel> sortByDate() {
        ArrayList<WordModel> wordList = getEveryWord();
        Collections.reverse(wordList);
        return wordList;
    }

    public ArrayList<WordModel> sortByDate(ArrayList<WordModel> wordList) {
        Collections.reverse(wordList);
        return wordList;
    }

    public ArrayList<WordModel> sortByWordName() {
        ArrayList<WordModel> wordList = getEveryWord();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getWord));
        }
        return wordList;
    }

    public ArrayList<WordModel> sortByWordName(ArrayList<WordModel> wordList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getWord));
        }
        return wordList;
    }

    public ArrayList<WordModel> sortByWordNameD() {
        ArrayList<WordModel> wordList = getEveryWord();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getWord));
        }
        Collections.reverse(wordList);
        return wordList;
    }

    public ArrayList<WordModel> sortByWordNameD(ArrayList<WordModel> wordList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getWord));
        }
        Collections.reverse(wordList);
        return wordList;
    }

    public ArrayList<WordModel> sortByWordType() {
        ArrayList<WordModel> wordList = getEveryWord();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getType));
        }
        return wordList;
    }

    public ArrayList<WordModel> sortByWordType(ArrayList<WordModel> wordList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getType));
        }
        return wordList;
    }

    public ArrayList<WordModel> sortByWordTypeD() {
        ArrayList<WordModel> wordList = getEveryWord();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getType));
        }
        Collections.reverse(wordList);
        return wordList;
    }

    public ArrayList<WordModel> sortByWordTypeD(ArrayList<WordModel> wordList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wordList.sort(Comparator.comparing(WordModel::getType));
        }
        Collections.reverse(wordList);
        return wordList;
    }
}
