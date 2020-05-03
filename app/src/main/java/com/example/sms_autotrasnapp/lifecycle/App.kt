package com.example.sms_autotrasnapp.lifecycle

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import com.example.sms_autotrasnapp.ContactRegister_Item.Contact
import com.example.sms_autotrasnapp.SmS_Send.Sms
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLog
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.realm.Realm.init

class MySharedPreferences(context: Context) {
    var gson :Gson = GsonBuilder().create()
    var contactType :TypeToken<MutableList<Contact>> = object :TypeToken<MutableList<Contact>>() {}
    var contactLogType :TypeToken<MutableList<ContactLog>> = object :TypeToken<MutableList<ContactLog>>() {}
    var smsLogType :TypeToken<MutableList<Sms>> = object :TypeToken<MutableList<Sms>>() {}
    var js :String = ""
    var gsContact = mutableListOf<Contact>()
    var gsContactLog = mutableListOf<ContactLog>()
    var gsSmsLog = mutableListOf<Sms>()


    val PREFS_FILENAME = "prefs"
    val PREF_KEY_MY_EDITTEXT = "myEditText"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)
    var contactlist = mutableListOf<Contact>()
    var contactLogList = mutableListOf<ContactLog>()
    var smsLogList = mutableListOf<Sms>()


    /* 파일 이름과 EditText 를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */

    fun setContact(key:String, value: Contact){
        val list = mutableListOf<String>()
        list.add(value.receiveName)
        list.add(value.receiveNumber)
        prefs.edit().putString(key,list.toString()).apply()
    }
    // 모든 받은 문자 정보 저장
    fun setSmsLogList(key:String){
        js = gson.toJson(smsLogList,contactType.type)
        setV(key,js)
    }
    fun getSmsList(key:String):MutableList<Sms>{
        js = getV(key)!!
        if(!js.isNullOrEmpty()) {
            gsSmsLog = gson?.fromJson(js, smsLogType.type)
            return gsSmsLog
        }
        return smsLogList
    }


    // 연락처 정보 저장
    fun setContactList(key:String){
        js = gson.toJson(contactlist,contactType.type)
        setV(key,js)
    }
    fun getContactList(key:String):MutableList<Contact>{
        js = getV(key)!!
        if(!js.isNullOrEmpty()) {
            gsContact = gson?.fromJson(js, contactType.type)
            return gsContact
        }
        return contactlist
    }

    // 보낸 문자 정보 저장
    fun setContactLogList(key:String){
        js = gson.toJson(contactLogList,contactLogType.type)
        setV(key,js)
    }
    fun getContactLogList(key:String):MutableList<ContactLog>{
        js = getV(key)!!
        if(!js.isNullOrEmpty()) {
            gsContactLog = gson?.fromJson(js, contactLogType.type)
            return gsContactLog
        }
        return contactLogList
    }


    fun setV(key:String, value:String?){
        prefs.edit().putString(key,value).apply()
    }
    fun getV(key:String):String?{
        return prefs.getString(key,"")
    }

    fun setIndex(key:String, value:Int){
        prefs.edit().putInt(key,value).apply()
    }

    fun getIndex(key: String):Int{
        return prefs.getInt(key,0)
    }

    fun deleteAt(key: String){
        prefs.edit().remove("key").apply()
    }
    fun deleteAll(){
        prefs.edit().clear().apply()
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