package com.example.otglister

import java.sql.Time

data class Devices(val IpAddress: String,
                       val Category: String,
                       val Data: Int,
                       val CreationTime: Time,
                       val ModTime: Time) {
}