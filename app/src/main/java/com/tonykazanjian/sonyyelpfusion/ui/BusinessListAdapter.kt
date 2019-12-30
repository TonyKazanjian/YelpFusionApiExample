package com.tonykazanjian.sonyyelpfusion.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tonykazanjian.sonyyelpfusion.R
import com.tonykazanjian.sonyyelpfusion.data.Business
import kotlinx.android.synthetic.main.business_item.view.*

class BusinessListAdapter(private var onItemClickListener: (Business) -> Unit, private var list: MutableList<Business> = mutableListOf())
    : RecyclerView.Adapter<BusinessListAdapter.BusinessViewHolder>() {

    fun addData(list: List<Business>){
        this.list.addAll(this.list.size, list)
        notifyItemInserted(this.list.size-1)
    }

    fun clearItems(){
        list.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusinessViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.business_item, parent,false)
        return BusinessViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BusinessViewHolder, position: Int) {
        val business = list[position]
        holder.bind(business)
        holder.itemView.setOnClickListener{onItemClickListener(business)}
    }

    override fun setHasStableIds(hasStableIds: Boolean) {
        super.setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return list[position].hashCode().toLong()
    }

    inner class BusinessViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun bind(item: Business){
            view.apply {
                business_name_text_view.text = item.name
                business_rating_bar.rating = item.rating
                price_text_view.text = item.price
                business_status_text_view.text = if (item.isClosed) {
                    context.getString(R.string.business_status_closed)
                } else {
                    context.getString(R.string.business_status_open)
                }
                Glide.with(context)
                    .load(item.imageUrl)
                    .into(business_image_view)
            }
        }
    }


}