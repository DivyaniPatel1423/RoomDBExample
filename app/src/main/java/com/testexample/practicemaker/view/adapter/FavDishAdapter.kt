package com.testexample.practicemaker.view.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testexample.practicemaker.databinding.ItemCustomListBinding
import com.testexample.practicemaker.databinding.ItemDishLayoutBinding
import com.testexample.practicemaker.model.entities.FavDish
import com.testexample.practicemaker.view.activity.AddUpdateDishActivity
import com.testexample.practicemaker.viewmodel.FavDishViewModel

class FavDishAdapter(private val fragment: Context,val viewModel: FavDishViewModel) : RecyclerView.Adapter<FavDishAdapter.CustomViewHolder>() {

    private var dishes: List<FavDish> = listOf()
    private var myDishes: ArrayList<FavDish> = arrayListOf()
    private var pos =0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding: ItemDishLayoutBinding = ItemDishLayoutBinding.inflate(LayoutInflater.from(fragment), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dishes.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val dish = dishes[position]
        pos = position
        Glide.with(fragment)
                .load(dish.image)
                .into(holder.ivDishImage)

        holder.ivDishTitle.text = dish.title
        holder.lnCardView.setOnClickListener {
            val intent = Intent(fragment, AddUpdateDishActivity::class.java)
            intent.putExtra("favdish", dish)
            fragment.startActivity(intent)
        }
        holder.imageDelete.setOnClickListener {
            viewModel.delete(dish)
            deleteItem(position)
        }
    }

    class CustomViewHolder(view: ItemDishLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        val ivDishImage = view.imgDishImage
        val ivDishTitle = view.tvDishName
        val lnCardView = view.lnCardview
        val imageDelete = view.imgDelete
    }

    fun dishesList(favDish: List<FavDish>) {
        dishes = favDish
        notifyItemChanged(pos)
    }
    fun deleteItem(index: Int){
        myDishes = dishes as ArrayList<FavDish>
        myDishes.removeAt(index)
        notifyDataSetChanged()
    }
    companion object {
        private val FAV_DISH_COMPARATOR = object : DiffUtil.ItemCallback<FavDish>() {
            override fun areItemsTheSame(oldItem: FavDish, newItem: FavDish): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: FavDish, newItem: FavDish): Boolean {
                return oldItem.favoriteDish == newItem.favoriteDish
            }
        }
    }
}