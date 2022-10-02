package com.example.kt2

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.kt2.databinding.ActivityMainBinding
import com.example.kt2.location.impl.LocationService

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            0
            )

        setContentView(view)

        binding.btStart.setOnClickListener {
            Intent(this, LocationService::class.java).apply {
                action = LocationService.ACTION_START
                startService(this)
                binding.tvServiceState.text = "Service Started"
            }
        }

        binding.btStop.setOnClickListener {
            Intent(this, LocationService::class.java).apply {
                action = LocationService.ACTION_STOP
                startService(this)
                binding.tvServiceState.text = "Service Stopped"

            }

        }




    }
}