package com.raynel.eldarwallet.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Card::class, User::class], version = 8)
abstract class AppDataBase : RoomDatabase() {

    abstract fun cardDao(): CardsDao
    abstract fun userDao(): UserDao

    companion object{
        private var db: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            if (db == null){
                db = Room.databaseBuilder(
                    context,
                    AppDataBase::class.java,
                    "wallet-db"
                ).fallbackToDestructiveMigration()
                    .build()
            }
            return db!!
        }

    }

}

