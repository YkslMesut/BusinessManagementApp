package com.example.myunetlog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myunetlog.databinding.RowConsignmentBinding
import com.example.myunetlog.model.Consignment


class ConsignmentAdapter(private val postList : ArrayList<Consignment>) :
    RecyclerView.Adapter<ConsignmentAdapter.PostHolder>() {

    inner class PostHolder(private val binding : RowConsignmentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(consignment : Consignment){
            binding.consignment = consignment
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder =
        PostHolder(RowConsignmentBinding.inflate(LayoutInflater.from(parent.context), parent, false))



    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.bind(postList[position])

    }

    override fun getItemCount(): Int {
        return postList.size
    }
    fun updateData(newList: List<Consignment>) {
        postList.clear()
        postList.addAll(newList)
        notifyDataSetChanged()
    }
}