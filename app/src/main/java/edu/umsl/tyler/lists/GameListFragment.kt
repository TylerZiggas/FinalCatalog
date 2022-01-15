// AUTHOR: Tyler Ziggas
// DATE: May 2021
// This fragment is for our recycler view of games.  It also has item listener that checks if a
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
import edu.umsl.tyler.persistence.game.Game
import edu.umsl.tyler.persistence.game.GameRepository
import edu.umsl.tyler.databinding.FragmentGameListBinding
import edu.umsl.tyler.databinding.FragmentRecyclerBinding

class GameListFragment: Fragment() {
    companion object { // Companion object for starting our instance
        fun newInstance() = GameListFragment()
    }

    private lateinit var repository: GameRepository
    private lateinit var viewModel: GameDataViewModel
    private lateinit var binding: FragmentRecyclerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecyclerBinding.inflate(inflater, container, false)
        return binding.root // Create our view for the fragment
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(GameDataViewModel::class.java)
        binding.listAll.layoutManager = LinearLayoutManager(activity)
        binding.listAll.adapter = GamesAdapter() // Create the adapter for our list of everything

        if (!this::repository.isInitialized) { // Initialize repository
            repository = activity?.let { GameRepository(it) }!!
        }
    }

    inner class GamesAdapter: RecyclerView.Adapter<GamesHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesHolder { // Our viewholder
            val inflater = LayoutInflater.from(parent.context)
            val binding = FragmentGameListBinding.inflate(inflater, parent, false)
            return GamesHolder(binding)
        }

        override fun onBindViewHolder(holder: GamesHolder, position: Int) { // Bind it based on our 3 parameters we show for our game
            holder.bind(viewModel.games[position])
            holder.itemView.setOnClickListener { // On click listener that deals with which entry is selected for deletion and editing
                viewModel.lastPosition = viewModel.getPosition()
                if (viewModel.lastPosition >= 0 && viewModel.lastPosition != position) { // Check to see whether we should change the color
                    viewModel.lastView.setBackgroundColor(viewModel.oldColor)
                }
                Log.e("Activity", "Clicked on item ${viewModel.games[position].title}")
                viewModel.lastView = holder.itemView // Store our last view to change it back later
                viewModel.changePosition(position)
                holder.itemView.setBackgroundColor(Color.BLUE)
            }
        }

        override fun getItemCount(): Int = viewModel.games.size // Getting our item count
    }

    inner class GamesHolder constructor(private val binding: FragmentGameListBinding): RecyclerView.ViewHolder(binding.root) {// Our holder constructor
        fun bind(game: Game) {
            binding.titleBoxID.text = game.title
            binding.scoreBoxID.text = game.score.toString()
            binding.creatorBoxID.text = game.developer
        }
    }
}
