package edu.umsl.tyler.persistence.game

import androidx.room.*

@Dao
interface GameDao {
    @Query("SELECT * FROM gameList ORDER BY title COLLATE NOCASE ASC")
    suspend fun fetchAllGames(): List<GameEntity>

    @Query("SELECT * FROM gameList ORDER BY score DESC")
    suspend fun fetchAllScores(): List<GameEntity>

    @Query("SELECT * FROM gameList ORDER BY hoursPlayed DESC")
    suspend fun fetchAllHours(): List<GameEntity>

    @Query("Delete FROM gameList WHERE title = :delTitle")
    suspend fun deleteGame(delTitle: String)

    @Query("SELECT EXISTS (SELECT id FROM gameList WHERE title = :updatingTitle)")
    suspend fun existID(updatingTitle: String): Boolean

    @Query("SELECT id FROM gameList WHERE title = :updatingTitle")
    suspend fun grabID(updatingTitle: String): Int

    @Query("SELECT * FROM gameList WHERE id = :theID")
    suspend fun grabGame(theID: Int): GameEntity

    @Query("UPDATE gameList SET title =:newTitle, score =:newScore, developer =:newDeveloper, hoursPlayed =:newHours, review =:newReview WHERE id = :theID")
    suspend fun updateGame(newTitle: String, newScore: Int, newDeveloper: String, newHours: Int, newReview: String, theID: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(gameEntity: GameEntity)
}