package com.raynel.eldarwallet.model

import com.raynel.eldarwallet.model.db.Card
import com.raynel.eldarwallet.model.db.User
import kotlinx.coroutines.flow.Flow

interface CardsRepo {
    fun allCards(email: String): Flow<List<Card>?>
    suspend fun saveNewCard(email: String, card: Card)
}