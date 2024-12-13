package com.example.trakkamap

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.io.File
import kotlin.math.floor

class Pinpointer : Service() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    companion object
    {
        var isRunning = false
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isRunning = true
        getLocation()
        startForeground(1, addNotification())
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun addNotification() : Notification{
        // create the notification
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setOngoing(true)
            .setSmallIcon(R.drawable.baseline_location_on_24)
            .setColor((Color.parseColor("#6a3de8")))
            .setContentTitle("Pinpointing service")
            .setContentText("Trakka Map is using your location to track your exploration")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel
            val channel = NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Channel Description"
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        return builder.build()
    }

    private fun getLocation() {
        // big bulky function
        // im sorry future me

        // i'm using a lot of deprecated things because the new things
        // only run in very new apis
        // fuck you google

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@Pinpointer)

        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(Priority.PRIORITY_BALANCED_POWER_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setSmallestDisplacement(0f);

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        // Opens files for each zoom level
                        val fileA = File(this@Pinpointer.filesDir, "recordsA.txt")
                        val fileB = File(this@Pinpointer.filesDir, "recordsB.txt")
                        val fileC = File(this@Pinpointer.filesDir, "recordsC.txt")
                        val fileD = File(this@Pinpointer.filesDir, "recordsD.txt")
                        val fileE = File(this@Pinpointer.filesDir, "recordsE.txt")

                        // If they don't exist, create them
                        if(!fileA.exists())
                            fileA.appendText("")

                        if(!fileB.exists())
                            fileB.appendText("")

                        if(!fileC.exists())
                            fileC.appendText("")

                        if(!fileD.exists())
                            fileD.appendText("")

                        if(!fileE.exists())
                            fileE.appendText("")

                        val exploredGeotagsA = fileA.readText().split("\n")
                        val exploredGeotagsB = fileB.readText().split("\n")
                        val exploredGeotagsC = fileC.readText().split("\n")
                        val exploredGeotagsD = fileD.readText().split("\n")
                        val exploredGeotagsE = fileE.readText().split("\n")


                        val location = locationResult.lastLocation
                        Log.i("Pinpointer", location.toString())
                        if (location != null) {
                            val A_ID = ZoomTypeA_ID(location)
                            val B_ID = ZoomTypeB_ID(location)
                            val C_ID = ZoomTypeC_ID(location)
                            val D_ID = ZoomTypeD_ID(location)
                            val E_ID = ZoomTypeE_ID(location)

                            if(exploredGeotagsA.indexOf(A_ID) == -1)
                                fileA.appendText(A_ID + "\n")

                            if(exploredGeotagsB.indexOf(B_ID) == -1)
                                fileB.appendText(B_ID + "\n")

                            if(exploredGeotagsC.indexOf(C_ID) == -1)
                                fileC.appendText(C_ID + "\n")

                            if(exploredGeotagsD.indexOf(D_ID) == -1)
                                fileD.appendText(D_ID + "\n")

                            if(exploredGeotagsE.indexOf(E_ID) == -1)
                                fileE.appendText(E_ID + "\n")

                        }
                    }
                },
                Looper.myLooper()!!
            )
        }
    }

    private fun ZoomTypeA_ID (loc : Location) : String {
        val latitude = loc.latitude
        val longitude = loc.longitude

        val coordinates : Pair<Int, Int> = Pair(floor((longitude+180)/0.009).toInt(), floor((latitude+90)/0.009).toInt())

        return coordinates.toString()
    }

    private fun ZoomTypeB_ID (loc : Location) : String {
        val latitude = loc.latitude
        val longitude = loc.longitude

        val coordinates : Pair<Int, Int> = Pair(floor((longitude+180)/0.036).toInt(), floor((latitude+90)/0.036).toInt())

        return coordinates.toString()
    }

    private fun ZoomTypeC_ID (loc : Location) : String {
        val latitude = loc.latitude
        val longitude = loc.longitude

        val coordinates : Pair<Int, Int> = Pair(floor((longitude+180)/0.36).toInt(), floor((latitude+90)/0.36).toInt())

        return coordinates.toString()
    }

    private fun ZoomTypeD_ID (loc : Location) : String {
        val latitude = loc.latitude
        val longitude = loc.longitude

        val coordinates : Pair<Int, Int> = Pair(floor((longitude+180)/3.6).toInt(), floor((latitude+90)/3.6).toInt())

        return coordinates.toString()
    }

    private fun ZoomTypeE_ID (loc : Location) : String {
        val latitude = loc.latitude
        val longitude = loc.longitude

        val coordinates : Pair<Int, Int> = Pair(floor((longitude+180)/12.0).toInt(), floor((latitude+90)/12.0).toInt())

        return coordinates.toString()
    }
}