package com.example.faceplant.activities.plantCare


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.faceplant.R
import com.example.faceplant.firestore.FirestoreClass
import com.example.faceplant.models.PlantCareModel
import com.example.faceplant.utils.Constants
import kotlinx.android.synthetic.main.plant_item.view.*

// the adapter will hold all the data and views
class PlantCareAdapter(
    val context: Context,
    val plantCareList: ArrayList<PlantCareModel>) :
    RecyclerView.Adapter<PlantCareAdapter.ViewHolder>() {


    //function that will create a viewholder for every single item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.plant_item,
                parent,
                false
            )
        )
    }

    //function to binds each item in the arraylist to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = plantCareList[position]

        FirestoreClass().glidePlantImageLoader(context, model.plantCareImage, holder.itemView.plant_item_image)
        holder.itemView.plant_type_textView.text = model.plantCareTitle

        //handle user click on one item in the recyclerview
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
