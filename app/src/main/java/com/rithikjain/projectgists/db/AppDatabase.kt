package com.rithikjain.projectgists.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rithikjain.projectgists.model.Gist

@Database(entities = [Gist::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}