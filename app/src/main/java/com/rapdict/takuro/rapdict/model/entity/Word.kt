package com.rapdict.takuro.rapdict.model.entity

import androidx.room.*

@Entity(tableName = "word",
        indices = arrayOf(Index(value = ["dictid"]), Index(value = ["length"])),
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

