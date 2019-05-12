package com.rapdict.takuro.rapdict;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class WordAccess {
    public ArrayList<Word> getWords(SQLiteDatabase database, int min_word, int max_word, int question){

        Cursor cursor = database.query(
                "wordtable", // DB名
                new String[] {"_id", "furigana", "word" }, // 取得するカラム名
                "word_len>=? AND word_len<=?", // WHERE句の列名
                new String[]{Integer.toString(min_word),Integer.toString(max_word)}, // WHERE句の値
                null, // GROUP BY句の値
                null, // HAVING句の値
                "RANDOM()", // ORDER BY句の値
                Integer.toString(question)
        );
        ArrayList<Word> result = new ArrayList<Word>();

        while (cursor.moveToNext()){
            Word word1=new Word();
            int furigana_id=cursor.getColumnIndex("furigana");
            int word_id=cursor.getColumnIndex("word");
            int _id_id=cursor.getColumnIndex("_id");
            String furigana=cursor.getString(furigana_id);
            String word=cursor.getString(word_id);
            int id =cursor.getInt(_id_id);
            word1.setWord(word);
            word1.setFurigana(furigana);
            word1.setWord_id(id);
            result.add(word1);
        }
        return result;
    }
}
