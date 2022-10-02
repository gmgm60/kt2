package com.example.kt2.location.domain

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClint {
    fun getLocationFlow(updateTime:Long) : Flow<Location>

    class LocationException(message:String):Exception()

}