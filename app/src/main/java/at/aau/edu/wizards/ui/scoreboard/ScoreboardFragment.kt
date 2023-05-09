package at.aau.edu.wizards.ui.scoreboard

import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.FragmentScoreboardBinding
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.ui.scoreboard.recycler.ScoreboardAdapter


class ScoreboardFragment(val listener: GameModelListener) : Fragment() {


    private var binding: FragmentScoreboardBinding? = null

    private val viewModel by lazy {
        val factory = ScoreboardViewModel.Factory(
            listener
        )
        ViewModelProvider(this, factory)[ScoreboardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentScoreboardBinding.inflate(inflater, container, false)

        this.binding = binding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupUI() {
        val binding = this.binding ?: return

        val adapter = ScoreboardAdapter()

        binding.scoreboardRecyclerView.adapter = adapter

        viewModel.score.observe(viewLifecycleOwner) { score ->
            adapter.submitList(score)
        }

        binding.btnMainmenu.setOnClickListener {
            val mainActivity = activity as? MainActivity
            if(listener.getRound() <= 10){
                mainActivity?.scoreboardBack(false)
            } else {
                mainActivity?.scoreboardBack(true)
            }
        }
    }
}

