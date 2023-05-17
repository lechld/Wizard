package at.aau.edu.wizards.ui.gameboard.claim

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import at.aau.edu.wizards.databinding.ItemGuessBinding

class GuessViewHolder(
    private val binding: ItemGuessBinding,
    private val onClick: (Int) -> Unit,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(value: Int) {
        binding.text.text = value.toString()
        binding.root.setOnClickListener {
            onClick(value)

            // Trigger haptic feedback
            val vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
            if (vibrator?.hasVibrator() == true) {
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