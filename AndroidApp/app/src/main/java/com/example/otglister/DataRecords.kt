package com.example.otglister

import java.sql.Time

data class DataRecords(val Mac: String,
                       val Category: String,
                       val Data: Int,
                       val CreationTime: Time,
                       val SendTime: Time ) {

}