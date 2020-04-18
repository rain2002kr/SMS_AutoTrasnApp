package com.example.sms_autotrasnapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.sms_autotransappfrag.Fragment_main.MainFragment
import com.example.sms_autotransappfrag.Infomation.InfomationFragment
import com.example.sms_autotransappfrag.SmS_Send.SendSmSFragment
import com.example.sms_autotransappfrag.SmS_SentLog.SentLogSmSFragment
import java.lang.Exception

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
            //contactLog(intent, "onNewIntent" )
            changeFragment(G.Companion.Scr.LOG_FRAG.number)
        }catch (e: Exception){e.printStackTrace()}

        super.onNewIntent(intent)
    }

    //TODO onCreate() : @SuppressLint
    //introActivity 에서 이미 체크한 위치 권한 허용 여부를 다시 체크하지 않기 위해서 함수에 annotation을 추가함.
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment,MainFragment()).commit()
    }//Out of onCreate//


    //프래그먼트 .addToBackStack(null)
    fun changeFragment(frag : Int) {
        val ft = supportFragmentManager.beginTransaction()
        when (frag) {
            G.Companion.Scr.MAIN_FRAG.number -> {
                ft.replace(R.id.fragment, MainFragment()).commit()
            }
            G.Companion.Scr.REGIST_FRAG.number -> {
                ft.replace(R.id.fragment, ContactRegistFragment()).commit()
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
