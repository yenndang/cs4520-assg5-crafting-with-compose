package com.cs4520.assignment5.api

import android.content.Context
import androidx.room.Room
import com.cs4520.assignment5.repository.AppDatabase

object AppDatabaseSingleton {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "assignment4_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
