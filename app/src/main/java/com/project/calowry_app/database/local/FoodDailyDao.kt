package com.project.calowry_app.database.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.sql.Date

@Dao
interface FoodDailyDao {
    @Insert
    suspend fun insertFood(foodScanEntity: FoodDailyEntity)

    @Query("SELECT * FROM food_daily")
    suspend fun getAllFoodDaily(): List<FoodDailyEntity>

    @Query("SELECT * FROM food_daily WHERE scanDate = :targetDate")
    suspend fun getFoodDailyByDate(targetDate: String): List<FoodDailyEntity>

    @Query("SELECT * FROM food_daily ORDER BY id DESC LIMIT 1")
    suspend fun getLastFoodDaily(): FoodDailyEntity?

    @Query("SELECT COALESCE(SUM(caloriesValue), 0) FROM food_daily WHERE scanDate = :targetDate")
    suspend fun sumCaloriesValueByDate(targetDate: String): Float

    @Query("SELECT COALESCE(SUM(carbsValue), 0) FROM food_daily WHERE scanDate = :targetDate")
    suspend fun sumCarbsValueByDate(targetDate: String): Float

    @Query("SELECT COALESCE(SUM(sugarValue), 0) FROM food_daily WHERE scanDate = :targetDate")
    suspend fun sumSugarValueByDate(targetDate: String): Float

    @Query("SELECT COALESCE(SUM(proteinValue), 0) FROM food_daily WHERE scanDate = :targetDate")
    suspend fun sumProteinValueByDate(targetDate: String): Float

    @Query("SELECT COALESCE(caloriesValue, 0) FROM food_daily WHERE id = :foodId")
    suspend fun getCaloriesValueById(foodId: Long): String
}