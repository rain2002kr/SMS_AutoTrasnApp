package com.example.sms_autotransappfrag.SmS_SentLog


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sms_autotrasnapp.ContactRegister_Item.Contact
import com.example.sms_autotrasnapp.ContactRegister_Item.ContactAdapter
import com.example.sms_autotrasnapp.ContactRegister_Item.ContactViewModel
import com.example.sms_autotrasnapp.G
import com.example.sms_autotrasnapp.MainActivity
import com.example.sms_autotrasnapp.R
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLog
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLogAdapter
import com.example.sms_autotrasnapp.SmS_SentLog.ContactLogViewModel
import kotlinx.android.synthetic.main.fragment_contact_regist.*
import kotlinx.android.synthetic.main.fragment_sent_log_sm.*


class SentLogSmSFragment : Fragment() {
    val TAG = "SentLogSmSFragment"
    lateinit var viewModleFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var contactLogViewModel : ContactLogViewModel
    private var id: Long? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)

    }

    fun viewModelCreate(){
        val adapter = ContactLogAdapter({ contactLog ->
            id = contactLog.id
            loadSmSMessage(contactLog)
        },
            { contactLog -> deleteDialog(contactLog)})

        val lm = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        contact_log_list.adapter = adapter
        contact_log_list.layoutManager = lm
        contact_log_list.setHasFixedSize(true)

        viewModleFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        contactLogViewModel = ViewModelProvider(this, viewModleFactory).get(ContactLogViewModel::class.java)
        contactLogViewModel.getAll().observe(requireActivity(), Observer<List<ContactLog>>{ contactLogs ->
            adapter.setContacts(contactLogs!!)
        })

    }
    private fun loadSmSMessage(contactLog: ContactLog) {
        G.message = contactLog.message.toString()
        G.receiveName = contactLog.receiveName
        G.receiveNumber = contactLog.receiveNumber
        G.transName = contactLog.transName
        G.transNumber = contactLog.transNumber
        (activity as MainActivity).changeFragment(G.Companion.Scr.SEND_FRAG.number)
    }

    private fun deleteDialog(contactLog: ContactLog) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Delete selected contact?")
            .setNegativeButton("NO") { _, _ -> }
            .setPositiveButton("YES") { _, _ ->
                contactLogViewModel.delete(contactLog)
            }
        builder.show()
    }

    fun itemInsertAndDelete(){
        //TODO insert Contact Register

        //TODO delete Contact Register
        del_item_bt.setOnClickListener({
            val contactLog  = contactLogViewModel.getAll().value.orEmpty().last()
            val contactLogs  = contactLogViewModel.getAll().value

            if(contactLogs?.lastIndex!! > 0 ) {
                contactLogViewModel.delete(contactLog)
            }
        })
        //TODO update Contact Register

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sent_log_sm, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModelCreate()
        itemInsertAndDelete()


    }


}
