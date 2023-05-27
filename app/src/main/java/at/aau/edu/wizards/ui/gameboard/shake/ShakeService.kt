package at.aau.edu.wizards.ui.gameboard.shake

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import kotlin.math.sqrt

class ShakeService : Service(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var accel: Float = 0.toFloat()
    private var accelCurrent: Float = 0.toFloat()
    private var accelLast: Float = 0.toFloat()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager!!
            .getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager!!.registerListener(
            this, accelerometer,
            SensorManager.SENSOR_DELAY_UI, Handler()
        )
        return Service.START_STICKY
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        accelLast = accelCurrent
        accelCurrent = sqrt((x * x + y * y + z * z))
        val delta = accelCurrent - accelLast
        accel = accel * 0.9f + delta

        if (accel > 11) {

            Toast.makeText(applicationContext, "Shake event detected", Toast.LENGTH_SHORT).show()
        }
    }

}