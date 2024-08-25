package com.example.hopedonationapp.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.hopedonationapp.R


class RecyclerViewAdapter(private val docReq: ArrayList<UpdateDocReq>, private val pdfSelectionCallback: (Int) -> Unit):
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.documents_listings, parent, false)

        return MyViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return docReq.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = docReq[position]
        holder.textView.text = currentItem.textView
        holder.button.text = currentItem.buttonText

        holder.button.setOnClickListener {

                pdfSelectionCallback(position)
        }
    }


    class MyViewHolder(private val itemView: View) : RecyclerView.ViewHolder(itemView) {

        val textView: TextView = itemView.findViewById<TextView>(R.id.textView17)
        val button: Button = itemView.findViewById<Button>(R.id.button3)

    }


}
