package edu.umsl.tyler.persistence.show

import androidx.room.*

@Dao
interface ShowDao {
    @Query("SELECT * FROM showList ORDER BY title COLLATE NOCASE ASC")
    suspend fun fetchAllShows(): List<ShowEntity>

    @Query("SELECT * FROM showList ORDER BY score DESC")
    suspend fun fetchScores(): List<ShowEntity>

    @Query("SELECT * FROM showList ORDER BY episodes DESC")
    suspend fun fetchEpisodes(): List<ShowEntity>

    @Query("Delete FROM showList WHERE title = :delTitle")
    suspend fun deleteShow(delTitle: String)

    @Query("SELECT EXISTS (SELECT id FROM showList WHERE title = :updatingTitle)")
    suspend fun existID(updatingTitle: String): Boolean

    @Query("SELECT id FROM showList WHERE title = :updatingTitle")
    suspend fun grabID(updatingTitle: String): Int

    @Query("SELECT * FROM showList WHERE id = :theID")
    suspend fun grabShow(theID: Int): ShowEntity

    @Query("UPDATE showList SET title =:newTitle, score =:newScore, director =:newDirector, episodes =:newEps, review =:newReview WHERE id = :theID")
    suspend fun updateShow(newTitle: String, newScore: Int, newDirector: String, newEps: Int, newReview: String, theID: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShow(showEntity: ShowEntity)
}