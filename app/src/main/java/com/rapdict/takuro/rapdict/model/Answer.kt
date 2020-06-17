package com.rapdict.takuro.rapdict.model

import androidx.room.*

@Entity
data class Answer(
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "answer") val answer: String?,
        @ColumnInfo(name = "answer_len") val answerLen: Int?,
        @ColumnInfo(name = "question") val question: String?,
        @ColumnInfo(name = "favorite") val favorite:Int
)