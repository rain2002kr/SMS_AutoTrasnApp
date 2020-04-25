package com.example.sms_autotrasnapp.SmS_Send

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Sms::class], version = 2)
abstract class SmsDatabase : RoomDatabase() {

    abstract fun SmsDao() : SmsDao

    companion object {
        private var INSTANCE : SmsDatabase? = null

        fun getInstance(context : Context): SmsDatabase? {
            if(INSTANCE == null){
                synchronized(SmsDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        SmsDatabase::class.java, "sms")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

    }

}