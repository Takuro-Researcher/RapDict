package com.rapdict.takuro.rapdict

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.BufferedReader

class KeepingMotivationOpenHelper internal constructor(private val mContext: Context) : android.database.sqlite.SQLiteOpenHelper(mContext, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_HITOKOTO_TABLE)
        db.execSQL(SQL_CREATE_SHOUGOU_TABLE)

        //CSVファイルを使用
        val assetManager = mContext.resources.assets
        var br: BufferedReader?=null
        //クラス配列
        val hitokotoList = ArrayList<Hitokoto>()
        val shougouList = ArrayList<Shougou>()


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

    }

    companion object {
        // データーベースのバージョン
        private val DATABASE_VERSION = 0

        // データーベース情報を変数に格納
        private val DATABASE_NAME = "WordDb.db"

        //称号テーブルカラム
        private val SHOUGOU_TABLE_NAME = "shougoutable"
        private val COLUMN_NAME_SHOUGOU_ID ="shougou_id"
        private val COLUMN_NAME_SHOUGOU ="shougou"
        private val COLUMN_NAME_ACHIEVE_EXP ="achieve_exp"

        //一言テーブルカラム
        private val HITOKOTO_TABLE_NAME = "hitokototable"
        private val COLUMN_NAME_HITOKOTO_ID = "hitokoto_id"
        private val COLUMN_NAME_HITOKOTO = "hitokoto"
        private val COLUMN_NAME_TO_SHOUGOU = "to_shougou"
        //スキーマ定義
        private val SQL_CREATE_SHOUGOU_TABLE = "CREATE TABLE " + SHOUGOU_TABLE_NAME + " (" +
                COLUMN_NAME_SHOUGOU_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME_SHOUGOU + " TEXT," +
                COLUMN_NAME_ACHIEVE_EXP + " INTEGER)"
        private val SQL_CREATE_HITOKOTO_TABLE = "CREATE TABLE " + HITOKOTO_TABLE_NAME + " (" +
                COLUMN_NAME_HITOKOTO_ID + " INTEGER PRIMARY KEY autoincrement," +
                COLUMN_NAME_HITOKOTO + " TEXT," +
                COLUMN_NAME_TO_SHOUGOU + " INTEGER)"

        private val SQL_HITOKOTO_DELETE = "DROP TABLE IF EXISTS $SHOUGOU_TABLE_NAME"
        private val SQL_SHOUGOU_DELETE = "DROP TABLE IF EXISTS $HITOKOTO_TABLE_NAME"
    }
}