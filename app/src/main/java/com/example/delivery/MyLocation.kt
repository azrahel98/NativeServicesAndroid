package com.example.delivery

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*

class MyLocation : Service() {

    var intent = Intent()
    private var mqtt:mqtt ? = null
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent != null){
            if (intent.action != null){
                if (intent.action == "AZRAEL"){
                    start()
                }else{
                    stopService()
                }
            }
        }
        return START_NOT_STICKY
    }


    private fun start(){
        val request = LocationRequest.create().apply {
            interval = 3000
            fastestInterval = 1000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(request,callback,
            Looper.getMainLooper())
    }
    private  fun stopService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(callback);
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
        if(mqtt != null)
            mqtt?.disconnect()
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private val callback = object : LocationCallback(){
        override fun onLocationAvailability(p0: LocationAvailability) {
            super.onLocationAvailability(p0)
             mqtt = mqtt(applicationContext)
            mqtt!!.connect()

            Log.e("raul","sdddddddddddd")
        }
        override fun onLocationResult(p0: LocationResult) {
            super.onLocationResult(p0)
            if(p0 != null && p0.lastLocation != null){
                intent.setAction("AZRAEL")
                    .putExtra("location","${p0.lastLocation.latitude} + ${p0.lastLocation.longitude}  ").flags =
                    Intent.FLAG_FROM_BACKGROUND
                sendBroadcast(intent)
                mqtt!!.pusblishLocation("${p0.lastLocation.latitude}-${p0.lastLocation.longitude}")
            Log.e("raul","recibiendo - ------------ ${p0.lastLocation.latitude}-${p0.lastLocation.longitude}")
            }
        }
    }
}