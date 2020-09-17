package com.example.otglister

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var listView : ListView
    private var sensor: Sensor? = null
    private var sensorManager: SensorManager? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    data class LightPosLog(val user: String, val time: String, val light: Float, var location: Location?, var sent2DB: Boolean?)

    private var user: String = ""
    var dataList = HashMap<Int, LightPosLog>()
    var scanning = true;
    var sending = true;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById<ListView>(R.id.devices_list_view)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener()        {

        }
        val bgWorkSwitch = findViewById<Switch>(R.id.switch2)
        bgWorkSwitch.setOnCheckedChangeListener(){ compoundButton: CompoundButton, b: Boolean ->
            onBgWorkCheckedChanged(b)
        }
        val firebaseSwitch = findViewById<Switch>(R.id.switch3)
        firebaseSwitch.setOnCheckedChangeListener(){ compoundButton: CompoundButton, b: Boolean ->
            onFirebaseCheckedChanged(b)
        }

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //sensorManager!!.unregisterListener(this)
        if(scanning){
            val toast = Toast.makeText(applicationContext, "EyeCare is still reading light sensor values.", Toast.LENGTH_SHORT)
            toast.show()
        }
    }

    fun getTimeStamp(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        val formatted = current.format(formatter)
        return formatted
    }

    fun getLastKnownLocation() : Location?{
        var ret : Location? = null
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                // Got last known location. In some rare situations this can be null.
                ret = location
            }
        return ret
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        try {
        } catch (e: Exception) {
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        try {
            var temp = LightPosLog(user, this.getTimeStamp(), event!!.values[0], getLastKnownLocation(), false)
            dataList.put(dataList.count()+1, temp)
        } catch (e: IOException) {
        }

        var counter = dataList.count()
        var listItems = arrayOfNulls<String>(counter)
        dataList.values.forEach { entry ->
            listItems[counter - 1] = "" + entry.time + " : " + entry.light;
            counter--
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems)
        listView.adapter = adapter
    }

    fun onBgWorkCheckedChanged(checked: Boolean) {
        // implementation
        scanning = checked
    }

    fun onFirebaseCheckedChanged(checked: Boolean) {
        // implementation
        sending = checked
    }
}