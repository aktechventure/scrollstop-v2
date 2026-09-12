package com.aswinkumar.scrollstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aswinkumar.scrollstop.data.local.entity.InsightEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InsightDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsight(insight: InsightEntity)

    @Query("SELECT * FROM insights ORDER BY createdAt DESC")
    fun getAllInsights(): Flow<List<InsightEntity>>

    @Query("SELECT * FROM insights WHERE status = :status ORDER BY createdAt DESC")
    fun getInsightsByStatus(status: String): Flow<List<InsightEntity>>

    @Query("UPDATE insights SET status = :status WHERE id = :id")
    suspend fun updateInsightStatus(id: Long, status: String)

    @Query("DELETE FROM insights")
    suspend fun deleteAll()
}
