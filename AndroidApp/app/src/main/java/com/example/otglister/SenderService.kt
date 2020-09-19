package com.example.otglister

import android.app.IntentService
import android.content.Intent
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.time.LocalDateTime

class SenderService : IntentService(SenderService::class.simpleName){

    private val client = OkHttpClient();

    fun run() {
        val currTime = LocalDateTime.now();
        val payload = "{\n" +
                "    \"Mac\": \"30-52-CB-81-87-27\",\n" +
                "    \"Category\": \"Sender\",\n" +
                "    \"Data\": 154,\n" +
                "    \"CreationTime\": \"2020-09-19T01:50:34\",\n" +
                "    \"SendTime\": \"$currTime\"\n" +
                "}"

        val okHttpClient = OkHttpClient()
        val requestBody = payload.toRequestBody()
        val request = Request.Builder()
            .method("POST", requestBody)
            .url("https://192.168.55.105:5001/api/DataRecords")
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle this
            }

            override fun onResponse(call: Call, response: Response) {
                // Handle this
            }
        })
    }
    override fun onHandleIntent(workIntent: Intent?) {
        // Gets data from the incoming Intent
        val dataString = workIntent?.dataString
        //...
        // Do work here, based on the contents of dataString
        //...


    }
}
