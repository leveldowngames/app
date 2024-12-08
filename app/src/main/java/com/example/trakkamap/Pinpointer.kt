package com.example.trakkamap

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.os.IBinder
import android.os.Looper
import android.os.Message
import android.os.Process
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import kotlin.math.exp
import kotlin.math.floor


class Pinpointer : Service()
{
    private var serviceLooper: Looper? = null
    private var serviceHandler: ServiceHandler? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    // Handler that receives messages from the thread
    private inner class ServiceHandler(looper: Looper) : Handler(looper) {

        @SuppressLint("MissingPermission")
        override fun handleMessage(msg: Message) {
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

            try {
                // Pinpointer loop
                while(true)
                {
                    val exploredGeotagsA = fileA.readText().split("\n")
                    val exploredGeotagsB = fileB.readText().split("\n")
                    val exploredGeotagsC = fileC.readText().split("\n")
                    val exploredGeotagsD = fileD.readText().split("\n")
                    val exploredGeotagsE = fileE.readText().split("\n")

                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this@Pinpointer)
                    fusedLocationClient.lastLocation
                        .addOnSuccessListener { location : Location? ->
                            if(location != null)
                            {
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

                                Log.i("pinpointer", "appended geotags file")

                            }
                        }

                    Thread.sleep(10000)

                }

            } catch (e: InterruptedException) {
                // Restore interrupt status.
                Thread.currentThread().interrupt()
            }

            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1)
        }
    }

    private fun addNotification() {
        // create the notification
        val builder = NotificationCompat.Builder(this, "channel_id")
            .setSmallIcon(R.drawable.baseline_location_on_24)
            .setColor((Color.parseColor("#6a3de8")))
            .setContentTitle("Pinpointing service")
            .setContentText("Trakka Map is using your location to track your exploration")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        // create the pending intent and add to the notification
        val intent = Intent(this, Pinpointer::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        builder.setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel
            val channel = NotificationChannel("channel_id", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Channel Description"
            // Register the channel with the system
            notificationManager.createNotificationChannel(channel)
        }

        // send the notification
        notificationManager.notify(1, builder.build())
    }

    override fun onCreate() {
        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND).apply {
            start()

            // Get the HandlerThread's Looper and use it for our Handler
            serviceLooper = looper
            serviceHandler = ServiceHandler(looper)
        }

        addNotification()
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show()

        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        serviceHandler?.obtainMessage()?.also { msg ->
            msg.arg1 = startId
            serviceHandler?.sendMessage(msg)
        }

        // If we get killed, after returning from here, restart
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        // We don't provide binding, so return null
        return null
    }

    override fun onDestroy() {
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show()
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
