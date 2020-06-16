package com.rapdict.takuro.rapdict.helper

import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import com.rapdict.takuro.rapdict.AnswerView
import com.rapdict.takuro.rapdict.Word

import java.util.ArrayList


//SQLクエリを使用し、表から検索条件をリストにして返すクラス
class WordAccess {

    fun getCount(database: SQLiteDatabase):Long{
        val recodeCount =DatabaseUtils.queryNumEntries(database, ANSWER_TABLE_NAME)
        return recodeCount
    }

    companion object {
        private const val ANSWER_TABLE_NAME = "answertable"
    }
}
