package com.rapdict.takuro.rapdict.Repository
import android.content.Context
import com.rapdict.takuro.rapdict.Word
import com.rapdict.takuro.rapdict.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WordRepository(val context: Context){
    private val wordDao: WordDao by lazy { RapDataBase.getInstance(context).wordDao()}

    suspend fun saveWords(words: List<Word>) {
        withContext(Dispatchers.IO) {
            words.forEach {
                wordDao.insert(it)
            }
        }
    }

    suspend fun getWords(uid:Int):List<Word>{
        var data = listOf<Word>()
        withContext(Dispatchers.IO){
            data = wordDao.findByDictIds(uid)
        }
        return data
    }

    suspend fun removeWords(uid: Int){
        withContext(Dispatchers.IO){
            wordDao.deleteByIds(uid)
        }
    }

    suspend fun countWords(uid: Int):Int{
        var count = 0
        withContext(Dispatchers.IO){
            count = wordDao.countByDictIds(uid)
        }
        return count
    }
}