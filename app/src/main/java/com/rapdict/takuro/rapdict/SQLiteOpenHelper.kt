package com.rapdict.takuro.rapdict

import android.content.ContentValues
import android.content.Context
import android.content.res.AssetManager
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import sample.intent.AnswerData

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.util.ArrayList

class SQLiteOpenHelper internal constructor(private val mContext: Context) : android.database.sqlite.SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_QUESTION_TABLE)
        db.execSQL(SQL_CREATE_ANSWER_TABLE)
        //CSVファイルを使用
        val assetManager = mContext.resources.assets
        var br: BufferedReader? = null
        //クラス配列
        val wordList = ArrayList<Word>()
        //CSVファイルから、クラス配列wordlist[]にデータを全て入れる。
        try {
            val inputStream = assetManager.open("wordlist.csv")
            val inputStreamReader = InputStreamReader(inputStream)
            br = BufferedReader(inputStreamReader!!)
            val arr = arrayOf("furigana", "word", "word_len")
            var line: String?

            do {
                line = br.readLine()
                if(line==null)
                    break;

                val data = line.split(",".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                var colno = 0
                val word = Word()
                for (column in arr) {
                    when (colno) {
                        0 -> word.furigana = data[colno]
                        1 -> word.word = data[colno]
                        2 -> word.word_len = Integer.parseInt(data[colno])
                    }
                    colno++
                }
                wordList.add(word)
            }while(true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                br!!.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        //wordList[]から、実際にSQLiteによる登録
        for (i in wordList.indices) {
            question_saveData(db, wordList[i].furigana!!, wordList[i].word!!, wordList[i].word_len!!)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_QUESTION_DELETE)
        db.execSQL(SQL_ANSWER_DELETE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    fun question_saveData(db: SQLiteDatabase, furigana: String, word: String, word_len: Int) {
        val values = ContentValues()
        values.put("furigana", furigana)
        values.put("word", word)
        values.put("word_len", word_len)
        db.insert("wordtable", null, values)
    }

    fun answer_saveData(db: SQLiteDatabase, answer:AnswerData) {
        val values = ContentValues()
        values.put("question_id", answer.question_id)
        values.put("answer", answer.answer)
        values.put("favorite",answer.favorite)
        db.insert("answertable", null, values)
    }

    fun answer_update_fav(db: SQLiteDatabase,answer_id:Int,favorite:Boolean){
        val values = ContentValues()
        val favorite:Int=bool2Int(favorite)
        val sql ="answer_id = "+answer_id
        values.put("favorite",favorite)
        db.update("answertable", values, sql,null)
    }

    fun answer_delete(db: SQLiteDatabase, deleAnswerId:Int){
        val query = "$COLUMN_NAME_ANSWER_ID=$deleAnswerId"
        db.delete("answertable",query,null)

    }
    fun bool2Int(flag:Boolean):Int{
        val flagInt:Int = if(flag) 1 else 0
        return flagInt
    }



    companion object {
        // データーベースのバージョン
        private val DATABASE_VERSION = 10

        // データーベース情報を変数に格納
        private val DATABASE_NAME = "WordDb.db"
        private val QUESTION_TABLE_NAME = "wordtable"
        private val ANSWER_TABLE_NAME = "answertable"
        private val COLUMN_NAME_FURIGANA = "furigana"
        private val COLUMN_NAME_WORD = "word"
        private val COLUMN_NAME_WORD_LEN = "word_len"
        private val COLUMN_NAME_WORD_ID = "word_id"
        private val COLUMN_NAME_QUESTION_ID = "question_id"
        private val COLUMN_NAME_ANSWER_ID = "answer_id"
        private val COLUMN_NAME_ANSWER = "answer"
        private val COLUMN_NAME_FAVORITE = "favorite"
        //スキーマ定義
        private val SQL_CREATE_QUESTION_TABLE = "CREATE TABLE " + QUESTION_TABLE_NAME + " (" +
                COLUMN_NAME_WORD_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME_FURIGANA + " TEXT," +
                COLUMN_NAME_WORD + " TEXT," +
                COLUMN_NAME_WORD_LEN + " INTEGER)"
        private val SQL_CREATE_ANSWER_TABLE = "CREATE TABLE " + ANSWER_TABLE_NAME + " (" +
                COLUMN_NAME_ANSWER_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME_ANSWER + " TEXT," +
                COLUMN_NAME_FAVORITE+" INTEGER "+"check(" + COLUMN_NAME_FAVORITE + " IN (0, 1)) ,"+
                COLUMN_NAME_QUESTION_ID + " INTEGER)"

        private val SQL_QUESTION_DELETE = "DROP TABLE IF EXISTS $QUESTION_TABLE_NAME"
        private val SQL_ANSWER_DELETE = "DROP TABLE IF EXISTS $ANSWER_TABLE_NAME"
    }
}

