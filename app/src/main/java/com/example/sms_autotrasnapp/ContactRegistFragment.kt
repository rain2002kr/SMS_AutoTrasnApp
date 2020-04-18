package com.example.sms_autotrasnapp

import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_contact_regist.*
import kotlin.reflect.KParameter

class ContactRegistFragment : Fragment() {
    val TAG = "ContactRegistFragment"

    fun callContact(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        startActivityForResult(intent,0)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_regist, container, false)
        inflater.inflate(R.layout.sub_contact_register_view,contact_register,true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.sub_contact_register_view,contact_register,true)

    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == RESULT_OK){

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}

