package com.cs4520.assignment5.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cs4520.assignment5.api.AppDatabaseSingleton
import com.cs4520.assignment5.api.RetrofitInstance
import com.cs4520.assignment5.repository.ProductRepository
import kotlinx.coroutines.coroutineScope
import android.util.Log

class ProductSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = coroutineScope {
        val database = AppDatabaseSingleton.getDatabase(applicationContext)
        val repository = ProductRepository(RetrofitInstance.api, database.productDao(), applicationContext)

        return@coroutineScope try {
            Log.d("ProductSyncWorker", "Fetching products")
            repository.getProducts(null)
            Log.d("ProductSyncWorker", "Products fetched and stored successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("ProductSyncWorker", "Error fetching products", e)
            Result.failure()
        }
    }
}

