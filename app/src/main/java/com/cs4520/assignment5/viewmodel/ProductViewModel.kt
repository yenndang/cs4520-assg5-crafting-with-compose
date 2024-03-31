package com.cs4520.assignment5.viewmodel

import android.content.Context
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
import com.cs4520.assignment5.workers.FetchProductsWorker
import java.util.concurrent.TimeUnit

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
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


    fun scheduleProductFetchWorker(context: Context) {
        val workRequest = PeriodicWorkRequestBuilder<FetchProductsWorker>(1, TimeUnit.HOURS)
            // Additional configuration...
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "fetchProducts",
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

}
