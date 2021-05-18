package com.company.app.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Service(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String
)