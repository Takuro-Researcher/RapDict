package com.rapdict.takuro.rapdict.model.net

import com.rapdict.takuro.rapdict.model.entity.Word
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("word")
    fun getWords(@Query("min") min: Int,
                 @Query("max") max: Int,
                 @Query("num") num: Int): Call<List<Word>>
}