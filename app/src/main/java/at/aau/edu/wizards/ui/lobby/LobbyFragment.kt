package at.aau.edu.wizards.ui.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentLobbyBinding
import at.aau.edu.wizards.ui.lobby.recycler.LobbyAdapter

class LobbyFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            LobbyViewModel.Factory(Server.getInstance(requireContext()))
        )[LobbyViewModel::class.java]
    }

    private var binding: FragmentLobbyBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLobbyBinding.inflate(inflater, container, false)

        this.binding = binding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onDestroyView() {
        viewModel.stopAdvertising()
        binding = null
        super.onDestroyView()
    }

    private fun setupUI() {
        val binding = this.binding ?: return
        val adapter = LobbyAdapter { clickedItem ->
            viewModel.clicked(clickedItem)
        }

        binding.lobbyRecycler.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { endpoints ->
            adapter.submitList(endpoints)
        }

        binding.startGameButton.setOnClickListener {
            viewModel.startGame()

            val mainActivity = activity as? MainActivity ?: return@setOnClickListener

            mainActivity.showGame(asClient = false)
        }
        viewModel.startAdvertising()
    }
}