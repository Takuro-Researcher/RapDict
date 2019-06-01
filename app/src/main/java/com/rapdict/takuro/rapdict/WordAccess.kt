package com.rapdict.takuro.rapdict

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

import java.util.ArrayList


//SQLクエリを使用し、表から検索条件をリストにして返すクラス
class WordAccess {

    fun getWords(database: SQLiteDatabase, min_word: Int, max_word: Int, question: Int): ArrayList<Word> {

        val cursor = database.query(
                "wordtable", // DB名
                arrayOf("word_id", "furigana", "word"), // 取得するカラム名
                "word_len>=? AND word_len<=?", // WHERE句の列名
                arrayOf(Integer.toString(min_word), Integer.toString(max_word)), null, null, // HAVING句の値
                "RANDOM()", // ORDER BY句の値
                Integer.toString(question)
        )// WHERE句の値
        // GROUP BY句の値
        val result = ArrayList<Word>()

        while (cursor.moveToNext()) {
            val word1 = Word()
            val furigana_id = cursor.getColumnIndex("furigana")
            val word_id = cursor.getColumnIndex("word")
            val word_id_id = cursor.getColumnIndex("word_id")
            val furigana = cursor.getString(furigana_id)
            val word = cursor.getString(word_id)
            val id = cursor.getInt(word_id_id)
            word1.word = word
            word1.furigana = furigana
            word1.word_id = id
            result.add(word1)
        }
        return result
    }


    fun getAnswers(database: SQLiteDatabase, min_word: Int, max_word: Int, question: Int): ArrayList<AnswerView> {
        val sql = ("SELECT * FROM " + ANSWER_TABLE_NAME + " INNER JOIN " + WORD_TABLE_NAME + " ON " +
                ANSWER_TABLE_NAME + "." + COLUMN_NAME_QUESTION_ID + " = " + WORD_TABLE_NAME + "." + COLUMN_NAME_WORD_ID
                + " WHERE " + WORD_TABLE_NAME + ".word_len<= " + max_word + " AND " + WORD_TABLE_NAME + ".word_len >=" + min_word)

        val cursor = database.rawQuery(sql, null)
        val result = ArrayList<AnswerView>()

        while (cursor.moveToNext()) {
            val answerView = AnswerView()
            val answer1_id = cursor.getColumnIndex("answer")
            val word_id = cursor.getColumnIndex("word")
            val question_id_id = cursor.getColumnIndex("question_id")
            val answer_id_id = cursor.getColumnIndex("answer_id")
            val word_len_id = cursor.getColumnIndex("word_len")
            val favorite_id = cursor.getColumnIndex("favorite")
            val word = cursor.getString(word_id)
            val question_id = cursor.getInt(question_id_id)
            val answer_id = cursor.getInt(answer_id_id)
            val word_len = cursor.getInt(word_len_id)
            val answer = cursor.getString(answer1_id)
            val favorite= cursor.getInt(favorite_id) > 0
            answerView.setColumn(answer_id, question_id, answer, word, word_len,favorite)
            result.add(answerView)
        }
        return result
    }
    companion object {

        private val WORD_TABLE_NAME = "wordtable"
        private val ANSWER_TABLE_NAME = "answertable"
        private val COLUMN_NAME_QUESTION_ID = "question_id"
        private val COLUMN_NAME_WORD_ID = "word_id"
    }
}
