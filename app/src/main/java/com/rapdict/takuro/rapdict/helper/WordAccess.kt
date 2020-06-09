package com.rapdict.takuro.rapdict.helper

import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.Word

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

            val furigana_id = cursor.getColumnIndex("furigana")
            val word_id = cursor.getColumnIndex("word")
            val word_id_id = cursor.getColumnIndex("word_id")

            val furigana = cursor.getString(furigana_id)
            val word_text = cursor.getString(word_id)
            val id = cursor.getInt(word_id_id)

            var word = Word(id,furigana,word_text,word_text.length)
            result.add(word)
        }
        return result
    }
    private fun favoInt2query(favorite: Int):String{
        if(favorite==1){
            return " AND $ANSWER_TABLE_NAME.favorite=1"
        }
        if (favorite==0) {
            return " AND $ANSWER_TABLE_NAME .favorite=0"
        }
        return ""
    }




    fun getCount(database: SQLiteDatabase):Long{
        val recodeCount =DatabaseUtils.queryNumEntries(database, ANSWER_TABLE_NAME)
        return recodeCount
    }


    companion object {

        private const val WORD_TABLE_NAME = "wordtable"
        private const val ANSWER_TABLE_NAME = "answertable"
        private const val COLUMN_NAME_QUESTION_ID = "question_id"
        private const val COLUMN_NAME_WORD_ID = "word_id"
        private const val COLUMN_NAME_WORD_LEN = "word_len"
    }
}
