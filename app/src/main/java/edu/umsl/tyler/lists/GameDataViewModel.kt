package edu.umsl.tyler.lists

import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModel
import edu.umsl.tyler.persistence.game.Game

class GameDataViewModel: ViewModel() {
    var games = listOf<Game>() // Initialize our view model parts
    private lateinit var selectedGame: Game
    private var selectedPosition: Int = -1
    var lastPosition: Int = -1
    lateinit var lastView: View
    var oldColor: Int = Color.parseColor("#547A97")
    var sortedID: Int = 0

    fun changePosition(position: Int) { // If the position changes
        selectedPosition = position
        if (position >= 0) {
            selectGame(games[position])
        } else {
            selectedGame = Game("",0,"",0,"")
        }
    }

    fun getPosition(): Int {  // If we want to return the position
        return selectedPosition
    }

    private fun selectGame(game: Game) { // Setting the game that was selected
        selectedGame = game
    }

    fun getGame(): Game { // Return our game
        return selectedGame
    }
}