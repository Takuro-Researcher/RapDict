package com.rapdict.takuro.rapdict

import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList

data class Word(var id:Int ,var furigana: String, var word: String, var length: Int)
