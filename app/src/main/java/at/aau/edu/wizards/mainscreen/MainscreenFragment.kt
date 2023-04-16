package at.aau.edu.wizards.mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.databinding.FragmentMainscreenBinding

class MainscreenFragment : Fragment() {

    // Create instance of view model once fragment is created
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MainscreenViewModel.Factory("some input", MainscreenDataSourceImpl())
        )[MainscreenViewModel::class.java]
    }

    private var binding: FragmentMainscreenBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainscreenBinding.inflate(inflater, container, false)

        this.binding = binding

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    override fun onDestroyView() {
        binding = null // important to set binding to null here. It's a memory leak otherwise.
        super.onDestroyView()
    }

    private fun setupUI() {
        val binding = this.binding ?: return
        val adapter = RoomAdapter()
        binding.recyclerview.adapter = adapter

        viewModel.dataToBeObservedInFragment.observe(viewLifecycleOwner) { newData ->
            adapter.submitList(newData)
            // could use
            // binding.someTextView.text = "something" to update the text of the textview
        }

        // binding.someTextView.text = "something" to update the text of the textview
    }
}