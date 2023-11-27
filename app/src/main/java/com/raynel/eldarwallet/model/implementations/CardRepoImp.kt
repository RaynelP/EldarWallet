package com.raynel.eldarwallet.model.implementations

import com.raynel.eldarwallet.model.db.entities.Card
import com.raynel.eldarwallet.model.db.daos.CardsDao
import com.raynel.eldarwallet.model.interfaces.CardsRepo
import kotlinx.coroutines.flow.Flow

class CardRepoImp(private val cardsDao: CardsDao): CardsRepo {

    override fun allCards(email: String): Flow<List<Card>?> = cardsDao.getAll(email)

    override suspend fun saveNewCard(email: String, card: Card) = cardsDao.insert(card)
}