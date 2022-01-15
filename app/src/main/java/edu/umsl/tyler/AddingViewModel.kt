package edu.umsl.tyler

import android.graphics.drawable.AnimationDrawable
import androidx.lifecycle.ViewModel
import edu.umsl.tyler.persistence.game.Game
import edu.umsl.tyler.persistence.show.Show

class AddingViewModel: ViewModel() { // Shared view model for dealing with the animations and our data types
    lateinit var game: Game
    lateinit var show: Show
    lateinit var animation: AnimationDrawable
    var editID = 0

    fun addNewGame(title: String, score: Int, developer: String, hoursPlayed: Int, review: String) { // Adding a new game
        game = Game(title, score, developer, hoursPlayed, review)
    }

    fun getGameInfo(): Game? { // getting a games info
        return game
    }

    fun addNewShow(title: String, score: Int, director: String, episodes: Int, review: String) { // Adding a new show
        show = Show(title, score, director, episodes, review)
    }

    fun getShowInfo(): Show? { // getting a shows info
        return show
    }
}