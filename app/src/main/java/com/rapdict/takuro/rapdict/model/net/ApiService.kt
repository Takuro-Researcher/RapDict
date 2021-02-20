package com.rapdict.takuro.rapdict.model.net

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("word")
    fun getWords(@Query("min") min: Int,
                 @Query("max") max: Int,
                 @Query("number") num: Int): Call<ApiData>
}

data class WordsData(var furigana: String, var length: Int, var word: String)
data class ApiData(var result: String, var words: List<WordsData>)