package edu.umsl.tyler.persistence.show

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ShowEntity::class], version = 1)
abstract class ShowDatabase: RoomDatabase() {
    abstract fun showDao(): ShowDao
}