package com.example.map

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.map.roodatabase.FeaturedEntity
import kotlinx.android.synthetic.main.row.view.*


class MyAdapter(var myData: ArrayList<String>, var itemClickInterface: ItemClickInterface) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    lateinit var binding : ViewDataBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),R.layout.row,parent ,false) as ViewDataBinding
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return myData.size!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(myData.get(position))
        holder.address.text = myData.get(position)
        holder.cv.setOnClickListener {
            var address = it.tvAddress.text.toString()
            itemClickInterface.onItemClick(address)
        }
    }
    class ViewHolder(var binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root){
        lateinit var address :TextView
        lateinit var cv :ConstraintLayout
        fun bind(data: String?) {
            address = binding.root.tvAddress
            cv = binding.root.cv
            binding.executePendingBindings()

        }


    }
}