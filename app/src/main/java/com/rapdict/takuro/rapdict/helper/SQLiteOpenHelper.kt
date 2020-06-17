package com.rapdict.takuro.rapdict.helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.Word
import sample.intent.AnswerData

import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.ArrayList

class SQLiteOpenHelper internal constructor(private val mContext: Context) : android.database.sqlite.SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_ANSWER_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_ANSWER_DELETE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }



    companion object {
        // データーベースのバージョン
        private val DATABASE_VERSION = 31

        // データーベース情報を変数に格納
        private val DATABASE_NAME = "WordDb.db"
        private val ANSWER_TABLE_NAME = "answertable"

        private val COLUMN_NAME_ANSWER = "answer"
        private val COLUMN_NAME_QUESTION = "question"
        private val COLUMN_NAME_ANSWER_ID = "answer_id"
        private val COLUMN_NAME_FAVORITE = "favorite"
        private val COLUMN_NAME_ANSWER_LEN = "answer_len"

        private val SQL_CREATE_ANSWER_TABLE = "CREATE TABLE " + ANSWER_TABLE_NAME + " (" +
                COLUMN_NAME_ANSWER_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME_ANSWER + " TEXT," +
                COLUMN_NAME_ANSWER_LEN + " INTEGER,"+
                COLUMN_NAME_FAVORITE +" INTEGER "+"check(" + COLUMN_NAME_FAVORITE + " IN (0, 1)) ,"+
                COLUMN_NAME_QUESTION + " TEXT)"
        private val SQL_ANSWER_DELETE = "DROP TABLE IF EXISTS $ANSWER_TABLE_NAME"
    }
}

