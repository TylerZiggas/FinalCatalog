package edu.umsl.tyler.persistence.game

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GameEntity::class], version = 1)
abstract class GameDatabase: RoomDatabase() {
    abstract fun gameDao(): GameDao
}