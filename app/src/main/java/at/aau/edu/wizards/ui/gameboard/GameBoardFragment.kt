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

    // Define the OnDragListener for the TextView
    val textView = findViewById<TextView>(R.id.text_view)
    textView.setOnDragListener(object : View.OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Get the dropped item from the DragEvent
                    val item = event.clipData.getItemAt(0).text
                    // Set the text of the TextView to the dropped item
                    textView.text = item
                    return true
                }
            }
            return false
        }
    })

    // Define the OnTouchListener for the RecyclerView
    val recyclerView = findViewById<RecyclerView>(R.id.gameboard_recycler_view)
    recyclerView.setOnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_DOWN) {
            val view = recyclerView.findChildViewUnder(event.x, event.y)
            if (view != null) {
                // Start the drag operation when an item is touched
                val item = ClipData.Item(view.toString())
                val dragData = ClipData("item", arrayOf("text/plain"), item)
                val shadow = View.DragShadowBuilder(view)
                view.startDragAndDrop(dragData, shadow, null, 0)
                return@setOnTouchListener true
            }
        }
        false
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

