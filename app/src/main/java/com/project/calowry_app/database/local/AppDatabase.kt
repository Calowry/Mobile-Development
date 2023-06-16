package com.project.calowry_app.database.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FoodDailyEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDailyDao(): FoodDailyDao
}