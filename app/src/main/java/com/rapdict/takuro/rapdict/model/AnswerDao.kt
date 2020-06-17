package com.rapdict.takuro.rapdict.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnswerDao {
    @Query("SELECT * FROM answer")
    fun findAll(): List<Answer>

    @Query("SELECT * FROM answer WHERE uid IN (:userIds)")
    fun findByIds(vararg userIds: Int): List<Answer>

    @Query("SELECT * FROM answer WHERE answer_len BETWEEN :min AND :max AND favorite IN (:favorite)")
    fun findByLenght(max: Int, min: Int, favorite: List<Int>): List<Answer>

    @Insert
    fun insertAll(vararg answer: Answer)
    @Insert
    fun insert(answer: Answer)

    @Delete
    fun delete(answer: Answer)
}