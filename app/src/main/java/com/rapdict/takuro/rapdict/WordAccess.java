package com.rapdict.takuro.rapdict;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

//SQLクエリを使用し、表から検索条件をリストにして返すクラス
public class WordAccess {

    private static final String WORD_TABLE_NAME = "wordtable";
    private static final String ANSWER_TABLE_NAME = "answertable";
    private static final String COLUMN_NAME_QUESTION_ID="question_id";
    private static final String COLUMN_NAME_WORD_ID="word_id";

    public ArrayList<Word> getWords(SQLiteDatabase database, int min_word, int max_word, int question){

        Cursor cursor = database.query(
                "wordtable", // DB名
                new String[] {"word_id", "furigana", "word" }, // 取得するカラム名
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
            int word_id_id=cursor.getColumnIndex("word_id");
            String furigana=cursor.getString(furigana_id);
            String word=cursor.getString(word_id);
            int id =cursor.getInt(word_id_id);
            word1.setWord(word);
            word1.setFurigana(furigana);
            word1.setWord_id(id);
            result.add(word1);
        }
        return result;
    }


    public ArrayList<AnswerView> getAnswers(SQLiteDatabase database, int min_word, int max_word, int question){


        String sql = "SELECT * FROM "+ ANSWER_TABLE_NAME +" INNER JOIN "+WORD_TABLE_NAME+" ON "+
                ANSWER_TABLE_NAME+"."+COLUMN_NAME_QUESTION_ID+" = "+WORD_TABLE_NAME+"."+COLUMN_NAME_WORD_ID
                + " WHERE " +WORD_TABLE_NAME+ ".word_len < " +max_word + " AND " +WORD_TABLE_NAME +".word_len > " +min_word;
        System.out.println(sql);

        Cursor cursor = database.rawQuery(sql,null);
        ArrayList<AnswerView> result = new ArrayList<AnswerView>();

        while (cursor.moveToNext()){
            AnswerView answerView = new AnswerView();
            int answer1_id = cursor.getColumnIndex("answer");
            int word_id = cursor.getColumnIndex("word");
            int question_id_id=cursor.getColumnIndex("question_id");
            int answer_id_id=cursor.getColumnIndex("answer_id");
            int word_len_id = cursor.getColumnIndex("word_len");
            String word=cursor.getString(word_id);
            int question_id = cursor.getInt(question_id_id);
            int answer_id = cursor.getInt(answer_id_id);
            int word_len =cursor.getInt(word_len_id);
            String answer = cursor.getString(answer1_id);
            answerView.setColumn(answer_id, question_id, answer, word, word_len);
            result.add(answerView);
        }
        return result;
    }
}
