package com.example.faceplant.activities.MySeeds

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.faceplant.R
import com.example.faceplant.models.Seed
import com.example.faceplant.utils.Constants
import kotlinx.android.synthetic.main.seed_item.view.*

// Adapter class for plant list items
class MySeedsAdapter(
        private val context: Context,
        private val seedsList: ArrayList<Seed>
): RecyclerView.Adapter<MySeedsAdapter.ViewHolder>() {


    //  Inflates the item views which is designed in xml layout file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.seed_item,
                        parent,
                        false
                )
        )
    }

    // Binds each item in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = seedsList[position]

        holder.itemView.seeds_type_textView.text = model.seedsType

        holder.itemView.setOnClickListener {
            // Launch ViewPlantActivity on clicking the items
            val intent = Intent(context, ViewSeedActivity::class.java)
            intent.putExtra(Constants.SEED_DETAILS, model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return seedsList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}