// AUTHOR: Tyler Ziggas
// DATE: May 2021
// This activity is for the radio buttons that let you sort in different ways and also the
// edit and delete buttons; while also holding the fragment recycler view of the list.
// Clicking the different buttons will sort them in the ways they say,
// while editing and deleting specifically work on the item selected in the recycler view
// (the one that is highlighted in blue after it has been clicked).

package edu.umsl.tyler.lists

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.umsl.tyler.AddGameActivity
import edu.umsl.tyler.databinding.ActivityGameBinding
import edu.umsl.tyler.persistence.game.GameRepository
import kotlinx.coroutines.runBlocking

class GameListActivity: AppCompatActivity() {
    private lateinit var viewModel: GameDataViewModel
    private lateinit var repository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameDataViewModel::class.java)
        val binding = ActivityGameBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        if (!this::repository.isInitialized) { // Initialize repository
            repository = GameRepository(this)
            runBlocking {// Grab our games from the database
                this.let {
                    viewModel.games = repository.fetchGames()
                }
            }
        }

        binding.editGame.setOnClickListener { // If edit game was clicked
            if (viewModel.getPosition() >= 0) { // Make sure the position exists
                val intent = Intent(this, AddGameActivity::class.java)
                val editCheck = true
                val editID = repository.grabID(viewModel.getGame())
                Log.e("Activity", "Selected on item ${viewModel.getGame()}, its id: $editID")
                intent.putExtra("editCheck", editCheck) // Put everything we need for filling our edit parameters
                intent.putExtra("editID", editID)
                intent.putExtra("editTitle", viewModel.getGame().title)
                intent.putExtra("editScore", viewModel.getGame().score)
                intent.putExtra("editCreator", viewModel.getGame().developer)
                intent.putExtra("editLength", viewModel.getGame().hoursPlayed)
                intent.putExtra("editReview", viewModel.getGame().review)
                startActivity(intent)
                finish()
            }
        }

        binding.delGame.setOnClickListener {// If we are deleting a game
            if (viewModel.getPosition() >= 0) { // make sure the position exists
                Log.e("Activity", "Deleting on item ${viewModel.getGame()}")
                repository.deleteGame(viewModel.getGame()) // Delete the game
                checkFilter(binding, viewModel.sortedID)
            }
            checkFilter(binding, viewModel.sortedID)
            viewModel.changePosition(-1)
        }

        binding.sortingGroup.setOnCheckedChangeListener { _: RadioGroup, i: Int -> // Check if the sorting group radio buttons change
            viewModel.sortedID = i
            checkFilter(binding, i)
        }

        if (savedInstanceState == null) { // Start fragment
            supportFragmentManager.beginTransaction()
                .replace(binding.gameFragment.id, GameListFragment.newInstance())
                .commitNow()
        }
    }

    private fun checkFilter(binding: ActivityGameBinding, id: Int) { // Checking our filter
        when (id) {
            binding.titleButton.id -> {
                runBlocking { // Grab our games from the database by title
                    this.let {
                        viewModel.games = repository.fetchGames()
                    }
                }
            }
            binding.scoreButton.id -> {
                runBlocking { // Grab our games from the database by score
                    this.let {
                        viewModel.games = repository.fetchScores()
                    }
                }
            }
            binding.lengthButton.id -> {
                runBlocking { // Grab our games from the database by hours played
                    this.let {
                        viewModel.games = repository.fetchHours()
                    }
                }
            }
            else -> {
                runBlocking { // default grab for our games from the database
                    this.let {
                        viewModel.games = repository.fetchGames()
                    }
                }
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.gameFragment.id, GameListFragment.newInstance())
            .commitNow()
    }
}