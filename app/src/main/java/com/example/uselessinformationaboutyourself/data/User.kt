package com.example.uselessinformationaboutyourself.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey val id: Int = 0,
    val name: String,
    val birthDate: String,
    val height: Int
)

