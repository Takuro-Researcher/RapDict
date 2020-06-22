package com.rapdict.takuro.rapdict.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnswerDao {
    @Query("SELECT * FROM answer")
    suspend fun findAll(): List<Answer>

    @Query("SELECT * FROM answer WHERE uid IN (:userIds)")
    suspend fun findByIds(vararg userIds: Int): List<Answer>

    @Query("SELECT * FROM answer WHERE answer_len BETWEEN :min AND :max AND favorite IN (:favorite)")
    suspend fun findByLenght(min: Int, max: Int, favorite: List<Int>): List<Answer>

    @Insert
    suspend fun insertAll(vararg answer: Answer)
    @Insert
    suspend fun insert(answer: Answer)

    @Query("DELETE FROM answer WHERE uid = :userIds")
    suspend fun deleteByIds(userIds: Int)

    @Delete
    fun delete(answer: Answer)
}