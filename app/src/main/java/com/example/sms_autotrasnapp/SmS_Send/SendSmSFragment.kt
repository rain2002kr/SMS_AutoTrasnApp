package com.example.sms_autotransappfrag.SmS_Send


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sms_autotrasnapp.*
import com.example.sms_autotrasnapp.ContactRegister_Item.Contact
import com.example.sms_autotrasnapp.SmS_Send.Sms
import com.example.sms_autotrasnapp.SmS_Send.SmsAdapter
import com.example.sms_autotrasnapp.SmS_Send.SmsViewModel
import com.example.sms_autotrasnapp.lifecycle.App
import com.example.sms_autotrasnapp.lifecycle.App.Companion.prefs
import kotlinx.android.synthetic.main.fragment_send_sms.*
import kotlinx.android.synthetic.main.sub_send_sms.*

/**
 * A simple [Fragment] subclass.
 */
class SendSmSFragment : Fragment() {
    val TAG = "SendSmSFragment"
    lateinit var viewModleFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var smsViewModel : SmsViewModel
    private var id: Long? = null


    fun viewModelCreate(){
        val smsAdapter = (activity as MainActivity).smsAdapter
        smsAdapter.clickOnEventListner = object :SmsAdapter.ClickOnEventListner{
            override fun onTouchEventListner(message: String) {
                edSMS.setText(message)
            }
        }
        val lm = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        sms_log_listview.let{
            it.adapter = smsAdapter
            lm.reverseLayout = true
            lm.stackFromEnd = true
            it.layoutManager = lm
            it.setHasFixedSize(true)
        }
        viewModleFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        smsViewModel=ViewModelProvider(this,viewModleFactory).get(SmsViewModel::class.java)
        smsViewModel.getAll().observe(requireActivity(),Observer<List<Sms>>{smss->
            smsAdapter.setSmss(smss!!)
        })
        // TODO ContactView model
    }

    fun viewModelCreate2(){

    }

    //TODO saveGetSmsMessage 메시지 추가
    fun saveGetSmsMessage(){
        //val listSms = smsViewModel.getAll().value

        val sender = App.prefs.getV("sender")
        val contents = App.prefs.getV("contents")
        val receivedDate = App.prefs.getV("receivedDate")
        val saveLastMessage = App.prefs.getV("saveLastMessage")

        val smsLoglist = prefs.getSmsList("smsLog")
        smsViewModel.deleteAll()
        smsLoglist.forEach {
            smsViewModel.insert(it)
            Log.d(TAG,it.receiveName+it.receiveMessage)
        }


/*
        if(saveLastMessage != receivedDate.toString()){
            val sms = Sms(id, sender.toString(),"",contents.toString(),receivedDate.toString(),true)
            smsViewModel.insert(sms)
            prefs.setV("saveLastMessage", receivedDate.toString())

        } else {
            Log.d(TAG, "message 같음 : ${receivedDate.toString()}")
        }


        var num = prefs.getV("contactMaxNum")
        val testlist = mutableListOf<String>()
        val contactlist = mutableListOf<String>()

        for(i in 1..num!!.toInt()){
            val contact = prefs.getV("contactlist${i.toString()}")
            Log.d(TAG, i.toString())
            Log.d(TAG,"0 : ${contact!!.toString()}")
            contact.split(" ").filter{
                testlist.add(it)
                it.length > 2
            }
            for(i in contactlist.size-1 downTo 0){
                contactlist.removeAt(i)
            }
            testlist.forEach {
                 contactlist.add(replaceFiltered(it))
            }
        }

        contactlist.forEach {
            Log.d(TAG,"tellist ${it}")
        }

 */

    }

    fun saveRawSmsMessage(){
        // save raw sms
        var index =App.prefs.getIndex("index")
        var listGs = listOf<String>()
        for(i in 0..index){
            var message =prefs.getV("raw${i.toString()}")
            Log.d(TAG,"index :${index} i: ${i} : ${message}")
        }
    }


    fun replaceFiltered(value:String):String{
        return value.replace(",","")
            .replace("[","")
            .replace("]","")
            .replace(" ","")

    }
    fun replaceBefore(name:String):String{
        return name.replaceAfter(",","")
            .replace("[","")
            .replace(",","")
    }
    fun replaceAfter(number:String):String{
        return number.replaceBefore(",","")
            .replace(",","")
            .replace("]","")
            .replace(" ","")
    }

    fun itemInsertAndDelete() {
        //TODO SMS 삭제
        imageView3.setOnClickListener{
            val sms = smsViewModel.getAll().value
            sms?.forEach {
                smsViewModel.delete(it)
            }
        }

        //TODO 임시기능
        btTemp.setOnClickListener{
            Log.d(TAG, "임시 버튼 눌림.")

            //prefs.deleteAll()

            edSetReceNumber.setText("010-4697-3907")
            edSetReceName.setText("이경훈")
            edSMS.setText("테스트메시지")
           // saveGetSmsMessage()
        }

        //TODO 지정번호로드
        bt_selectNumber.setOnClickListener{
            edSetReceNumber.setText("010-5687-4135")
            edSetReceName.setText("와이프")
        }
        //TODO 공유
        btShare.setOnClickListener{
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "${G.receiveName}: ${G.receiveNumber} 문자내역")
            intent.putExtra(Intent.EXTRA_TEXT, G.message)
            startActivity(intent)

        }
        //TODO 확인한 문자 로드
        btLoad.setOnClickListener{
            (activity as MainActivity).changeFragment(G.Companion.Scr.LOG_FRAG.number)
        }
        //TODO 문자 전송
        btSend.setOnClickListener{
            val number  = edSetReceNumber.text.toString()
            val message = edSMS.text.toString()

            if(number.count() > 1  && message.count() > 1  ) {
                (activity as MainActivity).sendSMS(number, message)
            }
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_send_sms, container, false)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.sub_send_sms,send_sms_view,true)

        viewModelCreate()
        saveGetSmsMessage()
        itemInsertAndDelete()
     //   saveRawSmsMessage()

    }

}
