// AUTHOR: Tyler Ziggas
// DATE: May 2021
// This activity is for adding and editing of games.  It utilizes a database of a list of games
// while also having an animation for a custom drawable of a console going from off, to idle,
// to starting up, to finally being completely on.
// It is required for you to have a title and score for it, if you input a score it must be
// between 0-100 and the length must be between 0-100000

package edu.umsl.tyler

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import edu.umsl.tyler.databinding.ActivityAddGameBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.umsl.tyler.lists.GameListActivity
import edu.umsl.tyler.persistence.game.GameRepository
import kotlinx.coroutines.runBlocking

class AddGameActivity: AppCompatActivity() {
    lateinit var binding: ActivityAddGameBinding
    private lateinit var viewModel: AddingViewModel
    private lateinit var repository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddingViewModel::class.java)
        binding = ActivityAddGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editCheck = intent.getSerializableExtra("editCheck") as Boolean
        if (editCheck) { // If we are here to edit, fill in the textboxes
            viewModel.editID = intent.getSerializableExtra("editID") as Int
            val oldTitle = intent.getSerializableExtra("editTitle") as String
            val oldScore = intent.getSerializableExtra("editScore") as Int
            val oldCreator = intent.getSerializableExtra("editCreator") as String
            val oldLength = intent.getSerializableExtra("editLength") as Int
            val oldReview = intent.getSerializableExtra("editReview") as String
            binding.titleInputID.setText(oldTitle) // Set all text
            binding.scoreInputBox.setText(oldScore.toString())
            binding.developerInputBox.setText(oldCreator)
            binding.hoursInputBox.setText(oldLength.toString())
            binding.ReviewInputBox.setText(oldReview)
            binding.addGameButton.text = "Finish Edit" // Change button text
        }

        viewModel.animation = binding.consoleView.background as AnimationDrawable
        viewModel.animation.start() // Start our animation for the console view

        if (!this::repository.isInitialized) { // Initialize repository
            repository = GameRepository(this)
        }

        binding.scoreInputBox.setOnFocusChangeListener { _: View, _: Boolean -> // In case of score change, want to make sure it is a valid input
            val newInput: String = binding.scoreInputBox.text.toString()
            if (newInput.isNotEmpty()) {
                val currentInput: Int = newInput.toInt()
                if (currentInput in 0..100) {
                    binding.scoreInputBox.setText(currentInput.toString())
                } else {
                    binding.scoreInputBox.setText("0") // Reset to 0 if out of bounds
                }
            }
        }

        binding.hoursInputBox.setOnFocusChangeListener { _: View, _: Boolean -> // In case of length change, want to make sure it is a valid input
            val newInput: String = binding.hoursInputBox.text.toString()
            if (newInput.isNotEmpty()) {
                val currentInput: Int = newInput.toInt()
                if (currentInput in 0..100000) {
                    binding.hoursInputBox.setText(currentInput.toString())
                } else {
                    binding.hoursInputBox.setText("0") // Reset to 0 if out of bounds
                }
            }
        }

        binding.addGameButton.setOnClickListener {
            val tempTitle: String = binding.titleInputID.text.toString().trim() // Grab all of our parameters
            val tempScore: String = binding.scoreInputBox.text.toString().trim()
            val tempDeveloper: String = binding.developerInputBox.text.toString().trim()
            var tempHours: String = binding.hoursInputBox.text.toString().trim()
            val tempReview: String = binding.ReviewInputBox.text.toString().trim()

            if (tempTitle.isNotEmpty() && tempScore.isNotEmpty()) { // Title and score are necessary, we also need to hours something before casting it if empty
                if (tempHours.isEmpty()) {
                    tempHours = "0"
                }

                val newTempScore: Int = tempScore.toInt() // Cast the ints
                val newTempHours: Int = tempHours.toInt()

                viewModel.addNewGame(tempTitle, newTempScore, tempDeveloper, newTempHours, tempReview) // add the game

                val idExists = viewModel.getGameInfo()?.let { it1 -> repository.existID(it1) } // See if that ID exists

                if (idExists == false && !editCheck) { // Make sure we are not here to edit and the id exists
                    runBlocking { // Inserting our game data
                        val gameData = (viewModel.getGameInfo())
                        this.let {
                            if (gameData != null) {
                                repository.saveGame(gameData)
                            }
                        }
                    }
                } else { // If we are here to update a row
                    val id: Int
                    id = if (!editCheck) { // Either grab the ID or get it from what it use to be
                        viewModel.getGameInfo()?.let { it1 -> repository.grabID(it1) }!!
                    } else {
                        viewModel.editID
                    }

                    runBlocking { // Inserting our game data
                        val gameData = (viewModel.getGameInfo())
                        this.let {
                            if (gameData != null) {
                                repository.updateGame(gameData, id) // update our game
                            }
                        }
                    }
                }

                val intent = Intent(this, GameListActivity::class.java)
                startActivity(intent) // start activity
                finish()
            }
        }

        binding.ReturnButton.setOnClickListener {
            finish() // Simple return button to just finish the activity
        }
    }

}