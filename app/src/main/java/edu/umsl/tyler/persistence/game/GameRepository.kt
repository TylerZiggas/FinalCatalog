package edu.umsl.tyler.persistence.game

import android.app.Activity
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class GameRepository(private var ctx: Context) {

    private val db: GameDatabase

    init { // Initializing our database
        if (ctx is Activity) {
            ctx = ctx.applicationContext
        }
        db = Room.databaseBuilder(ctx, GameDatabase::class.java, "Games.sqlite").build()
    }

    fun saveGame(game: Game) { // If we are to save a score, take in a game data type
        val entity = GameEntity(game.title, game.score, game.developer, game.hoursPlayed, game.review)
        CoroutineScope(Dispatchers.Default).launch {
            db.gameDao().insertScore(entity)
        }
    }

    fun deleteGame(game: Game) { // If we are to save a score, take in a game data type
        CoroutineScope(Dispatchers.Default).launch {
            db.gameDao().deleteGame(game.title)
        }
    }

    fun existID(game: Game): Boolean { // Check to see whether the ID exists
        return runBlocking {
            return@runBlocking db.gameDao().existID(game.title)
        }
    }

    fun grabID(game: Game): Int {  // Grabbing of an ID if we are sure that it exists
        return runBlocking {
            return@runBlocking db.gameDao().grabID(game.title)
        }
    }

    fun updateGame(game: Game, id: Int) { // Updating of a game with the ID and the game itself
        CoroutineScope(Dispatchers.Default).launch {
            db.gameDao().updateGame(game.title, game.score, game.developer, game.hoursPlayed, game.review, id)
        }
    }

    fun fetchGames(): List<Game> { // Getting our entire list of games by title
        val result = runBlocking {
            return@runBlocking db.gameDao().fetchAllGames()
        }

        return result.map {
            Game(it.title, it.score, it.developer, it.hoursPlayed, it.review)
        }
    }

    fun fetchScores(): List<Game> { // Getting our entire list of games by score
        val result = runBlocking {
            return@runBlocking db.gameDao().fetchAllScores()
        }

        return result.map {
            Game(it.title, it.score, it.developer, it.hoursPlayed, it.review)
        }
    }

    fun fetchHours(): List<Game> { // Getting our entire list of games by length
        val result = runBlocking {
            return@runBlocking db.gameDao().fetchAllHours()
        }

        return result.map {
            Game(it.title, it.score, it.developer, it.hoursPlayed, it.review)
        }
    }

}