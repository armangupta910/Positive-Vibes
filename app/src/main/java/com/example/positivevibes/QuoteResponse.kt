package com.example.positivevibes

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class QuoteResponse(
    val id: Int,
    val quote: String,
    val author: String
)

data class QuotesResponse(
    val quotes: List<QuoteResponse>,
    val total: Int,
    val skip: Int,
    val limit: Int
)


interface ZenQuotesApi {
    @GET("quotes")
    fun getQuotes(): Call<QuotesResponse>
}



object RetrofitClient {
    private const val BASE_URL = "https://dummyjson.com/"

    val instance: ZenQuotesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ZenQuotesApi::class.java)
    }
}


