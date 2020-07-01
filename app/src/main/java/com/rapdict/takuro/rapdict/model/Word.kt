package com.rapdict.takuro.rapdict

import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.ArrayList

@Entity
data class Word(
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "furigana") val furigana: String?,
        @ColumnInfo(name = "word") val word: String?,
        @ColumnInfo(name = "length") val length: Int?
)

