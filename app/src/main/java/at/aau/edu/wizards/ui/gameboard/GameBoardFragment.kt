package at.aau.edu.wizards.ui.gameboard


import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentGameboardBinding
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardAdapter

class GameBoardFragment : Fragment(){

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

        binding?.root?.findViewById<TextView>(R.id.text_view)?.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val item = event.clipData.getItemAt(0)
                    val cardImage = item.text.toString().toInt()
                    //binding.textview.text = getString(R.string.dropped_card_image, cardImage)
                    true
                }
                else -> false
            }
        }

    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    val adapter = GameBoardAdapter(dragListener = OnStartDragListener)

    private val itemTouchHelper by lazy {
        val callback = ItemTouchHelperCallback(adapter)
        ItemTouchHelper(callback)
    }

    private fun setupUI() {
        val binding = this.binding ?: return
        val adapter = GameBoardAdapter(dragListener)

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            adapter.submitList(cards)
        }
        itemTouchHelper.attachToRecyclerView(binding.gameboardRecyclerView)
    }
    fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
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

