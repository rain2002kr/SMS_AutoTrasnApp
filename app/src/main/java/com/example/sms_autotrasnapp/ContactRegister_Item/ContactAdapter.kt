package com.example.sms_autotrasnapp.ContactRegister_Item

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sms_autotrasnapp.R


class ContactAdapter(val contactItemClick: (Contact) -> Unit, val contactItemLongClick: (Contact) -> Unit)
    : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    private var contacts: List<Contact> = listOf()

    interface ClickOnEventListner {
        fun onTouchEventListner(contact: Contact)
    }
    lateinit var clickOnEventListner : ClickOnEventListner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_contact_list_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    override fun onBindViewHolder(viewHolder:ViewHolder , position: Int) {
        viewHolder.bind(contacts[position])
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val imgReceiver = itemView.findViewById<ImageView>(R.id.imgReceiver)
        private val imgRotate = itemView.findViewById<ImageView>(R.id.imgRotate)
        private val imgTransfer = itemView.findViewById<ImageView>(R.id.imgTransfer)

        private val txtReceNumber = itemView.findViewById<TextView>(R.id.txtReceNumber)
        private val txtReceName = itemView.findViewById<TextView>(R.id.txtReceName)
        private val txtTransNumber = itemView.findViewById<TextView>(R.id.txtTransNumber)
        private val txtTransName = itemView.findViewById<TextView>(R.id.txtTransName)

        fun bind(contact: Contact) {
            imgReceiver.setImageResource(R.drawable.robot)
            imgRotate.setImageResource(R.drawable.exchange)
            imgTransfer.setImageResource(R.drawable.robot_2)

            txtReceNumber.text = contact.receiveNumber
            txtReceName.text = contact.receiveName
            txtTransNumber.text = contact.transNumber
            txtTransName.text = contact.transName

            itemView.setOnClickListener {
                contactItemClick(contact)
                clickOnEventListner.onTouchEventListner(contact)
            }

            itemView.setOnLongClickListener {
                contactItemLongClick(contact)
                true
            }
        }
    }

    fun setContacts(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }
}