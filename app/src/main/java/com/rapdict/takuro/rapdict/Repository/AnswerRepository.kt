package com.rapdict.takuro.rapdict.Repository

import android.content.Context
import com.rapdict.takuro.rapdict.database.Answer
import com.rapdict.takuro.rapdict.database.AnswerDao
import com.rapdict.takuro.rapdict.database.RapDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AnswerRepository(val context: Context) {
    private val answerDao: AnswerDao by lazy { RapDataBase.getInstance(context).answerDao() }
    suspend fun saveAnswer(answers: List<Answer>) {
        withContext(Dispatchers.IO) {
            answers.forEach {
                answerDao.insert(it)
            }
        }
    }

    suspend fun getAnswer(min: Int, max: Int, favoFlag: List<Int>): List<Answer> {
        var data = listOf<Answer>()
        withContext(Dispatchers.IO) {
            data = answerDao.findByLenght(min, max, favoFlag)
        }
        return data
    }

    suspend fun updateAnswer(uid: Int, favoriteInt: Int) {
        withContext(Dispatchers.IO) {
            answerDao.updateByIdsFavorite(uid, favoriteInt)
        }
    }

}