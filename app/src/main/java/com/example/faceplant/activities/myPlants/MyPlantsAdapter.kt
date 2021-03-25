package com.example.faceplant.activities.myPlants

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.Plant
import com.example.faceplant.utils.Constants
import kotlinx.android.synthetic.main.plant_item.view.*

// Adapter class for plant list items
class MyPlantsAdapter(
        private val context: Context,
        private val plantList: ArrayList<Plant>
        ): RecyclerView.Adapter<MyPlantsAdapter.ViewHolder>() {


    //  Inflates the item views which is designed in xml layout file
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                LayoutInflater.from(context).inflate(
                        R.layout.plant_item,
                        parent,
                        false
                )
        )
    }

    // Binds each item in the ArrayList to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = plantList[position]

        FirestoreClass().glidePlantImageLoader(context, model.plantImage, holder.itemView.plant_item_image)
        holder.itemView.plant_type_textView.text = model.plantType

        holder.itemView.setOnClickListener {
            // Launch ViewPlantActivity on clicking the items
            val intent = Intent(context, ViewPlantActivity::class.java)
            intent.putExtra(Constants.PLANT_DETAILS, model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return plantList.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}