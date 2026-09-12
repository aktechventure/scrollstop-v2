package com.aswinkumar.scrollstop.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "insights")
data class InsightEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val message: String,
    val createdAt: Long,
    val evidenceStart: Long,
    val evidenceEnd: Long,
    val status: String // ACTIVE, DISMISSED, RESOLVED
)
