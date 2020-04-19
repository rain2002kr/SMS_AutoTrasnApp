package com.example.sms_autotrasnapp.ContactRegister_Item

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao{
    @Query("SELECT * FROM contact ORDER BY id ASC")
    fun getAll() : LiveData<List<Contact>> //어디든 변경이 생기면 UPDATE 할수 있는 라이브 데이타

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(contact :Contact)

    @Delete
    fun delete(contact :Contact)

    @Update
    fun update(contact :Contact)
}