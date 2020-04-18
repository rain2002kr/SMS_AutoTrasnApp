package com.example.sms_autotrasnapp.Data

import androidx.lifecycle.LiveData
import io.realm.RealmChangeListener
import io.realm.RealmObject
import io.realm.RealmResults

class RealmLiveData<T: RealmObject>(private val realmResults: RealmResults<T>) : LiveData<RealmResults<T>>() {
    init {
        value = realmResults
    }
    private val listener = RealmChangeListener<RealmResults<T>>{ value = it }

    //Realm 연결
    override fun onActive() {
        super.onActive()
        realmResults.addChangeListener( listener )

    }
    //Realm 해제
    override fun onInactive() {
        super.onInactive()
        realmResults.removeChangeListener( listener )

    }
}