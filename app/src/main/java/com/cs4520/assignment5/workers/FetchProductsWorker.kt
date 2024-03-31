package com.cs4520.assignment5.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cs4520.assignment5.api.RetrofitInstance
import com.cs4520.assignment5.api.AppDatabaseSingleton
import com.cs4520.assignment5.repository.ProductRepository

class FetchProductsWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val repository = ProductRepository(
            RetrofitInstance.api,
            AppDatabaseSingleton.getDatabase(applicationContext).productDao(),
            applicationContext
        )

        return try {
            repository.getProducts(null) // Fetch and store products
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
