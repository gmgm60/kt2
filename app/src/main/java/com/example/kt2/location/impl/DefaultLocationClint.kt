package com.example.kt2.location.impl

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.example.kt2.location.domain.LocationClint
import com.example.kt2.location.domain.hasLocationPermission
import com.example.kt2.location.domain.isLocationServiceEnabled
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

class DefaultLocationClint(private val context: Context) : LocationClint {
   // private val clint: FusedLocationProviderClient = FusedLocationProviderClient(context)
    private val clint2: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    @SuppressLint("MissingPermission")
    override fun getLocationFlow(updateTime: Long): Flow<Location> {
        return callbackFlow {
            if (!context.hasLocationPermission()) {
                throw LocationClint.LocationException("Missing Location Permission !")
            }

            if (!context.isLocationServiceEnabled()) {
                throw LocationClint.LocationException("Service is Disabled !")
            }

            val request = LocationRequest.create()
                .setInterval(updateTime)
                .setFastestInterval(updateTime)

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            clint2.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                clint2.removeLocationUpdates(locationCallback)
            }


        }

    }
}