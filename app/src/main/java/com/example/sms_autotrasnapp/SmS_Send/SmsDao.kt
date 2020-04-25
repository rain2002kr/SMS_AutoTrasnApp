package com.example.sms_autotrasnapp.SmS_Send

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SmsDao{
    @Query("SELECT * FROM sms ORDER BY id ASC")
    fun getAll() : LiveData<List<Sms>> //어디든 변경이 생기면 UPDATE 할수 있는 라이브 데이타

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sms :Sms)

    @Delete
    fun delete(sms :Sms)

    @Update
    fun update(sms :Sms)
}