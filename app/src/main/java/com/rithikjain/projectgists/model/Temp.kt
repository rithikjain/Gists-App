package com.rithikjain.projectgists.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gists")
data class Temp(val fileName: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 1
}