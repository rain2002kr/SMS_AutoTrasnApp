package com.example.sms_autotrasnapp.Data

import androidx.room.PrimaryKey
import io.realm.RealmObject
import java.util.*

open class ContactData (
    @PrimaryKey
    var id :String = UUID.randomUUID().toString(),
    var receiveImage :Int =0,
    var receiveName :String ="",
    var receiveNumber :String ="",
    var transImage :Int =0,
    var transName :String ="",
    var transNumber :String =""
    ) :RealmObject()