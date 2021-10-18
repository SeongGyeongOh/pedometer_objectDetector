package com.example.seminarsample.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.seminarsample.data.entity.StepCountEntity

@Database(entities = [StepCountEntity::class], version = 1, exportSchema = true)
abstract class CommonDatabase: RoomDatabase() {

    abstract fun stepCountDao() : StepCountDao

    companion object {

        @Volatile
        private var INSTANCE: CommonDatabase? = null

        fun getDatabase(context: Context): CommonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CommonDatabase::class.java,
                    "common_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}