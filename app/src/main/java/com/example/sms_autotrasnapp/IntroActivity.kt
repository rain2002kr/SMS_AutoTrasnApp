package com.example.sms_autotrasnapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class IntroActivity : AppCompatActivity() {
    var handler : Handler? = null
    var runnable : Runnable? = null
    private val multiplePermissionsCode = 100
    private val requiredPermissions = arrayOf(
        Manifest.permission.SEND_SMS,
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_CONTACTS
        //Manifest.permission.READ_PHONE_STATE,ACCESS_FINE_LOCATION
        //Manifest.permission.ACCESS_COARSE_LOCATION,ACCESS_FINE_LOCATION
    )
    //TODO User 함수 : 권한 요청 함수
    private fun checkPermissions() {
        //거절되었거나 아직 수락하지 않은 권한(퍼미션)을 저장할 문자열 배열 리스트
        var rejectedPermissionList = ArrayList<String>()

        //필요한 퍼미션들을 하나씩 끄집어내서 현재 권한을 받았는지 체크
        for(permission in requiredPermissions){
            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                //만약 권한이 없다면 rejectedPermissionList에 추가
                rejectedPermissionList.add(permission)
            }
        }
        //거절된 퍼미션이 있다면...
        if(rejectedPermissionList.isNotEmpty()){
            //권한 요청!
            val array = arrayOfNulls<String>(rejectedPermissionList.size)
            ActivityCompat.requestPermissions(this, rejectedPermissionList.toArray(array), multiplePermissionsCode)
        } else {
            //권한 획득시 Main Activity 로 이동
            moveMainActvity()
        }

    }
    ////권한 요청 결과 함수
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            multiplePermissionsCode -> {
                if(grantResults.isNotEmpty()) {
                    for((i, permission) in permissions.withIndex()) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            //권한 획득 실패
                            Log.i("TAG", "The user has denied to $permission")
                            Log.i("TAG", "I can't work for you anymore then. ByeBye!")
                            Toast.makeText(this, "$permission 거부시 앱을 실행 할수 없습니다, 2초후 종료합니다.",Toast.LENGTH_LONG).show()
                            runnable = Runnable { finish() }
                            handler = Handler()
                            handler?.run {
                                postDelayed(runnable, 2000)
                            }

                        }
                    }
                }
            }
        }
    }

    fun moveMainActvity(){
        runnable = Runnable {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        }
        handler = Handler()
        handler?.run {
            postDelayed(runnable, 1000)
        }
    }

    //TODO onResume()
    override fun onResume() {
        super.onResume()
        checkPermissions()

    }
    //TODO onPause()
    override fun onPause() {
        super.onPause()
        handler?.removeCallbacks(runnable)//Acitivity 가 Pause 상태일 때는 runnable 도 중단 하도록 함.
    }

    //TODO onCreate()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        //안드로이드 앱을 띄우는 Window의 속성을 변경하여 시스템 UI를 숨기고 전체화면으로 표시하는 코드
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
}
