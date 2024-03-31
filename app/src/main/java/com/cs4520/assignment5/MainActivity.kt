package com.cs4520.assignment5

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.navigation.compose.rememberNavController
import com.cs4520.assignment5.screens.nav.SetupNavHost
import androidx.work.*
import com.cs4520.assignment5.workers.ProductSyncWorker
import java.util.concurrent.TimeUnit
//import androidx.appcompat.app.AppCompatActivity
//
//class MainActivity : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//    }
//}
//
////fragment container view go inside the activity file.

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstantState: Bundle?) {
//        super.onCreate(savedInstantState)
//        setContent {
//            MaterialTheme {
//                val navController = rememberNavController()
//                SetupNavHost(navController = navController)
//            }
//        }
//
//    }
//}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstantState: Bundle?) {
        super.onCreate(savedInstantState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                SetupNavHost(navController = navController)
            }
        }

        setupPeriodicWork()
    }

    private fun setupPeriodicWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<ProductSyncWorker>(1, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.HOURS)
                .setConstraints(constraints)
                .build()
//        // for testing
//        val periodicWorkRequest =
//            PeriodicWorkRequestBuilder<ProductSyncWorker>(15, TimeUnit.MINUTES) // or 1, TimeUnit.MINUTES
//                .setConstraints(constraints)
//                .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "product_sync_work",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(periodicWorkRequest.id)
            .observe(this) { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> Log.d("MainActivity", "Work ENQUEUED")
                    WorkInfo.State.RUNNING -> Log.d("MainActivity", "Work RUNNING")
                    WorkInfo.State.SUCCEEDED -> Log.d("MainActivity", "Work SUCCEEDED")
                    WorkInfo.State.FAILED -> Log.d("MainActivity", "Work FAILED")
                    WorkInfo.State.BLOCKED -> Log.d("MainActivity", "Work BLOCKED")
                    WorkInfo.State.CANCELLED -> Log.d("MainActivity", "Work CANCELLED")
                    else -> Log.d("MainActivity", "Work state: ${workInfo.state}")
                }
            }

    }
}