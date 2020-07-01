package com.rapdict.takuro.rapdict

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.rapdict.takuro.rapdict.database.Mydict

@Entity(tableName = "word",
        foreignKeys = arrayOf(ForeignKey(entity = Mydict::class,
                parentColumns = arrayOf("uid"),
                childColumns = arrayOf("dictid"),
                onDelete = ForeignKey.CASCADE
        ))
)
data class Word(
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "furigana") val furigana: String?,
        @ColumnInfo(name = "word") val word: String?,
        @ColumnInfo(name = "length") val length: Int?,
        @ColumnInfo(name = "dictid") val dictid: Int?
)

