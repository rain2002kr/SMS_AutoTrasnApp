package com.example.sms_autotrasnapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.sub_main_view.view.*
import kotlinx.android.synthetic.main.sub_resend_log_view.view.*

//<!-- MainViewModel -->
class MainViewModel (val imageId : Int, val subject:String, val subject2:String )
class MainViewHolder (override val containerView: View) : RecyclerView.ViewHolder(containerView),LayoutContainer
class MainViewAdapter (val context: Context, val intId:Int, val list:List<MainViewModel>): RecyclerView.Adapter<MainViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(intId,parent,false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface ItemClickListener{
        fun onClick(view : View, position:Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener : ItemClickListener){
        this.itemClickListener=itemClickListener
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.containerView.imageView.setImageResource(list[position].imageId)
        holder.containerView.textView.text=list[position].subject
        holder.containerView.textView2.text=list[position].subject2

        holder.containerView.setOnClickListener({
            itemClickListener.onClick(it,position)
        })


    }
}

//<!-- SMS Adapter -->
class SMS_Adapter (val context: Context, val intId:Int, val list:List<Dcontact>): RecyclerView.Adapter<MainViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(intId,parent,false)
        return MainViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface ItemClickListener{
        fun onClick(view : View, position:Int)
    }

    private lateinit var itemClickListener : ItemClickListener

    fun setItemClickListener(itemClickListener : ItemClickListener){
        this.itemClickListener=itemClickListener
    }


    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.containerView.txtlogReceName.text = list[position].name
        holder.containerView.txtlogReceNumber.text = list[position].number
        holder.containerView.txtlogMessage.text = list[position].sms
        holder.containerView.txtlogReceTime.text = list[position].time

        holder.containerView.setOnClickListener({
            itemClickListener.onClick(it,position)
        })


    }
}

class G {

    companion object {
        enum class Scr(val number:Int){
            REGIST_FRAG(1),
            LOG_FRAG(2),
            SEND_FRAG(3),
            INFO_FRAG(4),
            MAIN_FRAG(99)
        }
    }
}