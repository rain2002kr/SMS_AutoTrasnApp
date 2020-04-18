package com.example.sms_autotrasnapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

data class Dcontact(val name:String,val number:String ,val sms:String="",val time:String="")
data class TelList(val name:String, val number:String)
data class TelTransList(val name:String, val number:String, val tName:String, val tNumber:String)

class MySharedPreferences(context: Context) {
    var gson = GsonBuilder().create()
    var listType : TypeToken<MutableList<Dcontact>> = object : TypeToken<MutableList<Dcontact>>() {}
    var listType2 : TypeToken<MutableList<TelList>> = object : TypeToken<MutableList<TelList>>() {}
    var listType3 : TypeToken<MutableList<TelTransList>> = object : TypeToken<MutableList<TelTransList>>() {}

    companion object {
        private var dcontacts : MutableList<Dcontact> = mutableListOf()
        private var tellists : MutableList<TelList> = mutableListOf()
        private var teltranslists : MutableList<TelTransList> = mutableListOf()
    }
    val PREFS_FILENAME = "prefs"
    val PREF_KEY_MY_EDITTEXT = "myEditText"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)

    /* 파일 이름과 EditText 를 저장할 Key 값을 만들고 prefs 인스턴스 초기화 */
//Put Tellists overloading
    fun putTellist(tellist : TelList){
        //get js from tellist
        val key = "tellists"
        val gsonCon = prefs.getString(key,"")
        if(null != gson.fromJson(gsonCon,listType2.type)){tellists = gson.fromJson(gsonCon,listType2.type)}
        tellists.add(tellist)
        var jsonCon = gson.toJson(tellists,listType2.type)
        prefs.edit().putString(key,jsonCon).apply()
    }
    fun getTellist():MutableList<TelList>{
        val key = "tellists"
        val gsonCon = prefs.getString(key,"")
        if(null != gson.fromJson(gsonCon,listType2.type)){tellists = gson.fromJson(gsonCon,listType2.type)}
        return tellists
    }
    //텔 리스트
    //Put TelTranslists overloading
    fun putTelTranslist(tellist : TelTransList){
        //get js from teltranslists
        val key = "teltranslists"
        val gsonCon = prefs.getString(key,"")
        if(null != gson.fromJson(gsonCon,listType3.type)){teltranslists = gson.fromJson(gsonCon,listType3.type)}
        teltranslists.add(tellist)
        var jsonCon = gson.toJson(teltranslists,listType3.type)
        prefs.edit().putString(key,jsonCon).apply()
    }
    fun getTelTranslist():MutableList<TelTransList>{
        val key = "teltranslists"
        val gsonCon = prefs.getString(key,"")
        if(null != gson.fromJson(gsonCon,listType3.type)){teltranslists = gson.fromJson(gsonCon,listType3.type)}
        return teltranslists
    }


//put SMS Dcontacts ovloading
    fun putDcontact(number: String,sms: String,time: String){

        var message = Dcontact("none",number,sms,time)
        val keytel = "tellists"
        val gsonConT = prefs.getString(keytel,"")
        if(null != gson.fromJson(gsonConT,listType2.type)){tellists = gson.fromJson(gsonConT,listType2.type)}

        //Check tellist
        tellists.forEach{
            var tempNumber = it.number.replace("-","")
            if(number == tempNumber){
                message = Dcontact(it.name,it.number,sms,time)
            }else{
                message = Dcontact("none",number,sms,time)
            }
        }
        val key = "receSms"
        val gsonCon = prefs.getString(key,"")
        if(null != gson.fromJson(gsonCon,listType.type)){dcontacts = gson.fromJson(gsonCon,listType.type)}
        dcontacts.add(message)
        var jsonCon = gson.toJson(dcontacts,listType.type)
        prefs.edit().putString(key,jsonCon).apply()

    }
    fun getDcontact():MutableList<Dcontact>{
        val key = "receSms"
        val gsonCon = prefs.getString(key,"")
        if(null != gson.fromJson(gsonCon,listType.type)){dcontacts = gson.fromJson(gsonCon,listType.type)}
        return dcontacts
    }


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

class App : Application() {

    companion object {
        lateinit var prefs : MySharedPreferences
    }
    /* prefs라는 이름의 MySharedPreferences 하나만 생성할 수 있도록 설정. */

    override fun onCreate() {
        prefs = MySharedPreferences(applicationContext)
        super.onCreate()
    }
}