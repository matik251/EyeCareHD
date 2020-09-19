package com.example.otglister

import android.os.Parcel
import android.os.Parcelable
import java.sql.Time
import java.io.Serializable

public data class DataRecords(
    val Mac: String?,
    val Category: String?,
    val Data: Int,
    val CreationTime: String?,
    val SendTime: String? )  {

}