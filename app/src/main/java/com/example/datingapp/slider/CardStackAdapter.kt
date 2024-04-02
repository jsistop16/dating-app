package com.example.datingapp.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.datingapp.R
import com.example.datingapp.auth.UserDataModel

class CardStackAdapter(val context: Context, val items: MutableList<UserDataModel>) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardStackAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val view : View = inflater.inflate(R.layout.item_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardStackAdapter.ViewHolder, position: Int) {
        holder.binding(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nickName = itemView.findViewById<TextView>(R.id.nickname)
        val age = itemView.findViewById<TextView>(R.id.age)
        val area = itemView.findViewById<TextView>(R.id.area)
        fun binding(data : UserDataModel){
            nickName.setText(data.nickname)
            age.setText(data.age)
            area.setText(data.area)
        }
    }
}