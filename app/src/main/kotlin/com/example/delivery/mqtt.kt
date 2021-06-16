package com.example.delivery

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

class mqtt(context: Context) {
        companion object {
            const val TAG = "MqttClientHelper"
        }

        private var mqttclient: MqttAndroidClient =
            MqttAndroidClient(context,"tcp://35.247.198.174:1883","azrae")


    init {
        mqttclient.setCallback( object : MqttCallbackExtended {
            override fun connectionLost(cause: Throwable?) {
                Log.e("raul","PERIDIENDO")
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                Log.e("raul ","arrived")
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                Log.e("raul ","complete")
            }

            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                Log.e("raul ","connected")
            }

        })
    }

        fun connect(){
            val options = MqttConnectOptions()
            try {
                mqttclient.connect(options, null, object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.d(TAG, "Connection success")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.d(TAG, "Connection failure")
                    }
                })

            } catch (e: MqttException) {
                Log.e("raul",e.printStackTrace().toString())
            }
        }

        fun disconnect(){
                mqttclient.disconnect()
        }

        fun pusblishLocation (location:String){
            try {
                val message = MqttMessage()
                message.payload = location.toByteArray()
                message.qos = 1
                message.isRetained = true

                mqttclient.publish("ubicacion",message,null,object : IMqttActionListener{
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        Log.e("raul","se ah enviado correctamente")
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        Log.e("raul","Fallo el envio")
                    }

                })
            }catch (e:MqttException){
                Log.e("raul","faloo desde el cathc")
                Log.e("raul",e.toString())
            }
        }

}