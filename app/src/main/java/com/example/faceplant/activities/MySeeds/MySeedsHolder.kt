package com.example.faceplant.activities.MySeeds

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.faceplant.R


class MySeedsHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView){
    val textView = itemView.findViewById<TextView>(R.id.text_view_mySeeds)
}