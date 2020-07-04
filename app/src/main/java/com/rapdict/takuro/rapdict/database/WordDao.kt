package com.rapdict.takuro.rapdict.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rapdict.takuro.rapdict.Word

@Dao
interface WordDao {
    @Query("SELECT * FROM word")
    suspend fun findAll(): List<Word>

    @Query("SELECT * FROM word WHERE uid IN (:wordIds)")
    suspend fun findByIds(vararg wordIds: Int): List<Word>

    @Query("SELECT * FROM word WHERE dictid == (:dictIds)")
    suspend fun findByDictIds(vararg dictIds: Int): List<Word>

    @Query("SELECT MAX(length) FROM word WHERE dictid == (:dictIds)")
    suspend fun findByDictIdsMax(vararg dictIds: Int): Int

    @Query("SELECT MIN(length) FROM word WHERE dictid == (:dictIds)")
    suspend fun findByDictIdsMin(vararg dictIds: Int): Int


    @Query("SELECT COUNT(*) from word WHERE dictid ==(:dictIds) " )
    suspend fun countByDictIds(vararg dictIds: Int):Int

    @Query("SELECT dictid,COUNT(dictid) AS dict_count from word GROUP BY dictid HAVING 10<= dict_count" )
    suspend fun countByDict():List<WordDictByCount>

    @Query("SELECT * FROM word WHERE length BETWEEN :min AND :max ")
    suspend fun findByLenght(min: Int, max: Int): List<Word>

    @Insert
    suspend fun insertAll(vararg word: Word)
    @Insert
    suspend fun insert(word: Word)

    @Query("DELETE FROM word WHERE uid = :wordIds")
    suspend fun deleteByIds(wordIds: Int)

    @Delete
    fun delete(word: Word)
}