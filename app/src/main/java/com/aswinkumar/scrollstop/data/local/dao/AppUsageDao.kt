package com.aswinkumar.scrollstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aswinkumar.scrollstop.data.local.entity.AppUsageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppUsageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsage(usage: AppUsageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsages(usages: List<AppUsageEntity>)

    @Query("SELECT * FROM app_usage WHERE date = :todayDate ORDER BY durationMillis DESC")
    fun getTodayUsage(todayDate: String): Flow<List<AppUsageEntity>>

    @Query("SELECT * FROM app_usage WHERE date BETWEEN :startDate AND :endDate ORDER BY timestamp ASC")
    fun getHistoricalUsage(startDate: String, endDate: String): Flow<List<AppUsageEntity>>

    @Query("SELECT * FROM app_usage")
    suspend fun getAllUsageSync(): List<AppUsageEntity>

    @Query("DELETE FROM app_usage")
    suspend fun deleteAll()
}
