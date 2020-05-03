package com.example.sms_autotrasnapp.ContactRegister_Item

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sms_autotrasnapp.MainActivity
import com.example.sms_autotrasnapp.R
import com.example.sms_autotrasnapp.lifecycle.App
import com.example.sms_autotrasnapp.lifecycle.App.Companion.prefs
import kotlinx.android.synthetic.main.fragment_contact_regist.*
import kotlinx.android.synthetic.main.sub_contact_register_view.*


class ContactRegistFragment : Fragment() {
    val TAG = "ContactRegistFragment"
    lateinit var viewModleFactory: ViewModelProvider.AndroidViewModelFactory
    private lateinit var contactViewModel : ContactViewModel
    private var id: Long? = null

    private val list by lazy {
        mutableListOf<Contact>(
            Contact(id,"1588-0800","국민카드","010-5687-4135","와이프"),
            Contact(id,"1899-0800","국민카드","010-5687-4135","와이프"),
            Contact(id,"1588-1688","국민카드","010-5687-4135","와이프"),
            Contact(id,"1577-6200","현대카드","010-5687-4135","와이프"),
            Contact(id,"1588-8900","삼성카드","010-5687-4135","와이프")
        )

    }
    fun viewModelCreate(){
        val adapter = (activity as MainActivity).contactAdapter

        adapter.clickOnEventListner = object:ContactAdapter.ClickOnEventListner{
            override fun onTouchEventListner(contact: Contact) {
                returnItem(contact)
            }
        }

        val lm = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL,false)
        contact_list.let{
            it.adapter = adapter
            it.layoutManager = lm
            it.setHasFixedSize(true)
        }

        viewModleFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application)
        contactViewModel = ViewModelProvider(this, viewModleFactory).get(ContactViewModel::class.java)
        contactViewModel.getAll().observe(requireActivity(), Observer<List<Contact>>{contacts ->
            adapter.setContacts(contacts!!)
        })

    }
    private fun returnItem(contact: Contact){
        txtSetReceNumber.setText(contact.receiveNumber)
        txtSetReceName.setText(contact.receiveName)
        txtSetTransNumber.setText(contact.transNumber)
        txtSetTransName.setText(contact.transName)
    }

    fun itemInsertAndDelete(){
        //TODO insert Contact Register
        bt_insert.setOnClickListener {
            val receNumber = txtSetReceNumber.text.toString()
            val receName = txtSetReceName.text.toString()
            val tranNumber = txtSetTransNumber.text.toString()
            val tranName = txtSetTransName.text.toString()

            val contact = Contact(id,receNumber,receName,tranNumber,tranName)
            contactViewModel.insert(contact)

            val contacts  = contactViewModel.getAll().value
            Log.d("sss","${contact.id.toString()} contacts size ${contacts?.size.toString()}")
        }

        //TODO delete Contact Register
        bt_delete.setOnClickListener{
            val contact  = contactViewModel.getAll().value.orEmpty().last()
            val contacts  = contactViewModel.getAll().value

            Log.d("sss","${contact.id.toString()} contacts size ${contacts?.size.toString()}")
            Log.d("sss","${contact.id.toString()} contact index ${contacts?.lastIndex}")

            if(contacts?.lastIndex!! > 0 ) {
                contactViewModel.delete(contact)
                Log.d("sss","${contact.id.toString()} contacts After size ${contacts?.size.toString()}")
            }
        }
        //TODO update Contact Register
        bt_change.setOnClickListener{
            val contacts  = contactViewModel.getAll().value
            contacts?.forEach {
                contactViewModel.delete(it)
            }
            list.forEach {
                contactViewModel.insert(it)
            }
            Toast.makeText(context,"초기화되었습니다.", Toast.LENGTH_LONG).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_contact_regist, container, false)
        inflater.inflate(R.layout.sub_contact_register_view,contact_register,true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val inflater = LayoutInflater.from(context)
        inflater.inflate(R.layout.sub_contact_register_view,contact_register,true)

        viewModelCreate()
        itemInsertAndDelete()


    }

    override fun onPause() {
        super.onPause()
        // save contact data
        val contacts  = contactViewModel.getAll().value
        prefs.contactlist = contacts as MutableList<Contact>
        prefs.contactlist.forEach {
            Log.d(TAG,it.receiveName)
        }
        val key = "RegistContactList"
        prefs.setContactList(key)

        val list = prefs.getContactList(key)
        list.forEach {
            Log.d(TAG,it.receiveName + it.receiveNumber)
        }
        Log.d(TAG,"onPause 실행")

    }
}