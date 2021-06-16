package com.example.delivery

import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine

class MainActivity: FlutterActivity() {

    lateinit var x:Channels

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        x = Channels(this,flutterEngine)
    }

    override fun onDestroy() {
        if (x.isRegister){
            unregisterReceiver(x)
        }
        super.onDestroy()
    }
}
