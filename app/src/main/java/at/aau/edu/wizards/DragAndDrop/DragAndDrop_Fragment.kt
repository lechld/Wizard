package at.aau.edu.wizards.DragAndDrop;

import android.os.Build
import android.os.Bundle
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import at.aau.edu.wizards.R
import at.aau.edu.wizards.databinding.FragmentSampleBinding
import at.aau.edu.wizards.sample.SampleDataSourceImpl

class DragAndDrop_Fragment : Fragment() {

    // Create instance of view model once fragment is created
    private val viewModel by lazy {
        ViewModelProvider(
            this,
            DragAndDrop_ViewModel.Factory("some input", SampleDataSourceImpl())
        )[DragAndDrop_ViewModel::class.java]
    }

    private var binding: FragmentSampleBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSampleBinding.inflate(inflater, container, false)

        this.binding = binding
        val dragView = binding.root.findViewById<View>(R.id.drag_view)
        val dropView = binding.root.findViewById<View>(R.id.drop_view)
        dragView.setOnLongClickListener {
            // Speichere die gezogene View und ihre ursprüngliche Position
            draggedView = it
            initialX = it.x
            initialY = it.y

            // Berechne den Offset zwischen dem Finger des Benutzers und der oberen linken Ecke der View
            offsetX = it.x - it.left
            offsetY = it.y - it.top

            // Starte die Drag and Drop-Operation
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(null, View.DragShadowBuilder(it), null, 0)
            }
            true
        }

        // Füge einen OnDragListener zum DropView hinzu, um die View zu positionieren
        dropView.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    // Setze die Hintergrundfarbe des DropViews, um anzuzeigen, dass es für eine Drop-Operation bereit ist
                    //dropView.setBackgroundColor(resources.getColor(R.color.drop_ready))
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Setze die Hintergrundfarbe des DropViews, um anzuzeigen, dass die View darüber gezogen wird
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    // Positioniere die View entsprechend der aktuellen Position des Fingers des Benutzers
                    val x = event.x - offsetX
                    val y = event.y - offsetY
                    draggedView?.animate()?.x(x)?.y(y)?.setDuration(0)?.start()
                    true
                }
                DragEvent.ACTION_DROP -> {
                    // Setze die View an die neue Position
                    val x = event.x - offsetX
                    val y = event.y - offsetY
                    draggedView?.animate()?.x(x)?.y(y)?.setDuration(0)?.start()
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    // Setze die Hintergrundfarbe des DropViews zurück
                    //dropView.setBackgroundColor(resources.getColor(R.color.drop_normal))
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    // Setze die Hintergrundfarbe des DropViews zurück und setze die View auf ihre ursprüngliche Position, falls sie nicht abgelegt wurde
                    //dropView.setBackgroundColor(resources.getColor(R.color.drop_normal))
                    if (!event.result) {
                        draggedView?.animate()?.x(initialX)?.y(initialY)?.setDuration(0)?.start()
                    }
                    true
                }
                else -> false
            }
        }

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

    private var draggedView: View? = null
    private var initialX: Float = 0.toFloat()
    private var initialY: Float = 0.toFloat()
    private var offsetX: Float = 0.toFloat()
    private var offsetY: Float = 0.toFloat()

    private fun setupUI() {
        val binding = this.binding ?: return

        viewModel.dataToBeObservedInFragment.observe(viewLifecycleOwner) { newData ->
            println("Received new data from view model. Do something with it.")
            println(newData)
            // could use
            // binding.someTextView.text = "something" to update the text of the textview
        }
    }

        // binding.someTextView.text = "something" to update the text of the textview
}
