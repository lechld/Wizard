package at.aau.edu.wizards.ui.lobby

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentLobbyBinding
import at.aau.edu.wizards.ui.lobby.recycler.LobbyAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class LobbyFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this, LobbyViewModel.Factory(Server.getInstance(requireContext()))
        )[LobbyViewModel::class.java]
    }

    private var binding: FragmentLobbyBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.startAdvertising()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.stopAdvertising()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        binding = null
        super.onDestroyView()
    }

    private fun setupUI() {
        val binding = this.binding ?: return
        val adapter = LobbyAdapter { clickedItem ->
            viewModel.clicked(clickedItem)

            if (viewModel.checkTooManyPlayer) {
                activity?.let {
                    MaterialAlertDialogBuilder(it).setMessage(getString(R.string.max_player))
                        .setPositiveButton(getString(R.string.okay), null)
                }?.create()?.show()
            }

        }

        binding.lobbyRecycler.adapter = adapter

        viewModel.items.asLiveData().observe(viewLifecycleOwner) { endpoints ->
            adapter.submitList(endpoints)
        }

        binding.startGameButton.setOnClickListener {

            if (viewModel.numPlayer < 3) {
                activity?.let {
                    MaterialAlertDialogBuilder(it).setMessage(getString(R.string.min_player))
                        .setPositiveButton(getString(R.string.okay), null)
                }?.create()?.show()
            }
            else {
                val amountCpu = viewModel.startGame()
                val mainActivity = activity as? MainActivity ?: return@setOnClickListener

                mainActivity.showGame(asClient = false, amountCpu = amountCpu)
            }
        }
    }
}