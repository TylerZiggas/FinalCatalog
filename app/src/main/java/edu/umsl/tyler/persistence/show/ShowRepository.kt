package edu.umsl.tyler.persistence.show

import android.app.Activity
import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShowRepository(private var ctx: Context) {

    private val db: ShowDatabase

    init { // Initializing our database
        if (ctx is Activity) {
            ctx = ctx.applicationContext
        }
        db = Room.databaseBuilder(ctx, ShowDatabase::class.java, "Shows.sqlite").build()
    }

    fun saveShow(show: Show) { // If we are to save a show, take in a show data type
        val entity = ShowEntity(show.title, show.score, show.director, show.episodes, show.review)
        CoroutineScope(Dispatchers.Default).launch {
            db.showDao().insertShow(entity)
        }
    }

    fun deleteShow(show: Show) { // If we are to delete a show, get rid of it by title
        CoroutineScope(Dispatchers.Default).launch {
            db.showDao().deleteShow(show.title)
        }
    }

    fun existID(show: Show): Boolean { // Check to see if an ID exists
        return runBlocking {
            return@runBlocking db.showDao().existID(show.title)
        }
    }

    fun grabID(show: Show): Int { // Grabbing of an ID if we are sure that it exists
        return runBlocking {
            return@runBlocking db.showDao().grabID(show.title)
        }
    }

    fun updateShow(show: Show, id: Int) { // Updating of a show with the ID and the show itself
        CoroutineScope(Dispatchers.Default).launch {
            db.showDao().updateShow(show.title, show.score, show.director, show.episodes, show.review, id)
        }
    }

    fun fetchShows(): List<Show> { // Getting our entire list of shows by title
        val result = runBlocking {
            return@runBlocking db.showDao().fetchAllShows()
        }

        return result.map {
            Show(it.title, it.score, it.director, it.episodes, it.review)
        }
    }

    fun fetchScores(): List<Show> { // Getting our entire list of shows by score
        val result = runBlocking {
            return@runBlocking db.showDao().fetchScores()
        }

        return result.map {
            Show(it.title, it.score, it.director, it.episodes, it.review)
        }
    }

    fun fetchEpisodes(): List<Show> { // Getting our entire list of shows by length
        val result = runBlocking {
            return@runBlocking db.showDao().fetchEpisodes()
        }

        return result.map {
            Show(it.title, it.score, it.director, it.episodes, it.review)
        }
    }
}