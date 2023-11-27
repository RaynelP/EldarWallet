package com.raynel.eldarwallet.model.interfaces

import com.raynel.eldarwallet.model.db.entities.Card
import kotlinx.coroutines.flow.Flow

interface CardsRepo {
    fun allCards(email: String): Flow<List<Card>?>
    suspend fun saveNewCard(email: String, card: Card)
}