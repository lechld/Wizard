package at.aau.edu.wizards.ui.gameboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.databinding.FragmentGameboardBinding
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardAdapter


class GameBoardFragment : Fragment() {

    private var binding: FragmentGameboardBinding? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            GameBoardViewModel.Factory()
        )[GameBoardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGameboardBinding.inflate(inflater, container, false)

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
        val adapter = GameBoardAdapter()

        binding.gameboardRecyclerView.adapter = adapter

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards)
        }
    }
}

