package com.rapdict.takuro.rapdict.database



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MydictDao {
    @Query("SELECT * FROM mydict")
    suspend fun findAll(): List<Mydict>

    @Query("SELECT * FROM mydict WHERE uid IN (:mydictIds)")
    suspend fun findByIds(vararg mydictIds: Int): List<Mydict>

    @Insert
    suspend fun insert(mydict: Mydict)

    @Query("SELECT COUNT(*) from mydict" )
    suspend fun count():Int

    @Query("DELETE FROM mydict WHERE uid = :mydictIds")
    suspend fun deleteByIds(mydictIds: Int)

    @Delete
    fun delete(mydict: Mydict )
}