package com.example.faceplant.activities.plantCare

// the adapter will hold all the data and views

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.PlantCareModel
import com.example.faceplant.utils.Constants
import kotlinx.android.synthetic.main.plantcare_item.view.*


//ska jag skicka in arraylist<string>?

class PlantCareAdapter(
    private val context: Context,
    private val plantCareList: ArrayList<PlantCareModel>) :
    RecyclerView.Adapter<PlantCareAdapter.ViewHolder>() {


    //function that will create a viewholder for every single item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.plantcare_item,
                parent,
                false
            )
        )
    }

    //function to binds each item in the arraylist to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = plantCareList[position]

        FirestoreClass().glidePlantImageLoader(context, model.plantCareImage, holder.itemView.MyPlants_Plantimage)
        holder.itemView.MyPlants_Planttitle.text = model.plantCareTitle

        holder.itemView.setOnClickListener {
            val intent = Intent(context, PlantCareProfileActivity::class.java)
            intent.putExtra(Constants.PLANT_CARE_DETAILS, model)
            context.startActivity(intent)
        }

    }

    //function that returns the number of items in the list
    override fun getItemCount(): Int {
        return plantCareList.size
    }

    //the viewholder describe an item view and its place withing the recyclerview
    class ViewHolder(view: View):RecyclerView.ViewHolder(view)
}
