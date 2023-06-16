package com.project.calowry_app.database.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "food_daily")
data class FoodDailyEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val scanDate: String,
    val foodLabel: String,
    val caloriesValue: String,
    val carbsValue: String,
    val sugarValue: String,
    val proteinValue: String
)