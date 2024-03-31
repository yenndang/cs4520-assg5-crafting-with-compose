package com.cs4520.assignment5.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import com.cs4520.assignment5.models.Product
import com.cs4520.assignment5.repository.ProductRepository
import com.cs4520.assignment5.utils.Result
import com.cs4520.assignment5.workers.ProductSyncWorker
import java.util.concurrent.TimeUnit

class ProductViewModel(
    private val application: Application,
    private val repository: ProductRepository
) : ViewModel() {
    val productList = MutableLiveData<Result<List<Product>>>()
    private var currentPage = 1
    var isFetching = false

    fun fetchProducts() {
        if (isFetching) return

        isFetching = true
        viewModelScope.launch {
            when (val result = repository.getProducts(currentPage)) {
                is Result.Success -> {
                    val currentProducts = (productList.value as? Result.Success)?.data ?: emptyList()
                    productList.postValue(Result.Success(currentProducts + result.data))
                    Log.d("ProductViewModel", "Current products size: ${currentProducts.size}")
                    Log.d("ProductViewModel", "New products size: ${result.data.size}")
                    currentPage++
                }
                is Result.Error -> {
                    productList.postValue(result)
                }
                is Result.Empty -> {
                    productList.postValue(Result.Empty)
                }
            }
            isFetching = false
        }
    }

    fun loadMoreProducts() {
        fetchProducts()
    }

    fun setupPeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<ProductSyncWorker>(1, TimeUnit.HOURS)
            .setInitialDelay(1, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(application).enqueueUniquePeriodicWork(
            "product_sync_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWorkRequest
        )

        // Log when the work is scheduled
        Log.d("ProductViewModel", "Product sync work scheduled")

        // Observe the work status and log it
        WorkManager.getInstance(application).getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observeForever { workInfo ->
                Log.d("ProductViewModel", "Current work state: ${workInfo.state}")
            }
    }

}