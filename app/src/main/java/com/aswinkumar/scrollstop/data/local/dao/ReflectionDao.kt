package com.aswinkumar.scrollstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aswinkumar.scrollstop.data.local.entity.ReflectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReflectionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReflection(reflection: ReflectionEntity)

    @Query("SELECT * FROM reflections WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    fun getReflectionsByTimeRange(startTime: Long, endTime: Long): Flow<List<ReflectionEntity>>

    @Query("SELECT * FROM reflections WHERE timestamp BETWEEN :startTime AND :endTime ORDER BY timestamp DESC")
    suspend fun getReflectionsByTimeRangeSync(startTime: Long, endTime: Long): List<ReflectionEntity>

    @Query("SELECT * FROM reflections ORDER BY timestamp DESC LIMIT :limit")
    fun getRecentReflections(limit: Int): Flow<List<ReflectionEntity>>

    @Query("SELECT * FROM reflections WHERE appPackageName = :packageName ORDER BY timestamp DESC")
    fun getReflectionsByPackage(packageName: String): Flow<List<ReflectionEntity>>

    @Query("DELETE FROM reflections")
    suspend fun deleteAll()
}
