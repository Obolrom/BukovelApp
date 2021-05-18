package com.company.app.repository

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteOpenHelper
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Service::class], version = 1)
abstract class BukovelDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: BukovelDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): BukovelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    BukovelDatabase::class.java,
                    "roomchik")
//                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}