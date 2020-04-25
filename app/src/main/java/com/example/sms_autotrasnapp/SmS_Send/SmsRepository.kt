package com.example.sms_autotrasnapp.SmS_Send

import android.app.Application
import androidx.lifecycle.LiveData
import java.lang.Exception


class SmsReposityory (application: Application) {

    private val smsDatabase = SmsDatabase.getInstance(application)!!
    private val smsDao : SmsDao = smsDatabase.SmsDao()
    private val smss : LiveData<List<Sms>> = smsDao.getAll()

    fun getAll() : LiveData<List<Sms>> {
        return smss
    }

    fun insert(sms : Sms){
        try {
            val thread = Thread(Runnable {
                smsDao.insert(sms)
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}
    }

    fun delete(sms: Sms) {
        try {
            val thread = Thread(Runnable {
                smsDao.delete(sms)
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}

    }
    fun deleteAll() {
        try {
            val thread = Thread(Runnable {
                val smss =smsDao.getAll().value
                smss?.forEach {
                    smsDao.delete(it)
                }
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}

    }
    fun update(sms: Sms) {
        try {
            val thread = Thread(Runnable {
                smsDao.update(sms)
            })
            thread.start()
        } catch (e : Exception){e.printStackTrace()}

    }
}