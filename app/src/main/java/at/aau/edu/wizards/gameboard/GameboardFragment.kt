package at.aau.edu.wizards.gameboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider


import at.aau.edu.wizards.R
import at.aau.edu.wizards.api.Client
import at.aau.edu.wizards.databinding.FragmentGameboardBinding
import at.aau.edu.wizards.sample.SampleDataSourceImpl
import at.aau.edu.wizards.sample.SampleViewModel


class GameboardFragment : Fragment() {

    private var binding: FragmentGameboardBinding? = null

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            GameboardViewModel.Factory("") //WAS KOMMT HIER
        )[GameboardViewModel::class.java]
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

    // WIE FUNKTIONIERT DER SETUPUI AUFBAU
    private fun setupUI(binding: FragmentGameboardBinding) {

        val binding = this.binding ?: return
        val adapter = GameboardAdapter {
        }

        binding.gameboardRecyclerView.adapter = adapter

        viewModel.items.observe(viewLifecycleOwner) { endpoints ->
            adapter.submitList(endpoints)
        }

        viewModel.()

    }


}

