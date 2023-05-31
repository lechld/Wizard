package at.aau.edu.wizards.ui.gameboard.shake

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener

class ShakeDetector : SensorEventListener {

    private var shakeListener: OnShakeListener? = null
    private var shakeTimestamp: Long = 0
    private var shakeCount: Int = 0

    fun setOnShakeListener(listener: OnShakeListener) {
        this.shakeListener = listener
    }

    interface OnShakeListener {
        fun onShake(count: Int)
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // ignore
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (shakeListener != null && event != null) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            if (x > 2 || x < -2 || y > 12 || y < -12 || z > 2 || z < -2) {

                val now = System.currentTimeMillis()

                if (shakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return
                }

                if (shakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    shakeCount = 0
                }
                shakeTimestamp = now
                shakeCount++

                shakeListener!!.onShake(shakeCount)
            }

        }

    }

    companion object {
        private val SHAKE_THRESHOLD_GRAVITY = 2.7f
        private val SHAKE_SLOP_TIME_MS = 500
        private val SHAKE_COUNT_RESET_TIME_MS = 3000
    }
}