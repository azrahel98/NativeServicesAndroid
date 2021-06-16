package com.example.delivery

import android.app.*
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class Notification : Service() {

    companion object{
        const val LOCATION_SERVICE = 145
        const val CHANNEL_ID = "location_channel_service"
        const val ACTION_START_SERVICE = "azrael.startService"
        const val ACTION_STOP_SERVICE = "azrael.stopService"
    }

    var intentSerice:Intent ? = null
    var intentNotif:Intent ? = null

    private fun startNotify(){
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        intentNotif = Intent(this,MainActivity::class.java)
        val pendingintent = PendingIntent.getActivity(applicationContext,0,intentNotif,PendingIntent.FLAG_UPDATE_CURRENT)
        val notifyBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)

        notifyBuilder.setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Esto es una mioerda")
            .setContentIntent(pendingintent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle()).priority = NotificationCompat.PRIORITY_MAX

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager.getNotificationChannel(CHANNEL_ID) == null){
                val channel =     NotificationChannel(CHANNEL_ID,"notificationServ",NotificationManager.IMPORTANCE_HIGH)
                channel.description = "Canal de Notification"
                notificationManager.createNotificationChannel(channel)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(LOCATION_SERVICE,notifyBuilder.build())
        }

    }
    private fun stopServ(){
        stopForeground(true)
        stopService(intentSerice)
        stopService(intentNotif)
        stopSelf()
    }



    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent!= null)
            if (intent.action != null)
                if (intent.action == ACTION_START_SERVICE){
                        intentSerice = Intent(applicationContext,MyLocation::class.java)
                        intentSerice!!.action = "AZRAEL"
                        startService(intentSerice)
                        startNotify()
                }   else if (intent.action == ACTION_STOP_SERVICE){
                        stopServ()
                }

        return START_NOT_STICKY
    }
    override fun onBind(intent: Intent): IBinder? {
       return null
    }
}