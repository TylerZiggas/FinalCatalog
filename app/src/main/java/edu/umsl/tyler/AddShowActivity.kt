// AUTHOR: Tyler Ziggas
// DATE: May 2021
// This activity is for adding and editing of shows.  It utilizes a database of a list of shows
// while also having an animation for a custom drawable of a television turning on.
// It is required for you to have a title and score for it, if you input a score it must be
// between 0-100 and the length must be between 0-100000

package edu.umsl.tyler

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import edu.umsl.tyler.databinding.ActivityAddShowBinding
import edu.umsl.tyler.lists.ShowListActivity
import edu.umsl.tyler.persistence.show.ShowRepository
import kotlinx.coroutines.runBlocking


class AddShowActivity: AppCompatActivity() {
    lateinit var binding: ActivityAddShowBinding
    private lateinit var viewModel: AddingViewModel
    private lateinit var repository: ShowRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddingViewModel::class.java)
        binding = ActivityAddShowBinding.inflate(layoutInflater)
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
            binding.directorInputBox.setText(oldCreator)
            binding.episodesInputBox.setText(oldLength.toString())
            binding.ReviewInputBox.setText(oldReview)
            binding.addShowButton.text = "Finish Edit" // Change button text
        }

        viewModel.animation = binding.tvView.background as AnimationDrawable
        viewModel.animation.start()

        if (!this::repository.isInitialized) { // Initialize repository
            repository = ShowRepository(this)
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

        binding.episodesInputBox.setOnFocusChangeListener { _: View, _: Boolean -> // In case of length change, want to make sure it is a valid input
            val newInput: String = binding.episodesInputBox.text.toString()
            if (newInput.isNotEmpty()) {
                val currentInput: Int = newInput.toInt()
                if (currentInput in 0..100000) {
                    binding.episodesInputBox.setText(currentInput.toString())
                } else {
                    binding.episodesInputBox.setText("0") // Reset to 0 if out of bounds
                }
            }
        }

        binding.addShowButton.setOnClickListener {
            val tempTitle: String = binding.titleInputID.text.toString().trim() // Grab all of our parameters
            val tempScore: String = binding.scoreInputBox.text.toString().trim()
            val tempDeveloper: String = binding.directorInputBox.text.toString().trim()
            var tempEpisodes: String = binding.episodesInputBox.text.toString().trim()
            val tempReview: String = binding.ReviewInputBox.text.toString().trim()

            if (tempTitle.isNotEmpty() && tempScore.isNotEmpty()) { // Title and score are necessary, we also need to hours something before casting it if empty
                if (tempEpisodes.isEmpty()) {
                    tempEpisodes ="0"
                }

                val newTempScore: Int = tempScore.toInt() // Cast to Ints
                val newTempHours: Int = tempEpisodes.toInt()

                viewModel.addNewShow(tempTitle, newTempScore, tempDeveloper, newTempHours, tempReview) // add the game

                val idExists = viewModel.getShowInfo()?.let { it1 -> repository.existID(it1) } // See if the ID exists

                if (idExists == false && !editCheck) { // Make sure we are not here to edit and the id exists
                    runBlocking { // Inserting our show data
                        val showData = (viewModel.getShowInfo())
                        this.let {
                            if (showData != null) {
                                repository.saveShow(showData)
                            }
                        }
                    }
                } else { // If we are here to update a row
                    val id: Int
                    id = if (!editCheck) { // Either grab the ID or get it from what it use to be
                        viewModel.getShowInfo()?.let { it1 -> repository.grabID(it1) }!!
                    } else {
                        viewModel.editID
                    }

                    runBlocking { // Inserting our show data
                        val showData = (viewModel.getShowInfo())
                        this.let {
                            if (showData != null) {
                                repository.updateShow(showData, id) // update our show
                            }
                        }
                    }
                }

                val intent = Intent(this, ShowListActivity::class.java)
                startActivity(intent) // start activity
                finish()
            }
        }

        binding.ReturnButton.setOnClickListener {
            finish() // Simple return button to just finish the activity
        }
    }

}