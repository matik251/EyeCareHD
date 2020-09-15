package com.example.eyecarehd.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.eyecarehd.R
import java.io.IOException

class HomeFragment : Fragment(), SensorEventListener {

    var sensor : Sensor? = null
    var sensorMaanager : SensorManager? = null
    var isRunning = false;
    private lateinit var homeViewModel: HomeViewModel
    var textView: TextView? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        textView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView?.text = it
        })

        sensorMaanager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorMaanager!!.getDefaultSensor(Sensor.TYPE_LIGHT)

        return root
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        //TODO("Not yet implemented")
        try{

        }catch(e: Exception){

        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        //TODO("Not yet implemented")
        try{
            textView?.text = "" + event!!.values[0]
            if(event!!.values[0] < 30 && isRunning == false){
                isRunning = true
            }else{
                isRunning = false
            }
        }catch(e : IOException){

        }
    }
}
