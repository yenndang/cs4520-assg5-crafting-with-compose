package com.cs4520.assignment5.api

import com.cs4520.assignment5.models.Product
import com.cs4520.assignment5.Api
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(Api.ENDPOINT)
    suspend fun getProducts(@Query("page") pageNumber: Int?): Response<List<Product>>
}
