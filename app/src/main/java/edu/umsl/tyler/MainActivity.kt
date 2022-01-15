// AUTHOR: Tyler Ziggas
// DATE: May 2021
// The purpose of this project is to be able to catalog a list of shows and games you have completed
// and rank them or simply store them, you may add, delete, and update your entries.  You may also
// sort the entries in different ways depending on the choice you select.
// This page simply has a drawable of a television with a console in a slot underneath it and four buttons
// that take you to view your games, view you shows, add a game, or add a show.

package edu.umsl.tyler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.umsl.tyler.databinding.ActivityMainBinding
import edu.umsl.tyler.lists.GameListActivity
import edu.umsl.tyler.lists.ShowListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewGames.setOnClickListener { // If we want to view our list of games
            val intent = Intent(this, GameListActivity::class.java)
            startActivity(intent)
        }

        viewShows.setOnClickListener { // If we want to view our list of shows
            val intent = Intent(this, ShowListActivity::class.java)
            startActivity(intent)
        }

        addGame.setOnClickListener {// If we want to add a game
            val intent = Intent(this, AddGameActivity::class.java)
            val editCheck = false // Set a check that says this should be a new entry
            val editID = 0
            intent.putExtra("editCheck", editCheck)
            intent.putExtra("editID", editID)
            startActivity(intent)
        }

        addShow.setOnClickListener {// If we want to add a show
            val intent = Intent(this, AddShowActivity::class.java)
            val editCheck = false // Set a check that says this should be a new entry
            val editID = 0
            intent.putExtra("editCheck", editCheck)
            intent.putExtra("editID", editID)
            startActivity(intent)
        }
    }
}