package com.raynel.eldarwallet.model.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val email: String,
    val name: String,
    val lastName: String,
    @ColumnInfo(defaultValue = "0.00")
    val amount: String,
    val password: String
)