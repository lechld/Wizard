package at.aau.edu.wizards.ui.gameboard


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnDragListener
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.api.Server
import at.aau.edu.wizards.databinding.FragmentGameboardBinding
import at.aau.edu.wizards.gameModel.GameModelCard
import at.aau.edu.wizards.ui.gameboard.claim.GuessAdapter
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardAdapter
import at.aau.edu.wizards.ui.gameboard.recycler.GameBoardBoardAdapter
import at.aau.edu.wizards.ui.gameboard.shake.ShakeDetector
import at.aau.edu.wizards.util.OffsetItemDecoration
import com.google.android.material.dialog.MaterialAlertDialogBuilder


class GameBoardFragment : Fragment(), OnDragListener {

    private val asClient by lazy {
        requireArguments().getBoolean(AS_CLIENT_EXTRA)
    }

    private val amountCpu by lazy {
        requireArguments().getInt(AMOUNT_CPU_EXTRA)
    }

    private var binding: FragmentGameboardBinding? = null

    private val vibrator: Vibrator by lazy {
        binding?.root?.let { ContextCompat.getSystemService(it.context, Vibrator::class.java) }!!
    }

    private val viewModel by lazy {
        val factory = GameBoardViewModelFactory(
            asClient = asClient,
            amountCpu = amountCpu,
            server = Server.getInstance(requireContext()),
            client = Client.getInstance(requireContext())
        )
        ViewModelProvider(this, factory)[GameBoardViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activity = requireActivity()

        activity.onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                MaterialAlertDialogBuilder(activity).setMessage(getString(R.string.leave_warning))
                    .setPositiveButton(getString(R.string.no), null)
                    .setNegativeButton(getString(R.string.yes)) { _, _ ->
                        isEnabled = false
                        activity.onBackPressedDispatcher.onBackPressed()
                    }.create().show()
            }
        })


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentGameboardBinding.inflate(inflater, container, false)

        this.binding = binding

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        registerShakeListener()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupUI() {
        val binding = this.binding ?: return

        setupHand(binding)
        setupBoard(binding)
        setupGuess(binding)
        setupHeader(binding)
        setupTrump(binding)
        setupScoreboard()
        setupPopUp()
    }


    private fun setupTrump(binding: FragmentGameboardBinding) {
        binding.trumpIndicatorCard.text.visibility = View.INVISIBLE
        viewModel.trump.observe(viewLifecycleOwner) { trump ->
            binding.trumpIndicatorCard.image.setImageResource(trump.image())
            binding.trumpIndicatorCard.text.text = trump.getNumber()
        }
    }

    private fun setupScoreboard() {

        binding?.btnScoreboard?.setOnClickListener {
            val mainActivity = activity as? MainActivity
            mainActivity?.showScoreboard(viewModel.gameModel.listener)
        }

        viewModel.scoreboard.observe(viewLifecycleOwner) { finish ->
            if (finish) {
                val mainActivity = activity as? MainActivity
                mainActivity?.showScoreboard(viewModel.gameModel.listener)
            }
        }
    }

    private fun setupPopUp() {
        viewModel.winningcard.observe(viewLifecycleOwner) {
            if (it.visible) {
                binding?.winningCard?.root?.visibility = View.VISIBLE
                binding?.winningCard?.winningCard?.setImageResource(it.lastCardWon.image())
                val playerName = viewModel.gameModel.listener.getNameOfPlayer(it.lastPlayerWon)
                binding?.winningCard?.tvPlayerWon?.text = buildString {
                    append(playerName)
                    append(" \n won!")
                }
                binding?.trumpIndicatorCard?.root?.visibility = View.INVISIBLE
                binding?.boardRecycler?.visibility = View.INVISIBLE
                binding?.gameboardRecyclerView?.visibility = View.INVISIBLE
                binding?.seperationBoardHand?.visibility = View.INVISIBLE
            } else {
                binding?.winningCard?.root?.visibility = View.INVISIBLE
                binding?.trumpIndicatorCard?.root?.visibility = View.VISIBLE
                binding?.boardRecycler?.visibility = View.VISIBLE
                binding?.gameboardRecyclerView?.visibility = View.VISIBLE
                binding?.seperationBoardHand?.visibility = View.VISIBLE
            }
        }
    }

    private fun setupHand(binding: FragmentGameboardBinding) {
        val handAdapter = GameBoardAdapter()

        binding.gameboardRecyclerView.adapter = handAdapter
        binding.gameboardRecyclerView.addItemDecoration(OffsetItemDecoration(90))

        viewModel.cards.observe(viewLifecycleOwner) { cards ->
            handAdapter.submitList(cards)
        }
    }

    private fun setupGuess(binding: FragmentGameboardBinding) {
        val guessAdapter = GuessAdapter { guess ->
            // should not access GameModel here
            viewModel.gameModel.sendGuessOfLocalPlayer(guess)
        }

        binding.guessRecycler.adapter = guessAdapter

        viewModel.guess.observe(viewLifecycleOwner) { guess ->
            guessAdapter.submitList(guess)
        }
    }

    private fun setupBoard(binding: FragmentGameboardBinding) {
        val boardAdapter = GameBoardBoardAdapter()

        binding.boardRecycler.adapter = boardAdapter
        binding.boardRecycler.addItemDecoration(OffsetItemDecoration(310))

        viewModel.board.observe(viewLifecycleOwner) { cards ->
            boardAdapter.submitList(cards)
        }
    }

    private fun setupHeader(binding: FragmentGameboardBinding) {
        viewModel.headersWithCurrentPlayer.observe(viewLifecycleOwner) {
            val headers = it.first
            val currentPlayer = it.second
            val header = headers.getOrNull(currentPlayer) ?: return@observe

            binding.header.headerIcon.setImageResource(viewModel.getIconFromId(header.icon))
            binding.header.headerUsername.text = header.name
            binding.header.headerScore.text = header.score.toString()
            binding.header.headerGuessAndWins.text = buildString {
                append(header.wins)
                append(" / ")
                append(header.guess)
            }
        }

        binding.dragContainer.setOnDragListener(this)
    }

    override fun onDrag(view: View, event: DragEvent): Boolean {
        val binding = this.binding
        if (event.action == DragEvent.ACTION_DROP && binding != null) {
            val dropX = event.x
            val dropY = event.y

            if (dropX < binding.dragContainer.width && dropY < binding.dragContainer.height) {
                val item: GameModelCard = event.localState as GameModelCard
                viewModel.sendMessage(item.getString())

                // Trigger haptic feedback
                if (vibrator.hasVibrator()) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        vibrator.vibrate(
                            VibrationEffect.createOneShot(
                                50,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    } else {
                        // Deprecated in API 26
                        @Suppress("DEPRECATION")
                        vibrator.vibrate(50)
                    }
                }
            } else {
                return false
            }
        }
        return true
    }

    private fun registerShakeListener() {
        val shakeDetector = ShakeDetector()
        shakeDetector.setOnShakeListener {
            val arrayGuessesPossibilities = viewModel.getBuildGuess()
            var selectedOption = 0
            if (viewModel.gameModel.listener.getHandOfPlayer(viewModel.gameModel.localPlayer()).size > 1 && viewModel.gameModel.listener.getRound() > 1 && !viewModel.gameModel.listener.guessing && !viewModel.gameModel.listener.hasCheated()) {
                activity?.let {
                    MaterialAlertDialogBuilder(it).setTitle("Shake event detected. Guess updated")
                        .setSingleChoiceItems(
                            arrayGuessesPossibilities, selectedOption
                        ) { _, which ->
                            selectedOption = which
                        }.setPositiveButton("Ok") { _, _ ->
                            val selectedGuess = arrayGuessesPossibilities[selectedOption]
                            val selectedGuessInt = selectedGuess.toString().toInt()
                            viewModel.updateGuess(selectedGuessInt)
                        }.show()
                }
            }
        }


        val sensorManager =
            requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(
            shakeDetector, accelerometer, SensorManager.SENSOR_DELAY_NORMAL
        )
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
                    AS_CLIENT_EXTRA to asClient, AMOUNT_CPU_EXTRA to amountCpu
                )
            }
        }
    }
}