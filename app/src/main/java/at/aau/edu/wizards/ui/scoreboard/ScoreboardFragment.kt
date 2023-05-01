package at.aau.edu.wizards.ui.scoreboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentScoreboardBinding
import at.aau.edu.wizards.ui.lobby.recycler.LobbyAdapter
import at.aau.edu.wizards.ui.scoreboard.recycler.ScoreboardAdapter

class ScoreboardFragment : Fragment() {

    private val asClient by lazy {
        requireArguments().getBoolean(AS_CLIENT_EXTRA)
    }

    private val amountCpu by lazy {
        requireArguments().getInt(AMOUNT_CPU_EXTRA)
    }

    private var binding: FragmentScoreboardBinding? = null

    private val viewModel by lazy {
        val factory = ScoreboardViewModelFactory(
            asClient = asClient,
            amountCpu = amountCpu,
            server = Server.getInstance(requireContext()),
            client = Client.getInstance(requireContext())
        )
        ViewModelProvider(this, factory)[ScoreboardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        viewModel.points.observe(viewLifecycleOwner) { points ->
            adapter.submitList(points)

            viewModel.avatar.observe(viewLifecycleOwner) { avatar ->
                adapter.submitList(avatar)
            }
        }
    }



    companion object {
        private const val AS_CLIENT_EXTRA = "AS_CLIENT_EXTRA"
        private const val AMOUNT_CPU_EXTRA = "AMOUNT_CPU_EXTRA"
        fun instance(asClient: Boolean, amountCpu: Int = 0): ScoreboardFragment {
            if (asClient && amountCpu > 0) {
                // This is not handled idealy, but fine for now
                throw IllegalArgumentException("Only Server is allowed to define cpu players")
            }

            return ScoreboardFragment().apply {
                arguments = bundleOf(
                    AS_CLIENT_EXTRA to asClient,
                    AMOUNT_CPU_EXTRA to amountCpu
                )
            }
        }
    }
}
