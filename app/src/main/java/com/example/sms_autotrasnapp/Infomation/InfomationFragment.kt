package com.example.sms_autotransappfrag.Infomation


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sms_autotrasnapp.R
import kotlinx.android.synthetic.main.fragment_infomation.*

/**
 * A simple [Fragment] subclass.
 */


class InfomationFragment : Fragment() {
    val TAG = "InfomationFragment"
    private val recyclerview_info_log: RecyclerView by lazy {
        view?.findViewById(R.id.recyclerview_info_log) as RecyclerView
    }
    private val list by lazy{
        mutableListOf<InfoViewModel>(
            InfoViewModel("2020-04월","개발 계획",
                  " *[문자보내기] 보낸 문자 리싸이클러 저장"

            ),
            InfoViewModel("2020-04-15","개발 이력",
                "\n *[기능] 죽지 않는 서비스 추가   "
            )

        )

    }
    private lateinit var infoAdapter : InfoViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val view =inflater.inflate(R.layout.fragment_infomation, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerview_info_log.setHasFixedSize(true) //lazy 접근
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerview_info_log.layoutManager = LinearLayoutManager(context)
        }
        val infoAdapter = InfoViewAdapter(requireContext(), R.layout.sub_infomation_log_view, list)

        recyclerview_info_log.apply {
            this.adapter = infoAdapter
            setHasFixedSize(true)
            val gridLayout = GridLayoutManager(context,1 )
            layoutManager = gridLayout
        }

    }



}
