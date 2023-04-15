package at.aau.edu.wizards.Mainscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.FragmentSampleBinding

class Mainscreen_Fragment : Fragment() {

    // Create instance of view model once fragment is created
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            Mainscreen_ViewModel.Factory("some input", MainscreenDataSourceImpl())
        )[Mainscreen_ViewModel::class.java]
    }

    private var binding: FragmentSampleBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSampleBinding.inflate(inflater, container, false)

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

        viewModel.dataToBeObservedInFragment.observe(viewLifecycleOwner) { newData ->
            println("Received new data from view model. Do something with it.")
            println(newData)

            // could use
            // binding.someTextView.text = "something" to update the text of the textview
        }

        // binding.someTextView.text = "something" to update the text of the textview
    }
}