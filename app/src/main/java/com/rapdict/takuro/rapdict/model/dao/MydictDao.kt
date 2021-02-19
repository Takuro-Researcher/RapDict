package com.rapdict.takuro.rapdict.model.dao



import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.rapdict.takuro.rapdict.model.entity.Mydict

@Dao
interface MydictDao {
    @Query("SELECT * FROM mydict")
    suspend fun findAll(): List<Mydict>

    @Query("SELECT * FROM mydict WHERE uid IN (:mydictIds)")
    suspend fun findByIds(mydictIds: List<Int>): List<Mydict>

    @Query("SELECT * FROM mydict WHERE uid == :mydictId")
    suspend fun findOneByIds(mydictId: Int): Mydict

    @Insert
    suspend fun insert(mydict: Mydict)

    @Query("SELECT COUNT(*) from mydict" )
    suspend fun count():Int

    @Query("DELETE FROM mydict WHERE uid = :mydictIds")
    suspend fun deleteByIds(mydictIds: Int)

    @Delete
    fun delete(mydict: Mydict)
}