package com.rapdict.takuro.rapdict.model.repository

import android.content.Context
import com.rapdict.takuro.rapdict.model.RapDataBase
import com.rapdict.takuro.rapdict.model.dao.MydictDao
import com.rapdict.takuro.rapdict.model.entity.Mydict
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyDictRepository(val context: Context){
    private val mydictDao: MydictDao by lazy { RapDataBase.getInstance(context).mydictDao()}
    suspend fun uid2dict(uid :Int): Mydict? {
        var data: Mydict? = null
        withContext(Dispatchers.IO) {
            data = mydictDao.findOneByIds(uid)
        }
        return data
    }
    suspend fun getDictAll(): List<Mydict> {
        var data: List<Mydict> = listOf()
        withContext(Dispatchers.IO){
            data = mydictDao.findAll()
        }
        return data
    }

    suspend fun removeuid2MyDict(uid: Int){
        withContext(Dispatchers.IO){
            mydictDao.deleteByIds(uid)
        }
    }


}