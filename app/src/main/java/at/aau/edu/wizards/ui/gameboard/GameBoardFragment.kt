package at.aau.edu.wizards.ui.gameboard


import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentGameboardBinding
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardAdapter
import kotlin.coroutines.coroutineContext

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
        val adapter = GameBoardAdapter()

        binding.gameboardRecyclerView.adapter = adapter
        binding.gameboardRecyclerView.addItemDecoration(OffsetDecoration())

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards)
        }

        val adapterBoard = GameBoardAdapter()

        binding.gameboardBoardRecyclerView.adapter = adapterBoard
        binding.gameboardRecyclerView.addItemDecoration(OffsetBoardDecoration())

        viewModel.board.observe(viewLifecycleOwner) {cards ->
            adapterBoard.submitList(cards)
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

    class OffsetDecoration : RecyclerView.ItemDecoration() {

        private val overlap = 60

        override fun getItemOffsets(outRect : Rect, view : View, parent : RecyclerView, state : RecyclerView.State) {
            outRect.set(0, 0, -overlap, 0)
        }
    }

    class OffsetBoardDecoration : RecyclerView.ItemDecoration() {

        private val overlap = 120

        override fun getItemOffsets(outRect : Rect, view : View, parent : RecyclerView, state : RecyclerView.State) {
            outRect.set(0, 0, -overlap, 0)
        }
    }
}

