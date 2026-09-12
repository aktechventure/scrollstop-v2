package com.aswinkumar.scrollstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intervention_sessions")
data class InterventionSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val packageName: String,
    val startTime: Long,
    val endTime: Long,
    val triggerType: String,
    val interventionType: String,
    val userAction: String // CONTINUE, LEAVE, DISMISSED
)
