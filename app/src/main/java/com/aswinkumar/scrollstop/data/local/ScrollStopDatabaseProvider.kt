package com.aswinkumar.scrollstop.data.local

import android.content.Context
import androidx.room.Room

object ScrollStopDatabaseProvider {
    @Volatile
    private var instance: ScrollStopDatabase? = null

    fun get(context: Context): ScrollStopDatabase =
        instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                ScrollStopDatabase::class.java,
                "scrollstop.db"
            ).fallbackToDestructiveMigration().build().also { instance = it }
        }
}
