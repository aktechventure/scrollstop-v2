package com.aswinkumar.scrollstop.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aswinkumar.scrollstop.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPreferenceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(preference: UserPreferenceEntity)

    @Query("SELECT * FROM user_preferences WHERE id = 1")
    fun getPreference(): Flow<UserPreferenceEntity?>

    @Query("SELECT * FROM user_preferences WHERE id = 1")
    suspend fun getPreferenceSync(): UserPreferenceEntity?

    @Query("DELETE FROM user_preferences")
    suspend fun deleteAll()
}
