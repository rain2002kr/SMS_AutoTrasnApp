package com.example.sms_autotrasnapp.SmS_Send

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData


class SmsViewModel (application: Application) : AndroidViewModel(application) {

    private val reposityory = SmsReposityory(application)
    private val smss = reposityory.getAll()

    fun getAll() : LiveData<List<Sms>> {
        return this.smss
    }

    fun insert(sms :Sms){
        reposityory.insert(sms)
    }

    fun delete(sms : Sms){
        reposityory.delete(sms)
    }

    fun deleteAll(){
        reposityory.deleteAll()
    }

    fun update(sms :  Sms){
        reposityory.update(sms)
    }

}