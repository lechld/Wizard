package at.aau.edu.wizards.ui.gameboard


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentGameboardBinding
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardAdapter
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardBoardAdapter
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardHeaderAdapter
import at.aau.edu.wizards.util.OffsetItemDecoration

class GameBoardFragment : Fragment() {

    private val asClient by lazy {
        requireArguments().getBoolean(AS_CLIENT_EXTRA)
    }

    private val amountCpu by lazy {
        requireArguments().getInt(AMOUNT_CPU_EXTRA)
    }

    private var binding: FragmentGameboardBinding? = null

    private val viewModel by lazy {
        val factory = GameBoardViewModelFactory(
            asClient = asClient,
            amountCpu = amountCpu,
            server = Server.getInstance(requireContext()),
            client = Client.getInstance(requireContext())
        )
        ViewModelProvider(this, factory)[GameBoardViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val adapter = GameBoardAdapter {
            viewModel.sendMessage(it.getString())
        }

        binding.gameboardRecyclerView.adapter = adapter
        binding.gameboardRecyclerView.addItemDecoration(OffsetItemDecoration(90))

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards)
        }

        val adapterBoard = GameBoardBoardAdapter(viewModel, viewModel.gameModel)

        binding.gameboardBoardRecyclerView.adapter = adapterBoard
        binding.gameboardBoardRecyclerView.addItemDecoration(OffsetItemDecoration(310))

        viewModel.board.observe(viewLifecycleOwner) { cards ->
            adapterBoard.submitList(cards)
        }

        val adapterHeader = GameBoardHeaderAdapter()

        binding.gameboardHeaderRecyclerView.adapter = adapterHeader

        viewModel.header.observe(viewLifecycleOwner) { header ->
            adapterHeader.submitList(header)
        }

        viewModel.player.observe(viewLifecycleOwner) { player ->
            binding.gameboardBoardRecyclerView.scrollToPosition(player) //Doesn't work
        }

        viewModel.trump.observe(viewLifecycleOwner) { trump ->
            //binding.boardBackground.setImageResource(trump.imageBackground())
            //binding.boardSlice.setImageResource(trump.imageSlice())
            //binding.boardHeaderBackground.setImageResource(trump.imageHeaderBackground())
        }
    }

    companion object {
        private const val AS_CLIENT_EXTRA = "AS_CLIENT_EXTRA"
        private const val AMOUNT_CPU_EXTRA = "AMOUNT_CPU_EXTRA"
        fun instance(asClient: Boolean, amountCpu: Int = 0): GameBoardFragment {
            if (asClient && amountCpu > 0) {
                // This is not handled ideally, but fine for now
                throw IllegalArgumentException("Only Server is allowed to define cpu players")
            }

            return GameBoardFragment().apply {
                arguments = bundleOf(
                    AS_CLIENT_EXTRA to asClient,
                    AMOUNT_CPU_EXTRA to amountCpu
                )
            }
        }
    }

}

