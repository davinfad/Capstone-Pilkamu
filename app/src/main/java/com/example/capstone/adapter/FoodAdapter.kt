package com.example.capstone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstone.R


class FoodAdapter(private var foodList: List<Food> = emptyList()) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    private var itemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foodList[position]
        holder.bind(food)
    }

    override fun getItemCount(): Int = foodList.size

    fun setData(data: List<Food>) {
        foodList = data
        notifyDataSetChanged()
    }

    inner class FoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodNameTextView: TextView = itemView.findViewById(R.id.foodNameTextView)
        private val foodImageView: ImageView = itemView.findViewById(R.id.foodImageView)
        fun bind(food: Food) {
            itemView.apply {
                foodNameTextView.text = food.foodName

                Glide.with(context)
                    .load(food.imageUrl)
                    .into(foodImageView)
            }
                foodImageView.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener?.onItemClick(it, foodList[position])
                    }
                }
            foodNameTextView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(it, foodList[position])
                }
            }

        }


    }


    interface OnItemClickListener {
        fun onItemClick(view: View, food: Food)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


}
