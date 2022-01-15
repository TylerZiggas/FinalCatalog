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
import edu.umsl.tyler.AddShowActivity
import edu.umsl.tyler.databinding.ActivityShowBinding
import edu.umsl.tyler.persistence.show.ShowRepository
import kotlinx.coroutines.runBlocking

class ShowListActivity: AppCompatActivity() {
    private lateinit var viewModel: ShowDataViewModel
    private lateinit var repository: ShowRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShowDataViewModel::class.java)
        val binding = ActivityShowBinding.inflate(this.layoutInflater)
        setContentView(binding.root)

        if (!this::repository.isInitialized) { // Initialize repository
            repository = ShowRepository(this)
            runBlocking {// Grab our shows from the database
                this.let {
                    viewModel.shows = repository.fetchShows()
                }
            }
        }

        binding.editShow.setOnClickListener { // If edit was clicked
            if (viewModel.getPosition() >= 0) { // Check to see if the position exists
                val intent = Intent(this, AddShowActivity::class.java)
                val editCheck = true
                val editID = repository.grabID(viewModel.getShow())
                Log.e("Activity", "Selected on item ${viewModel.getShow()}, its id: $editID")
                intent.putExtra("editCheck", editCheck) // Put everything we need for filling our edit parameters
                intent.putExtra("editID", editID)
                intent.putExtra("editTitle", viewModel.getShow().title)
                intent.putExtra("editScore", viewModel.getShow().score)
                intent.putExtra("editCreator", viewModel.getShow().director)
                intent.putExtra("editLength", viewModel.getShow().episodes)
                intent.putExtra("editReview", viewModel.getShow().review)
                startActivity(intent)
                finish()
            }
        }

        binding.delShow.setOnClickListener { // If we are deleting a show
            if (viewModel.getPosition() >= 0) { // See if the position exists
                Log.e("Activity", "Deleting on item ${viewModel.getShow()}")
                repository.deleteShow(viewModel.getShow()) // Delete the show
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
                .replace(binding.showFragment.id, ShowListFragment.newInstance())
                .commitNow()
        }
    }

    private fun checkFilter(binding: ActivityShowBinding, id: Int) {
        when (id) {
            binding.titleButton.id -> {
                runBlocking { // Grab our games from the database by title
                    this.let {
                        viewModel.shows = repository.fetchShows()
                    }
                }
            }
            binding.scoreButton.id -> {
                runBlocking { // Grab our games from the database by score
                    this.let {
                        viewModel.shows = repository.fetchScores()
                    }
                }
            }
            binding.lengthButton.id -> {
                runBlocking { // Grab our games from the database by episodes
                    this.let {
                        viewModel.shows = repository.fetchEpisodes()
                    }
                }
            }
            else -> {
                runBlocking { // default grab for our shows from the database
                    this.let {
                        viewModel.shows = repository.fetchShows()
                    }
                }
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(binding.showFragment.id, ShowListFragment.newInstance())
            .commitNow()
    }
}