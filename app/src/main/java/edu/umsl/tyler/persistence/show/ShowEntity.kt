package edu.umsl.tyler.persistence.show

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "showList")
data class ShowEntity(
    @ColumnInfo val title: String,
    @ColumnInfo val score: Int,
    @ColumnInfo val director: String,
    @ColumnInfo val episodes: Int,
    @ColumnInfo val review: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}