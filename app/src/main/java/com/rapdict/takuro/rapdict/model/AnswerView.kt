package com.rapdict.takuro.rapdict

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import sample.intent.AnswerData

class AnswerView {
    var answerview_id: Int = 0
    var question_id: Int=0
    var answer: String? = null
    var question: String? = null
    var question_len: Int = 0
    var favorite:Boolean ?=false

    fun setColumn(rec_answerview_id:Int,rec_question_id:Int,rec_answer:String,rec_question:String,rec_question_len:Int,rec_favorite:Boolean){
        answerview_id = rec_answerview_id
        question_id = rec_question_id
        answer = rec_answer
        question = rec_question
        question_len = rec_question_len
        favorite = rec_favorite
    }
    fun question_saveData(db: SQLiteDatabase, furigana: String, word: String, word_len: Int) {
        val values = ContentValues()
        values.put("furigana", furigana)
        values.put("word", word)
        values.put("word_len", word_len)
        db.insert("wordtable", null, values)
    }

    fun answer_saveData(db: SQLiteDatabase, answer: AnswerData) {
        val values = ContentValues()
        values.put("question_id", answer.question_id)
        values.put("answer", answer.answer)
        values.put("favorite",answer.favorite)
        db.insert("answertable", null, values)
    }

    fun answer_update_fav(db: SQLiteDatabase, answer_id:Int, favorite:Boolean){
        val values = ContentValues()
        val intFavorite:Int= if(favorite) 1 else 0
        val sql ="answer_id = "+answer_id
        values.put("favorite",intFavorite)
        db.update("answertable", values, sql,null)
    }

    fun answer_delete(db: SQLiteDatabase, deleAnswerId:Int){
        val query = "$COLUMN_NAME_ANSWER_ID=$deleAnswerId"
        db.delete("answertable",query,null)
    }

    fun getLengthMinMax(database: SQLiteDatabase):ArrayList<Int>{
        val sql = ("SELECT DISTINCT "+ COLUMN_NAME_WORD_LEN + " FROM " + ANSWER_TABLE_NAME + " INNER JOIN " + WORD_TABLE_NAME + " ON " +
                ANSWER_TABLE_NAME + "." + COLUMN_NAME_QUESTION_ID + " = " + WORD_TABLE_NAME + "." + COLUMN_NAME_WORD_ID)
        val cursor = database.rawQuery(sql, null)
        val result = ArrayList<Int>()
        while(cursor.moveToNext()){
            val word_len =cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_WORD_LEN))
            result.add(word_len)
        }
        cursor.close()

        return result
    }
    fun getAnswers(database: SQLiteDatabase, min_word: Int, max_word: Int,favorite: Int): ArrayList<AnswerView> {

        var favoriteSql = if(favorite==1) " AND $ANSWER_TABLE_NAME.favorite=1" else " AND $ANSWER_TABLE_NAME.favorite=0"
        if(favorite ==2) favoriteSql = ""

        val sql = ("SELECT * FROM " + ANSWER_TABLE_NAME + " INNER JOIN " + WORD_TABLE_NAME + " ON " +
                ANSWER_TABLE_NAME + "." + COLUMN_NAME_QUESTION_ID + " = " + WORD_TABLE_NAME + "." + COLUMN_NAME_WORD_ID
                + " WHERE " + WORD_TABLE_NAME + ".word_len<= " + max_word + " AND " + WORD_TABLE_NAME + ".word_len >=" + min_word
                + favoriteSql)

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
            val intFavorite= cursor.getInt(favorite_id) > 0
            answerView.setColumn(answer_id, question_id, answer, word, word_len,intFavorite)
            result.add(answerView)
        }
        cursor.close()

        return result
    }

    companion object {
        private const val WORD_TABLE_NAME = "wordtable"
        private const val ANSWER_TABLE_NAME = "answertable"
        private const val COLUMN_NAME_QUESTION_ID = "question_id"
        private const val COLUMN_NAME_WORD_ID = "word_id"
        private const val COLUMN_NAME_WORD_LEN = "word_len"
        private val COLUMN_NAME_ANSWER_ID = "answer_id"
        // SQL用にフラグを数値に変更するプログラム
        fun getSearchFav(id:Int):Int{
            if (id == R.id.withoutFav){
                return 0
            }else if(id == R.id.onlyFav){
                return 1
            }
            return 2
        }
        fun favo2background(favorite:Boolean):Int{
            return if (favorite){
                Color.YELLOW
            }else{
                Color.WHITE
            }
        }

    }
}
