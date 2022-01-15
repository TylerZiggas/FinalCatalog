package edu.umsl.tyler.lists

import android.graphics.Color
import android.view.View
import androidx.lifecycle.ViewModel
import edu.umsl.tyler.persistence.show.Show

class ShowDataViewModel: ViewModel() {
    var shows = listOf<Show>() // Initialize our view model parts
    private lateinit var selectedShow: Show
    private var selectedPosition: Int = -1
    var lastPosition: Int = -1
    lateinit var lastView: View
    var oldColor: Int = Color.parseColor("#547A97")
    var sortedID: Int = 0

    fun changePosition(position: Int) { // If the position changes
        selectedPosition = position
        if (position >= 0) {
            selectShow(shows[position])
        } else {
            selectedShow = Show("",0,"",0,"")
        }
    }

    fun getPosition(): Int { // If we want to return the position
        return selectedPosition
    }

    private fun selectShow(show: Show) { // Setting the show that was selected
        selectedShow = show
    }

    fun getShow(): Show { // Return our show
        return selectedShow
    }

}