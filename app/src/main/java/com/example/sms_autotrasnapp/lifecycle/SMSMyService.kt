package com.example.sms_autotrasnapp.lifecycle

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.sms_autotrasnapp.IntroActivity
import com.example.sms_autotrasnapp.MainActivity

class SMSMyService : Service() {
    val TAG : String ="SMSMyService"
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand()")
        if(intent == null){
            return Service.START_STICKY
        } else {
            processCmd(intent)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")

    }

    fun processCmd(intent:Intent){
        val command = intent.getStringExtra("command")
        val name = intent.getStringExtra("name")
        Log.d(TAG, "processCmd(), $command, $name")
        //5초 지연후, MainActivity 띄워 줍니다.
        if(command == "restart") {
            Thread.sleep(100)
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
            Log.d(TAG, "call mainActivity()")
            startActivity(intent)
        }
    }
}