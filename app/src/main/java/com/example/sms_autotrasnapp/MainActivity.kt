package com.example.sms_autotrasnapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.*
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sms_autotransappfrag.Fragment_main.MainFragment
import com.example.sms_autotransappfrag.Infomation.InfomationFragment
import com.example.sms_autotransappfrag.SmS_Send.SendSmSFragment
import com.example.sms_autotransappfrag.SmS_SentLog.SentLogSmSFragment
import com.example.sms_autotrasnapp.ContactRegister_Item.Contact
import com.example.sms_autotrasnapp.ContactRegister_Item.ContactAdapter
import com.example.sms_autotrasnapp.ContactRegister_Item.ContactRegistFragment
import com.example.sms_autotrasnapp.ContactRegister_Item.ContactViewModel
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLog
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLogAdapter
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLogViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_contact_regist.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    var backButtonCount = 0
    private val multiplePermissionsCode = 100
    private val ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 102
    private var overlayPermissionBoolen = false
    private val requiredPermissions = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_CONTACTS
        //Manifest.permission.READ_PHONE_STATE,
        //Manifest.permission.ACCESS_COARSE_LOCATION,
    )
    lateinit var viewModleFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var contactViewModel : ContactViewModel
    private lateinit var contactLogViewModel : ContactLogViewModel
    private var id: Long? = null



    //menuScreen
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return true
    }
    //menuScreen option
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val curId = item.itemId
        when(curId){
            R.id.menu_home -> changeFragment(G.Companion.Scr.MAIN_FRAG.number)
            R.id.menu_upgrade -> overlayPermission() //화면 우선순위
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onBackPressed() {
        backButtonCount ++
        if(backButtonCount < 2) {
            Toast.makeText(this, "한번더 뒤로 가기 버튼을 누르면 앱을 종료합니다.", Toast.LENGTH_LONG)
                .show()
        }
        if(backButtonCount > 1) {
               AlertDialog.Builder(this).setTitle("종료")
                .setMessage("어플을 종료하시겠습니까?")
                .setPositiveButton("Yes 버튼", DialogInterface.OnClickListener { dialog, which ->

                    //ActivityCompat.finishAffinity(this);
                    //System.runFinalizersOnExit(true);
                    //System.exit(0);

                    finish()
                })
                .setNegativeButton("No 버튼", DialogInterface.OnClickListener { dialog, which ->
                    Toast.makeText(this, "어플에 남아있습니다.", Toast.LENGTH_LONG).show()
                }).show()
            backButtonCount = 0
        }
    }

    //onNewIntent
    override fun onNewIntent(intent: Intent) {
        try {
            contactLog(intent, "onNewIntent" )
            changeFragment(G.Companion.Scr.LOG_FRAG.number)
        }catch (e: Exception){e.printStackTrace()}

        super.onNewIntent(intent)
    }

    //TODO viewModelCreate()
    fun viewModelCreate(){
        // TODO ContactView model
        // Set contactItemClick & contactItemLongClick lambda
        val adapter = ContactAdapter({ contact ->
            id = contact.id

        }, { contact ->
            deleteDialog(contact)

        })

        viewModleFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        contactViewModel = ViewModelProvider(this, viewModleFactory).get(ContactViewModel::class.java)
        contactViewModel.getAll().observe(this, Observer<List<Contact>>{contacts ->
            adapter.setContacts(contacts!!)
        })

        // TODO ContactLogView model
        // Set contactItemClick & contactItemLongClick lambda
        val adapterLog = ContactLogAdapter({ contactLog ->
            id = contactLog.id
        }, { contactLog ->
            deleteLogDialog(contactLog)
        })

        contactLogViewModel = ViewModelProvider(this, viewModleFactory).get(ContactLogViewModel::class.java)
        contactLogViewModel.getAll().observe(this, Observer<List<ContactLog>>{ contactsLog ->
            adapterLog.setContacts(contactsLog!!)
        })

        contactLog(intent, "onCreateIntent" )

    }
    //Dialog for adpter
    private fun deleteDialog(contact: Contact) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected contactLog?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contactViewModel.delete(contact)
            }
        builder.show()
    }

    //Dialog for adpter2
    private fun deleteLogDialog(contactLog: ContactLog) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Delete selected contactLog?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contactLogViewModel.delete(contactLog)
            }
        builder.show()
    }


    //TODO onCreate() : @SuppressLint
    //introActivity 에서 이미 체크한 위치 권한 허용 여부를 다시 체크하지 않기 위해서 함수에 annotation을 추가함.
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModelCreate()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment,MainFragment()).commit()


    }//Out of onCreate//

    //send SMS Function
    fun sendSMS(smsNumber:String, smsText:String) {

        val intentSent : Intent = Intent("SMS_SENT_ACTION")
        val intentDelivered : Intent = Intent("SMS_DELIVERED_ACTION")
        val sentIntent = PendingIntent.getBroadcast(this,0,intentSent,0)
        val deliveredIntent = PendingIntent.getBroadcast(this,0,intentDelivered,0)
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK ->  // 전송 성공
                    {
                        Toast.makeText(context, "전송 완료", Toast.LENGTH_SHORT).show()
                        println("전송 완료")
                        val transTime =  SimpleDateFormat("yy년 MM월 dd일 E요일 HH:mm:ss").format(Date(System.currentTimeMillis()))

                        Log.d(TAG,"전송 완료1 : ${transTime}")
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE ->  // 전송 실패
                    {
                        Toast.makeText(context, "전송 실패", Toast.LENGTH_SHORT).show()
                        println("전송 실패")
                    }
                    SmsManager.RESULT_ERROR_NO_SERVICE ->  // 서비스 지역 아님
                    {
                        Toast.makeText(context, "서비스 지역이 아닙니다", Toast.LENGTH_SHORT).show()
                        println("서비스 지역이 아닙니다")
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF ->  // 무선 꺼짐
                    {
                        Toast.makeText(context, "휴대폰이 꺼져있습니다", Toast.LENGTH_SHORT).show()
                        println("휴대폰이 꺼져있습니다")
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU ->  // PDU 실패
                    {
                        Toast.makeText(context, "PDU Null", Toast.LENGTH_SHORT).show()
                        println("PDU Null")
                    }
                }
            }
        }, IntentFilter("SMS_SENT_ACTION"))

        // SMS가 도착했을때 실행
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK ->  // 도착 완료
                    {
                        Toast.makeText(context, "SMS 도착 완료", Toast.LENGTH_SHORT).show()
                        println("SMS 도착 완료")
                        Log.d(TAG,"SMS 도착 완료")

                    }
                    Activity.RESULT_CANCELED ->  // 도착 안됨
                    {
                        Toast.makeText(context, "SMS 도착 실패", Toast.LENGTH_SHORT).show()
                        println("SMS 도착 실패")
                    }
                }
            }
        }, IntentFilter("SMS_DELIVERED_ACTION"))

        val SmsManager = SmsManager.getDefault()
        SmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent)

    }

    // get SmS function
    private fun contactLog(getintent : Intent, intentType :String) {

        val receivesender = getintent.getStringExtra(G.EXTRA_BROD_NUMBER)
        val contents = getintent.getStringExtra(G.EXTRA_BROD_CONTENTS)
        val receivedDate = getintent.getStringExtra(G.EXTRA_BROD_RECEIVED_DATE)


        Log.d(TAG,"${intentType}"
                + " sender : ${receivesender}"
                + " contents : ${contents}"
                + " receivedDate : ${receivedDate}"

        )
        val contacts  : List<Contact> ?= contactViewModel.getAll().value
        contacts?.forEach { contact -> Unit
            Log.d( TAG, "inside contacts"    )
            val receiveNumber = contact.receiveNumber.replace("-", "")
            Log.d(
                TAG, "inside contacts number ${receiveNumber}"

            )
            if (receiveNumber == receivesender) {
                val receiveName = contact.receiveName
                val receiveTime = receivedDate
                val receiveNumber = contact.receiveNumber
                val message = contents
                val transNumber = contact.transNumber
                val transName = contact.transName
                val transTime =  SimpleDateFormat("yy년 MM월 dd일 E요일 HH:mm:ss").format(Date(System.currentTimeMillis()))

                var contactlog = ContactLog(
                    id,
                    receiveName,
                    receiveTime,
                    receiveNumber,
                    message,
                    transName,
                    transTime,
                    transNumber
                )

                sendSMS(transNumber,message)

                contactLogViewModel.insert(contactlog)

                Log.d(
                    TAG, "[inside ${intentType}: ] receiveName: ${receiveName}" +
                            " receiveTime : ${receiveTime}" +
                            " receiveNumber : ${receiveNumber}" +
                            " message : ${message}" +
                            " transNumber : ${transNumber}" +
                            " transName : ${transName}" +
                            " transTime : ${contactlog.transTime}"
                )
            } else {
                Log.d(
                    TAG, "[${intentType}: ] receivesender: ${receivesender}" +
                            " contents : ${contents}" +
                            " receiveNumber : ${receiveNumber}" +
                            " receivedDate : ${receivedDate}"
                )
            }

        }
    }


    //프래그먼트 .addToBackStack(null)
    fun changeFragment(frag : Int) {
        val ft = supportFragmentManager.beginTransaction()
        when (frag) {
            G.Companion.Scr.MAIN_FRAG.number -> {
                ft.replace(R.id.fragment, MainFragment()).commit()
            }
            G.Companion.Scr.REGIST_FRAG.number -> {
                ft.replace(R.id.fragment,
                    ContactRegistFragment()
                ).commit()
            }
            G.Companion.Scr.SEND_FRAG.number -> {
                ft.replace(R.id.fragment, SendSmSFragment()).commit()
            }
            G.Companion.Scr.LOG_FRAG.number -> {
                ft.replace(R.id.fragment, SentLogSmSFragment()).commit()
            }
            G.Companion.Scr.INFO_FRAG.number -> {
                ft.replace(R.id.fragment, InfomationFragment()).commit()
            }

        }
    }


    // 화면 우선 순위 지정
    fun overlayPermission(){
        if (overlayPermissionBoolen==false){
            val overlay = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + applicationContext.getPackageName())
            )
            startActivityForResult(overlay, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE)
            overlayPermissionBoolen = true
        }
    }
}
