package edu.umsl.tyler.persistence.game

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gameList")
data class GameEntity(
    @ColumnInfo val title: String,
    @ColumnInfo val score: Int,
    @ColumnInfo val developer: String,
    @ColumnInfo val hoursPlayed: Int,
    @ColumnInfo val review: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}