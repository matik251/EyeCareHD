package com.example.otglister

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.net.wifi.WifiManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
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
    var sending = false;

    var endpointList = HashMap<Int, String>()

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
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            );
        }
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
        var temp: LightPosLog? = null;
        try {
            temp = LightPosLog(getMac(this).toString(), this.getTimeStamp(), event!!.values[0], getLastKnownLocation(), false)
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

        if(sending){
            var result = sendData(temp)
            temp?.sent2DB = result;
            if (temp != null) {
                dataList.replace(dataList.count(), temp)
            }
            sendAllData();
        }
    }

    public fun onBgWorkCheckedChanged(checked: Boolean) {
        // implementation
        scanning = checked
    }

    public fun onFirebaseCheckedChanged(checked: Boolean) {
        // implementation
        sending = checked
        //sendData()
    }

    fun getMac(context: Context): String {
        val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = manager.connectionInfo
        return info.macAddress.toUpperCase()
    }

    fun sendAllData(){
        for (entry in dataList) {
            if(entry.value.sent2DB == false){
                var result = sendData(entry.value)
                if(result){
                    entry.value.sent2DB = true;
                    dataList.replace(entry.key, entry.value)
                }
            }
        }

    }

    fun sendData(data: LightPosLog?):Boolean {
        var result = false;
        val currTime = LocalDateTime.now();
        val payload = "{\n" +
                "    \"Mac\": \"${data?.user.toString()}\",\n" +
                "    \"Category\": \"Light\",\n" +
                "    \"Data\": ${data?.light?.toInt()},\n" +
                "    \"Position\": \"${data?.location?.toString()}\",\n" +
                "    \"CreationTime\": \"${data?.time.toString().replace(" ","T")}\",\n" +
                "    \"SendTime\": \"$currTime\"\n" +
                "}"

        val okHttpClient = OkHttpClient.Builder()
            .followRedirects(true)
            .build()
        var endpointURL = endpointList.get(0); //pobranie adresu Endpointa
        val requestBody = payload.toRequestBody()
        val request = endpointURL?.let {
            Request.Builder()
                .header("Content-Type","application/json")
                .method("POST", requestBody)
                .url("192.168.55.105:5001"+ "/api/DataRecords") //usunac .url(it)
                .build()
        }
        if (request != null) {
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    endpointList.remove(0) //usuwanie wadliwego Endpointa
                }
                override fun onResponse(call: Call, response: Response) {
                    result = true
                }
            })
        }
        return result
    }

    fun getEndpoints(): String {
        var responseString: String? = null;
        val client = OkHttpClient.Builder()
            .followRedirects(true)
            .build()
            val request = Request.Builder()
                .url("192.168.55.105:5001" + "/api/Devices")
                .build()
        client.newCall(request).execute().use { response -> return response.body!!.string() }
    }
}
