package at.aau.edu.wizards.ui.scoreboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.databinding.FragmentScoreboardBinding
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.ui.scoreboard.recycler.ScoreboardAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// TODO: We should never pass something in constructor! This will crash now on Activity recreation
class ScoreboardFragment(val listener: GameModelListener) : DialogFragment() {

    private var binding: FragmentScoreboardBinding? = null

    private val viewModel by lazy {
        val factory = ScoreboardViewModel.Factory(
            listener
        )
        ViewModelProvider(this, factory)[ScoreboardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        binding.btnCheatingFunction.visibility = View.GONE
        if (viewModel.listener.getRound() > 1 && listener.hasCheated()) {
            binding.btnCheatingFunction.visibility = View.VISIBLE
        }

        binding.btnMainmenu.setOnClickListener {
            val mainActivity = activity as? MainActivity
            if (listener.getRound() <= 10) {
                mainActivity?.scoreboardBack(false)
            } else {
                mainActivity?.scoreboardBack(true)
            }
        }

        binding.btnCheatingFunction.setOnClickListener {
            var selectedOption = 0
            activity?.let {
                MaterialAlertDialogBuilder(it).setTitle("Choose Cheater")
                    .setNeutralButton("Cancel", null)
                    .setSingleChoiceItems(viewModel.listOFPlayer(), selectedOption) { _, which ->
                        selectedOption = which
                    }.setPositiveButton("Ok") { _, _ ->
                        viewModel.foundCheaterCall(selectedOption)
                    }.show()
            }
        }
    }
}


