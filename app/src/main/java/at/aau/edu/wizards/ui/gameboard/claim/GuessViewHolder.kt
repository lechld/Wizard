package at.aau.edu.wizards.ui.gameboard.claim

import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemGuessBinding

class GuessViewHolder(
    private val binding: ItemGuessBinding,
    private val onClick: (Int) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {

    private val vibrator: Vibrator by lazy {
        ContextCompat.getSystemService(binding.root.context, Vibrator::class.java)!!
    }

    fun bind(value: Int) {
        binding.text.text = value.toString()
        binding.root.setOnClickListener {
            onClick(value)

            println("VIBRATE GUESSVIEWHOLDER")

            // Trigger haptic feedback
            if (vibrator.hasVibrator() == true) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    // Deprecated in API 26
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(50)
                }
            }
        }
    }
}