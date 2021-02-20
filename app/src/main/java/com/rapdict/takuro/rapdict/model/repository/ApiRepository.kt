package com.rapdict.takuro.rapdict.model.repository

import com.rapdict.takuro.rapdict.model.entity.Word
import com.rapdict.takuro.rapdict.model.net.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit

class ApiRepository {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("http://118.27.117.79:8080/")
    }.build()
    private val service = retrofit.create(ApiService::class.java)

    suspend fun getApiWords(min: Int, max: Int, num: Int): List<Word> {
        var words = listOf<Word>()
        val get = service.getWords(min, max, num)
        withContext(Dispatchers.IO) {
            val response = get.execute()
            response.body()?.let { words = it }
        }
        return words
    }
}