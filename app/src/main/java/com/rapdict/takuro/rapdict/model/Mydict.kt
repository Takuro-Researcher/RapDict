package com.rapdict.takuro.rapdict.model


import androidx.room.*

@Entity
data class Mydict(
        @PrimaryKey(autoGenerate = true) val uid: Int,
        @ColumnInfo(name = "name") val answer: String?
)