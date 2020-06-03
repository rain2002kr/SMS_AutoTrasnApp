package com.example.sms_autotransappfrag.SmS_SentLog


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sms_autotrasnapp.G
import com.example.sms_autotrasnapp.MainActivity
import com.example.sms_autotrasnapp.R
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLog
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLogAdapter
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLogViewModel
import com.example.sms_autotrasnapp.lifecycle.App
import kotlinx.android.synthetic.main.fragment_sent_log_sm.*


class SentLogSmSFragment : Fragment() {
    val TAG = "SentLogSmSFragment"
    lateinit var viewModleFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var contactLogViewModel : ContactLogViewModel
    private var id: Long? = null

    private fun viewModelCreate(){
        val adapter = (activity as MainActivity).contactLogAdapter
        adapter.clickOnEventListner = object :ContactLogAdapter.ClickOnEventListner{
            override fun onTouchEventListner(contactLog: ContactLog) {
               loadContactLog(contactLog)
            }
        }

        val lm = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        contact_log_list.let{
            it.adapter = adapter
            lm.stackFromEnd = true
            lm.reverseLayout = true
            it.layoutManager = lm
            it.setHasFixedSize(true)
        }

        viewModleFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        contactLogViewModel = ViewModelProvider(this, viewModleFactory).get(ContactLogViewModel::class.java)
        contactLogViewModel.getAll().observe(requireActivity(), Observer<List<ContactLog>>{ contactLogs ->
            adapter.setContacts(contactLogs!!)
        })

    }


    private fun loadContactLog(contactLog: ContactLog) {
        G.message = contactLog.message.toString()
        G.receiveName = contactLog.receiveName
        G.receiveNumber = contactLog.receiveNumber
        G.transName = contactLog.transName
        G.transNumber = contactLog.transNumber
        (activity as MainActivity).changeFragment(G.Companion.Scr.SEND_FRAG.number)
    }


    fun itemInsertAndDelete(){
        //TODO insert Contact Register

        //TODO delete Contact Register
        del_item_bt.setOnClickListener{
            val contactLog  = contactLogViewModel.getAll().value.orEmpty().last()
            val contactLogs  = contactLogViewModel.getAll().value
            Log.d(TAG,"${contactLog.id.toString()} contacts size ${contactLogs?.size.toString()}")
            Log.d(TAG,"${contactLog.id.toString()} contact index ${contactLogs?.lastIndex}")

            if(contactLogs?.lastIndex!! > 0 ) {
                contactLogViewModel.delete(contactLog)
                Log.d(TAG,"${contactLog.id.toString()} contacts After size ${contactLogs?.size.toString()}")
            }
        }
        //TODO update Contact Register

        // TODO Load contactlist from sharedPre
        // sharedPref 로 부터 저장 연락처 불러오기

        bt_scr_rawSms.setOnClickListener{
            val listTest=(activity as MainActivity).callContactRegistedListFromPreference()
            listTest.forEach {
                Log.d(TAG,it.receiveName +it.receiveNumber)
            }
        }

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return  inflater.inflate(R.layout.fragment_sent_log_sm, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelCreate()
        itemInsertAndDelete()
    }




}
