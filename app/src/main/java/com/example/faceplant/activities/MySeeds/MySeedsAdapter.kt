package com.example.faceplant.activities.MySeeds

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.faceplant.R

class MySeedsAdapter: RecyclerView.Adapter<MySeedsHolder>() {
    val numbers = listOf(1,2,3,4)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
    = MySeedsHolder(
            LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_my_seeds, parent, false))

    override fun onBindViewHolder(holder: MySeedsHolder, position: Int) {
        val number = numbers[position]
        holder.textView.text = "$number"
    }
    override fun getItemCount() = numbers.size
}