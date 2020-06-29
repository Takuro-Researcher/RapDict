package com.rapdict.takuro.rapdict.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Answer::class,Mydict::class), version = 3)
abstract class RapDataBase : RoomDatabase() {
    abstract fun answerDao(): AnswerDao
    abstract fun mydictDao(): MydictDao

    companion object {
        private const val dbName = "Rap.db"
        private var instance: RapDataBase? = null
        fun getInstance(context: Context): RapDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context, RapDataBase::class.java, dbName)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return requireNotNull(instance)
        }
    }
}