package com.tonykazanjian.sonyyelpfusion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.data.Business
import com.tonykazanjian.sonyyelpfusion.data.BusinessesResponse
import com.tonykazanjian.sonyyelpfusion.databinding.BusinessItemBinding
import com.tonykazanjian.sonyyelpfusion.ui.viewmodels.BusinessItemViewModel
import kotlinx.android.synthetic.main.business_item.view.*

class BusinessListAdapter(private var onItemClickListener: (Business) -> Unit, private var list: MutableList<Business> = mutableListOf())
    : RecyclerView.Adapter<BusinessListAdapter.BusinessViewHolder>() {

    lateinit var binding: BusinessItemBinding

    fun addData(list: List<Business>){
        this.list.addAll(this.list.size, list)
        notifyItemInserted(this.list.size-1)
    }

    fun setQueryData(list: List<Business>){
        clearItems()
        this.list = list.toMutableList()
        notifyDataSetChanged()
    }

    private fun clearItems(){
        list.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.business_item, parent, false)
        return BusinessViewHolder(binding.root)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = list[position]
        holder.bind(business)
        holder.itemView.setOnClickListener{onItemClickListener(business)}
    }

    inner class BusinessViewHolder(view: View): RecyclerView.ViewHolder(view){

        fun bind(item: Business){
            binding.viewModel = BusinessItemViewModel(item)
            Glide.with(binding.root.context)
                .load(item.imageUrl)
                .into(binding.businessImageView)

        }
    }


}