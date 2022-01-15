// AUTHOR: Tyler Ziggas
// DATE: May 2021
// This fragment is for our recycler view of shows.  It also has item listener that checks if a
// item in the recycler view is clicked, and that selected item determines which item you wish
// to edit or delete.

package edu.umsl.tyler.lists

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.umsl.tyler.databinding.FragmentRecyclerBinding
import edu.umsl.tyler.databinding.FragmentShowListBinding
import edu.umsl.tyler.persistence.show.Show
import edu.umsl.tyler.persistence.show.ShowRepository

class ShowListFragment: Fragment() {
    companion object { // Companion object for starting our instance
        fun newInstance() = ShowListFragment()
    }

    private lateinit var repository: ShowRepository
    private lateinit var viewModel: ShowDataViewModel
    private lateinit var binding: FragmentRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root // Create our view for the fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(ShowDataViewModel::class.java)
        binding.listAll.layoutManager = LinearLayoutManager(activity)
        binding.listAll.adapter = ShowsAdapter() // Create the adapter for our list of everything

        if (!this::repository.isInitialized) { // Initialize repository
            repository = activity?.let { ShowRepository(it) }!!
        }
    }

    inner class ShowsAdapter: RecyclerView.Adapter<ShowsHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShowsHolder { // Our viewholder
            val inflater = LayoutInflater.from(parent.context)
            val binding = FragmentShowListBinding.inflate(inflater, parent, false)
            return ShowsHolder(binding)
        }

        override fun onBindViewHolder(holder: ShowsHolder, position: Int) { // Bind it based on our 3 parameters we show for the show
            holder.bind(viewModel.shows[position])
            holder.itemView.setOnClickListener { // On click listener that deals with which entry is selected for deletion and editing
                viewModel.lastPosition = viewModel.getPosition()
                if (viewModel.lastPosition >= 0 && viewModel.lastPosition != position) { // Check to see whether we should change the color
                    viewModel.lastView.setBackgroundColor(viewModel.oldColor)
                }
                Log.e("Activity", "Clicked on item ${viewModel.shows[position].title}")
                viewModel.lastView = holder.itemView // Store our last view to change it back later
                viewModel.changePosition(position)
                holder.itemView.setBackgroundColor(Color.BLUE)
            }
        }

        override fun getItemCount(): Int = viewModel.shows.size // Getting our item count
    }

    inner class ShowsHolder constructor(private val binding: FragmentShowListBinding): RecyclerView.ViewHolder(binding.root) { // Our holder constructor
        fun bind(show: Show) {
            binding.titleBoxID.text = show.title
            binding.scoreBoxID.text = show.score.toString()
            binding.creatorBoxID.text = show.director
        }
    }
}