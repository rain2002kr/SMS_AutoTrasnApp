package com.example.sms_autotrasnapp.SmS_Send

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sms")
class Sms (
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name =  "receiveNumber")
    var receiveNumber: String ,

    @ColumnInfo(name =  "receiveName")
    var receiveName: String ,

    @ColumnInfo(name =  "receiveMessage")
    var receiveMessage: String,

    @ColumnInfo(name =  "receiveTime")
    var receiveTime: String,

    @ColumnInfo(name =  "sendCheck")
    var sendCheck: Boolean


){
    constructor() : this(null, "","","","",false)

}