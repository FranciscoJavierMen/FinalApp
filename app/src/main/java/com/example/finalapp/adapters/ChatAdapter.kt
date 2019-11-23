package com.example.finalapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapp.R
import com.example.finalapp.models.Messages
import com.example.finalapp.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_chat_left.view.*
import kotlinx.android.synthetic.main.item_chat_right.view.*
import java.text.SimpleDateFormat

class ChatAdapter (val items: List<Messages>, val userId: String): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.item_chat_right
    private val layoutLeft = R.layout.item_chat_left

    override fun getItemViewType(position: Int) =
        if (items[position].authorId == userId) MY_MESSAGE else GLOBAL_MESSAGE

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MY_MESSAGE -> ViewHolderR(LayoutInflater.from(parent.context).inflate(layoutRight, parent, false))
            else -> ViewHolderL(LayoutInflater.from(parent.context).inflate(layoutLeft, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            MY_MESSAGE-> (holder as ViewHolderR).bind(items[position])
            GLOBAL_MESSAGE -> (holder as ViewHolderL).bind(items[position])
        }
    }

    class ViewHolderR(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message: Messages) = with(itemView){
            txtMessageRight.text = message.message
            txtTimeRight.text = SimpleDateFormat("hh:mm").format(message.sentAt)
            //Cargar imagen con picasso
            if(message.profileImageURL.isEmpty()){
                Picasso.get().load(R.drawable.ic_person)
                    .resize(100, 100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imgProfileRight)
            }else {
                Picasso.get().load(message.profileImageURL)
                    .resize(100, 100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imgProfileRight)
            }
        }
    }

    class ViewHolderL(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message: Messages) = with(itemView){
            txtMessageLeft.text = message.message
            txtTimeLeft.text = SimpleDateFormat("hh:mm").format(message.sentAt)
            //Cargar imagen con picasso
            if(message.profileImageURL.isEmpty()){
                Picasso.get().load(R.drawable.ic_person)
                    .resize(100, 100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imgProfileLeft)
            }else {
                Picasso.get().load(message.profileImageURL)
                    .resize(100, 100)
                    .centerCrop()
                    .transform(CircleTransform())
                    .into(imgProfileLeft)
            }
        }
    }

}