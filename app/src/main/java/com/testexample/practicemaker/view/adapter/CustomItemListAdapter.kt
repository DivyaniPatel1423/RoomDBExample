package com.testexample.practicemaker.view.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testexample.practicemaker.databinding.ItemCustomListBinding
import com.testexample.practicemaker.view.activity.AddUpdateDishActivity

class CustomItemListAdapter(val context : Activity,
                            val itemList :List<String>,
                            val selection :String) : RecyclerView.Adapter<CustomItemListAdapter.CustomViewHolder>()
   {
       override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
           val binding : ItemCustomListBinding = ItemCustomListBinding.inflate(LayoutInflater.from(context),parent,false)
           return CustomViewHolder(binding)
       }

       override fun getItemCount(): Int {
           return itemList.size
       }

       override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
           val item =  itemList[position]
           holder.tvName.text = item
           holder.itemView.setOnClickListener {
               if(context is AddUpdateDishActivity){
                   context.selectedCustomItem(item,selection)
               }
           }
       }
       class CustomViewHolder(view : ItemCustomListBinding) : RecyclerView.ViewHolder(view.root){
           val tvName = view.tvItemName
       }
   }