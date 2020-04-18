package com.example.sms_autotrasnapp.lifecycle

import android.app.Application
import android.content.Intent
import android.util.Log

class App : Application(), LifeCycleDelegate {
    val TAG :String = "AppStyle"
    var appInBackGround :Boolean = false
    var serviceStartBit :Boolean = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)
        if(serviceStartBit == false) {
            val intent = Intent(applicationContext, SMSMyService::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
            intent.putExtra("command", "start")
            startService(intent)
            serviceStartBit = true
        }
    }

    override fun onAppBackgrounded() {
        Log.d(TAG, "App in background")
        appInBackGround = true
    }

    override fun onAppForegrounded() {
        Log.d(TAG, "App in foreground")
        appInBackGround = false
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy appInBackGround:${appInBackGround}")

        if(appInBackGround == true) {
            val intent = Intent(applicationContext, SMSMyService::class.java)
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP
            )
            intent.putExtra("command", "restart")
            startService(intent)
        }
    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }
}