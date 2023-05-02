package at.aau.edu.wizards.ui.gameboard


import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnDragListener
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentGameboardBinding
import at.aau.edu.wizards.databinding.ItemCardBinding
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardAdapter


class GameBoardFragment : Fragment(), OnDragListener {

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

        binding.dragContainer.setOnDragListener(this)
    }

    override fun onDrag(view: View, event: DragEvent): Boolean {
        val binding = this.binding
        if (event.action == DragEvent.ACTION_DROP && binding != null) {

            val dropX = event.x
            val dropY = event.y
            val item = event.localState as Cards
            val inflater = LayoutInflater.from(requireContext())
            val shape = ItemCardBinding.inflate(inflater, binding.dragContainer,false)

            shape.root.x = dropX
            shape.root.y = dropY

            /*
            shape.setImageResource(state.item.getImageDrawable());
            shape.setX(dropX - (float) state.width / 2);
            shape.setY(dropY - (float) state.height / 2);
            shape.getLayoutParams().width = state.width;
            shape.getLayoutParams().height = state.height;
            */
            binding.dragContainer.addView(shape.root)

        }

        return true
    }

    companion object {
        private const val AS_CLIENT_EXTRA = "AS_CLIENT_EXTRA"
        private const val AMOUNT_CPU_EXTRA = "AMOUNT_CPU_EXTRA"
        fun instance(asClient: Boolean, amountCpu: Int = 0): GameBoardFragment {
            if (asClient && amountCpu > 0) {
                // This is not handled idealy, but fine for now
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

