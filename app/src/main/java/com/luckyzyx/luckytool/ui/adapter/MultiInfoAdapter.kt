package com.luckyzyx.luckytool.ui.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.switchmaterial.SwitchMaterial
import com.luckyzyx.luckytool.databinding.LayoutMultiinfoItemBinding
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import com.luckyzyx.luckytool.utils.tools.getStringSet
import com.luckyzyx.luckytool.utils.tools.putStringSet
import java.io.Serializable

data class AppInfo(
    var appIcon: Drawable,
    var appName: CharSequence,
    var packName: String,
) : Serializable

class MultiInfoAdapter(private val context: Context, datas: ArrayList<AppInfo>) : RecyclerView.Adapter<MultiInfoAdapter.ViewHolder>() {

    private var allDatas = ArrayList<AppInfo>()
    private var filterDatas = ArrayList<AppInfo>()
    private var enabledMulti = ArrayList<String>()
    private var sortData = ArrayList<AppInfo>()

    init {
        allDatas = datas
        sortDatas()
        filterDatas = datas
    }

    private fun sortDatas(){
        val getEnabledMulti = context.getStringSet(XposedPrefs,"enabledMulti", HashSet<String>())
        if (getEnabledMulti != null && getEnabledMulti.isNotEmpty()){
            for (i in getEnabledMulti){
                enabledMulti.add(i)
            }
        }
        allDatas.forEach { its ->
            if (enabledMulti.contains(its.packName)) sortData.add(0,its)
        }
        enabledMulti.clear()
        sortData.forEach {
            enabledMulti.add(it.packName)
        }
        context.putStringSet(XposedPrefs,"enabledMulti",enabledMulti.toSet())
        allDatas.apply {
            sortData.forEach {
                this.remove(it)
                this.add(0,it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LayoutMultiinfoItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.appIcon.setImageDrawable(filterDatas[position].appIcon)
        holder.appName.text = filterDatas[position].appName
        holder.packName.text = filterDatas[position].packName
        holder.appInfoView.setOnClickListener(null)
        holder.switchview.setOnCheckedChangeListener(null)

        holder.switchview.isChecked = enabledMulti.contains(filterDatas[position].packName)
        holder.appInfoView.setOnClickListener {
            holder.switchview.isChecked = !holder.switchview.isChecked
        }
        holder.switchview.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                enabledMulti.add(filterDatas[position].packName)
            }else{
                enabledMulti.remove(filterDatas[position].packName)
            }
            context.putStringSet(XposedPrefs,"enabledMulti",enabledMulti.toSet())
        }
    }

    override fun getItemCount(): Int {
        return filterDatas.size
    }

    val getFilter get() = object : Filter(){
        override fun performFiltering(constraint: CharSequence): FilterResults {
            filterDatas = if (constraint.isBlank()) {
                allDatas
            }else{
                val filterlist = ArrayList<AppInfo>()
                for (data in allDatas) {
                    if (
                        data.appName.toString().lowercase().contains(constraint.toString().lowercase()) ||
                        data.packName.lowercase().contains(constraint.toString().lowercase())) {
                        filterlist.add(data)
                    }
                }
                filterlist
            }
            val filterResults = FilterResults()
            filterResults.values = filterDatas
            return filterResults
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults?) {
            @Suppress("UNCHECKED_CAST")
            filterDatas = results?.values as ArrayList<AppInfo>
            notifyDataSetChanged()
        }
    }

    class ViewHolder(binding: LayoutMultiinfoItemBinding) : RecyclerView.ViewHolder(binding.root){
        val appInfoView: ConstraintLayout = binding.root
        val appIcon: ImageView = binding.appIcon
        val appName: TextView = binding.appName
        val packName: TextView = binding.packName
        val switchview: SwitchMaterial = binding.switchview
    }
}