package com.rithikjain.projectgists.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rithikjain.projectgists.model.Temp

@Database(entities = [Temp::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}