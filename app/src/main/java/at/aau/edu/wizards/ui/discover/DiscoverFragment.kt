package at.aau.edu.wizards.ui.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.MainActivity
import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.databinding.FragmentDiscoverBinding
import at.aau.edu.wizards.ui.discover.recycler.DiscoverAdapter


class DiscoverFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DiscoverViewModel.Factory(Client.getInstance(requireContext()))
        )[DiscoverViewModel::class.java]
    }

    private var binding: FragmentDiscoverBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.startDiscovery()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.stopDiscovery()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        this.binding = binding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupAnimation()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun setupUI() {
        val binding = this.binding ?: return
        val adapter = DiscoverAdapter { clickedPending ->
            viewModel.connectEndpoint(clickedPending)
        }

        binding.discoverRecycler.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { endpoints ->
            adapter.submitList(endpoints)
        }

        viewModel.startGame.observe(viewLifecycleOwner) {
            val mainActivity = activity as? MainActivity ?: return@observe

            mainActivity.showGame(asClient = true)
        }
    }

    private fun setupAnimation() {
        val binding = this.binding ?: return
        val context = this.context ?: return
        val animation = AnimationUtils.loadAnimation(context, R.anim.crystal_animation)

        binding.animatedView.animation = animation
    }
}