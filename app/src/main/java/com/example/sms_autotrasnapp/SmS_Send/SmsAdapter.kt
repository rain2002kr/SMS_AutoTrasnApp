package com.example.sms_autotrasnapp.SmS_Send

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sms_autotrasnapp.R


class SmsAdapter(val contactItemClick: (Sms) -> Unit, val contactItemLongClick: (Sms) -> Unit)
    : RecyclerView.Adapter<SmsAdapter.ViewHolder>() {

    private var smss: List<Sms> = listOf()
    interface ClickOnEventListner {
        fun onTouchEventListner(message: String)
    }
    lateinit var clickOnEventListner : ClickOnEventListner

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.sub_sms_log_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return smss.size
    }

    override fun onBindViewHolder(viewHolder:ViewHolder , position: Int) {
        viewHolder.bind(smss[position])
    }


    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        private val txtSmsReceiveName = itemView.findViewById<TextView>(R.id.txtSmsReceiveName)
        private val txtSmsReceiveNumber = itemView.findViewById<TextView>(R.id.txtSmsReceiveNumber)
        private val txtSmsReceiveTime = itemView.findViewById<TextView>(R.id.txtSmsReceiveTime)
        private val txtSmsMessage = itemView.findViewById<TextView>(R.id.txtSmsMessage)


        fun bind(sms: Sms) {
            txtSmsReceiveName.setText(sms.receiveName)
            txtSmsReceiveNumber.setText(sms.receiveNumber)
            txtSmsReceiveTime.setText(sms.receiveTime)
            txtSmsMessage.setText(sms.receiveMessage)


            itemView.setOnClickListener {
                contactItemClick(sms)
                clickOnEventListner.onTouchEventListner(sms.receiveMessage)
            }

            itemView.setOnLongClickListener {
                contactItemLongClick(sms)
                true
            }
        }
    }

    fun setSmss(smss: List<Sms>) {
        this.smss = smss
        notifyDataSetChanged()
    }
}