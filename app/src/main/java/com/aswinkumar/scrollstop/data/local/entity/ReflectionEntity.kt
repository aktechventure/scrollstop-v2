package com.aswinkumar.scrollstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reflections")
data class ReflectionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: String,
    val timestamp: Long,
    val triggerType: String,
    val appPackageName: String,
    val answerId: String,
    val signalType: String,
    val dismissed: Boolean
)
