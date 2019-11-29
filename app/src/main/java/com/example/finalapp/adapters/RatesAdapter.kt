package com.example.finalapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.finalapp.R
import com.example.finalapp.models.Rate
import com.example.finalapp.utils.CircleTransform
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_rate.view.*
import java.text.SimpleDateFormat

class RatesAdapter(private val items: ArrayList<Rate>): RecyclerView.Adapter<RatesAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_rate, parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(rate: Rate) = with(itemView){
            txtComment.text = rate.text
            txtCalendar.text = SimpleDateFormat("dd MMM, yyyy").format(rate.createdAt)
            txtRateCount.text = "${rate.rate}"
            Picasso.get().load(rate.profileImgUrl).resize(100, 100)
                .centerCrop().transform(CircleTransform()).into(imgProfile)
        }
    }

}