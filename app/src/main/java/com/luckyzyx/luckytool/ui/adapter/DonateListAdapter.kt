package com.luckyzyx.luckytool.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.luckyzyx.luckytool.databinding.LayoutDonateItemBinding
import com.luckyzyx.luckytool.utils.tools.DonateInfo

class DonateListAdapter(val context: Context, val data: ArrayList<DonateInfo>) : RecyclerView.Adapter<DonateListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutDonateItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = data[position].name
        holder.money.text = data[position].money.toString()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(binding: LayoutDonateItemBinding): RecyclerView.ViewHolder(binding.root) {
        val name = binding.donateName
        val money = binding.donateMoney
    }
}