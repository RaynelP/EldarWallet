package com.raynel.eldarwallet.model.db

import android.provider.ContactsContract.CommonDataKinds.Email
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Card(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val email: String,
    val name: String,
    val cardNumber: String,
    val lastThreeNumbers: String,
    val dateExpired: String,
    val company: String
)