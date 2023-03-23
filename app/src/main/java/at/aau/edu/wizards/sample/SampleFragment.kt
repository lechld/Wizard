package at.aau.edu.wizards.sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.R

class SampleFragment : Fragment() {

    // Create instance of view model once fragment is created
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            SampleViewModel.Factory("some input", SampleDataSourceImpl())
        )[SampleViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
    }

    private fun setupUI() {
        val textView = view?.findViewById<TextView>(R.id.some_text_view)

        viewModel.dataToBeObservedInFragment.observe(viewLifecycleOwner) { newData ->
            println("Received new data from view model. Do something with it.")
            println(newData)

            // could use
            // textView?.text = "something" to update the text of the textview
        }
    }
}