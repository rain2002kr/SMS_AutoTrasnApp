package com.example.sms_autotrasnapp.Data

import io.realm.Realm
import io.realm.RealmResults

class ContactDao (private val realm: Realm){
    fun getAllContacts() :RealmResults<ContactData> {
        return realm.where(ContactData::class.java)
            .findAll()
    }
    fun selectContacts(receiveNumber : String):ContactData {
        return realm.where(ContactData::class.java)
            .equalTo("receiveNumber",receiveNumber)
            .findFirst() as ContactData
    }

    fun addOrUpdateContact(contactData : ContactData){
        //쿼리가 끝날때까지 DB를 안전하게 사용 가능
        realm.executeTransaction{
            //매니지드 상태가 아닌경우,Realm 함수로 DB 추가
            if(!contactData.isManaged){
                it.copyToRealmOrUpdate(contactData)
            }
        }
    }


}