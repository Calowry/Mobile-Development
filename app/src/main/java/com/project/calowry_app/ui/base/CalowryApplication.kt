package com.project.calowry_app.ui.base

import android.app.Application
import androidx.room.Room
import com.project.calowry_app.database.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class CalowryApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "food_daily_database")
            .build()
    }

    companion object {
        lateinit var database: AppDatabase
            private set
    }

}