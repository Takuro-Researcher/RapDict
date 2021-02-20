package com.rapdict.takuro.rapdict.model.repository

import com.rapdict.takuro.rapdict.model.entity.Word
import com.rapdict.takuro.rapdict.model.net.ApiService
import com.rapdict.takuro.rapdict.model.usecase.WordUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepository {
    private val retrofit = Retrofit.Builder().apply {
        baseUrl("http://118.27.117.79:8080/")
        addConverterFactory(GsonConverterFactory.create())
    }.build()
    private val service = retrofit.create(ApiService::class.java)

    suspend fun getApiWords(min: Int, max: Int, num: Int, paddFlag: Boolean = true): List<Word> {
        var words = listOf<Word>()
        val get = service.getWords(min, max, num)
        withContext(Dispatchers.IO) {
            val response = get.execute()
            response.body()?.let {
                words = it.words.map { it.run { Word(-1, furigana, word, length, -1) } }
                if (paddFlag) {
                    words = WordUseCase().paddingWord(words, num)
                }
            }
        }
        return words
    }


}