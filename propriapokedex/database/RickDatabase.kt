package com.theoandrade.propriapokedex.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [RickEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RickDatabase : RoomDatabase() {

    abstract fun rickDao(): RickDao

    companion object {

        @Volatile
        private var INSTANCE: RickDatabase? = null

        fun getInstance(context: Context): RickDatabase {

            return INSTANCE ?: synchronized(this) {

                Room.databaseBuilder(
                    context.applicationContext,
                    RickDatabase::class.java,
                    "rick.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }
}