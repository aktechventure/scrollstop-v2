package com.aswinkumar.scrollstop.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import com.aswinkumar.scrollstop.data.local.dao.AppUsageDao
import com.aswinkumar.scrollstop.data.local.dao.InsightDao
import com.aswinkumar.scrollstop.data.local.dao.InterventionSessionDao
import com.aswinkumar.scrollstop.data.local.dao.ReflectionDao
import com.aswinkumar.scrollstop.data.local.dao.UserPreferenceDao
import com.aswinkumar.scrollstop.data.local.entity.AppUsageEntity
import com.aswinkumar.scrollstop.data.local.entity.InsightEntity
import com.aswinkumar.scrollstop.data.local.entity.InterventionSessionEntity
import com.aswinkumar.scrollstop.data.local.entity.ReflectionEntity
import com.aswinkumar.scrollstop.data.local.entity.UserPreferenceEntity

@Database(
    entities = [
        AppUsageEntity::class,
        ReflectionEntity::class,
        InsightEntity::class,
        UserPreferenceEntity::class,
        InterventionSessionEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ScrollStopDatabase : RoomDatabase() {

    abstract fun appUsageDao(): AppUsageDao
    abstract fun reflectionDao(): ReflectionDao
    abstract fun insightDao(): InsightDao
    abstract fun userPreferenceDao(): UserPreferenceDao
    abstract fun interventionSessionDao(): InterventionSessionDao

    suspend fun deleteAllUserData() {
        withTransaction {
            appUsageDao().deleteAll()
            reflectionDao().deleteAll()
            insightDao().deleteAll()
            userPreferenceDao().deleteAll()
            interventionSessionDao().deleteAll()
        }
    }
}
