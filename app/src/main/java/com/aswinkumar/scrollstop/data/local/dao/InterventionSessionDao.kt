package com.aswinkumar.scrollstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aswinkumar.scrollstop.data.local.entity.InterventionSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InterventionSessionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: InterventionSessionEntity)

    @Query("SELECT * FROM intervention_sessions ORDER BY startTime DESC LIMIT :limit")
    fun getRecentSessions(limit: Int): Flow<List<InterventionSessionEntity>>

    @Query("SELECT * FROM intervention_sessions WHERE startTime BETWEEN :startTime AND :endTime ORDER BY startTime DESC")
    suspend fun getSessionsInTimeRangeSync(startTime: Long, endTime: Long): List<InterventionSessionEntity>

    @Query("SELECT * FROM intervention_sessions WHERE packageName = :packageName ORDER BY startTime DESC")
    fun getSessionsByPackage(packageName: String): Flow<List<InterventionSessionEntity>>

    @Query("DELETE FROM intervention_sessions")
    suspend fun deleteAll()
}
