package com.example.sms_autotrasnapp.lifecycle

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log

class MySharedPreferences(context: Context) {

    val PREFS_FILENAME = "prefs"
    val PREF_KEY_MY_EDITTEXT = "myEditText"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    /* 파일 이름과 EditText 를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    fun setV(key:String, value:String?){
        prefs.edit().putString(key,value).apply()

    }
    fun getV(key:String):String?{
        return prefs.getString(key,"")
    }

    var myEditText: String?
        get() = prefs.getString(PREF_KEY_MY_EDITTEXT, "")
        set(value) = prefs.edit().putString(PREF_KEY_MY_EDITTEXT, value).apply()
    /* get/set 함수 임의 설정. get 실행 시 저장된 값을 반환하며 default 값은 ""
     * set(value) 실행 시 value로 값을 대체한 후 저장 */

}

class App : Application(), LifeCycleDelegate {
    val TAG :String = "AppStyle"
    var appInBackGround :Boolean = false
    var serviceStartBit :Boolean = false

    companion object {
        lateinit var prefs : MySharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        prefs = MySharedPreferences(applicationContext)
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