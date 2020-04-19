package com.example.sms_autotransappfrag.SmS_Send


import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sms_autotrasnapp.*

import kotlinx.android.synthetic.main.fragment_send_sms.*
import kotlinx.android.synthetic.main.sub_send_sms.*

/**
 * A simple [Fragment] subclass.
 */
class SendSmSFragment : Fragment() {
    val TAG = "SendSmSFragment"
    private var smsList = mutableListOf<Dcontact>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_send_sms, container, false)
        return view
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // [START initialize_database_ref]
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.sub_send_sms,send_sms_view,true)
        //loadSMS()
        txtSetReceName.setText(G.transName)
        txtSetReceNumber.setText(G.transNumber)
        edSMS.setText(G.message)

        //임시 기능
        btTemp.setOnClickListener{
            txtSetReceNumber.setText("010-4697-3907")
            txtSetReceName.setText("이경훈")
            edSMS.setText("테스트메시지")
        }
        //공유
        btShare.setOnClickListener({
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, "${G.receiveName}: ${G.receiveNumber} 문자내역")
            intent.putExtra(Intent.EXTRA_TEXT, G.message)
            startActivity(intent)

        })
        //확인한 문자 로드
        btLoad.setOnClickListener({
            (activity as MainActivity).changeFragment(G.Companion.Scr.LOG_FRAG.number)
        })
        //문자 전송
        btSend.setOnClickListener({
            val number  = txtSetReceNumber.text.toString()
            val message = edSMS.text.toString()
            (activity as MainActivity).sendSMS(number , message)
        })



    }


    private fun loadSMS(){
        val contactGet = App.prefs.getDcontact()
        smsList = contactGet

        //받은 sms recyclerview_resent_log
        recyclerview_resent_log.setHasFixedSize(true) //lazy 접근
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerview_resent_log.layoutManager = LinearLayoutManager(context)
        }
        val sms_Adpater = SMS_Adapter(requireContext(), R.layout.sub_resend_log_view, smsList)


        recyclerview_resent_log.apply {
            this.adapter = sms_Adpater
            setHasFixedSize(true)
            val gridLayout = GridLayoutManager(context,1 )
            layoutManager = gridLayout
        }
    }

}
