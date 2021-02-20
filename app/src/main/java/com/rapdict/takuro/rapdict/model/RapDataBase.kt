package com.rapdict.takuro.rapdict.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rapdict.takuro.rapdict.model.dao.AnswerDao
import com.rapdict.takuro.rapdict.model.dao.MydictDao
import com.rapdict.takuro.rapdict.model.dao.WordDao
import com.rapdict.takuro.rapdict.model.entity.Answer
import com.rapdict.takuro.rapdict.model.entity.Mydict
import com.rapdict.takuro.rapdict.model.entity.Word

@Database(entities = arrayOf(Answer::class, Mydict::class, Word::class), version = 6
)
abstract class RapDataBase : RoomDatabase() {
    abstract fun answerDao(): AnswerDao
    abstract fun mydictDao(): MydictDao
    abstract fun wordDao(): WordDao

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