package com.raynel.eldarwallet.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CardsDao {

    @Query("SELECT * FROM Card where email = :email")
    fun getAll(email: String): Flow<List<Card>?>

    @Insert
    suspend fun insert(card: Card)

    @Delete
    suspend fun delete(card: Card)
}
