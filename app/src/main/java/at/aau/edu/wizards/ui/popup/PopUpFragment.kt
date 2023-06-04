package at.aau.edu.wizards.ui.popup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.databinding.FragmentPopUpBinding
import at.aau.edu.wizards.gameModel.GameModelListener
import at.aau.edu.wizards.ui.popup.recycler.PopUpAdapter


class PopUpFragment(val listener: GameModelListener) : Fragment() {


    private var binding: FragmentPopUpBinding? = null

    private val viewModel by lazy {
        val factory = PopUpViewModel.Factory(
            listener
        )
        ViewModelProvider(this, factory)[PopUpViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPopUpBinding.inflate(inflater, container, false)

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

        val adapter = PopUpAdapter()

        binding.popupRecyclerView.adapter = adapter

        viewModel.winningcard.observe(viewLifecycleOwner) { winningcard ->
            adapter.submitList(winningcard)
        }

        binding.btnNextround.setOnClickListener {
            val mainActivity = activity as? MainActivity
            if (listener.getRound() <= 10) {
                mainActivity?.scoreboardBack(false)
            } else {
                mainActivity?.scoreboardBack(true)
            }
        }
    }
}

