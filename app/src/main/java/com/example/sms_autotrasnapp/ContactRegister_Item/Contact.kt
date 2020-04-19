package com.example.sms_autotrasnapp.ContactRegister_Item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
class Contact (
    @PrimaryKey(autoGenerate = true)
    var id: Long?,

    @ColumnInfo(name =  "receiveNumber")
    var receiveNumber: String ,

    @ColumnInfo(name =  "receiveName")
    var receiveName: String,

    @ColumnInfo(name =  "transNumber")
    var transNumber: String,

    @ColumnInfo(name =  "transName")
    var transName: String


){
    //constructor() : this(null, "","",'\u0000')
    constructor() : this(null, "","","","")

}