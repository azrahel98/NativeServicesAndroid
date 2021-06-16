package com.example.delivery

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.eclipse.paho.client.mqttv3.MqttException
import java.lang.Exception

class Channels(private var activity: Activity, flutterEngine: FlutterEngine?) :
    MethodChannel.MethodCallHandler, EventChannel.StreamHandler, BroadcastReceiver() {

    private val messengue = flutterEngine!!.dartExecutor.binaryMessenger
    var channel = MethodChannel(messengue,"azrael.com/method")
    var eventChannel = EventChannel(messengue,"azrael.com/listener")
    private var events:EventChannel.EventSink? = null
    var isRegister:Boolean = false
    init {
        channel.setMethodCallHandler(this)
        eventChannel.setStreamHandler(this)
    }



    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method){
            "start" -> startListen(result)
            "stop" -> stopListen(result)
            else -> result.notImplemented()
        }
    }



    private fun startListen(result: MethodChannel.Result){
        val intent = Intent(activity,Notification::class.java)
        intent.action = Notification.ACTION_START_SERVICE
        activity.registerReceiver(this, IntentFilter("AZRAEL"))
        isRegister = true
        activity.startService(intent)
        result.success("starting")
    }


    private fun stopListen(result: MethodChannel.Result){
        val intent = Intent(activity,Notification::class.java)
        intent.action = Notification.ACTION_STOP_SERVICE
        val locaintent = Intent(activity,MyLocation::class.java)
        locaintent.action = "APAGATE"
        if (isRegister)
            activity.unregisterReceiver(this)
        isRegister = false
        activity.stopService(locaintent)
        activity.stopService(intent)
        result.success("starting")
    }










    override fun onListen(arguments: Any?, events: EventChannel.EventSink?){
        this.events = events!!
    }

    override fun onCancel(arguments: Any?) {
        this.events = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        val location = intent?.extras?.get("location")
        if (this.events != null){
            events?.success(location)
        }
    }

}