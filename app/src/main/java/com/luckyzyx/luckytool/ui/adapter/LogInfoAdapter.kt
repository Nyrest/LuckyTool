package com.luckyzyx.luckytool.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.highcapable.yukihookapi.hook.log.YukiLoggerData
import com.luckyzyx.luckytool.databinding.LayoutLoginfoItemBinding
import com.luckyzyx.luckytool.utils.tools.dialogCentered
import com.luckyzyx.luckytool.utils.tools.dp
import com.luckyzyx.luckytool.utils.tools.getAppIcon
import com.luckyzyx.luckytool.utils.tools.getAppLabel

class LogInfoViewAdapter(val context: Context, private val data: ArrayList<YukiLoggerData>) :
    RecyclerView.Adapter<LogInfoViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutLoginfoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.logIcon.setImageDrawable(context.getAppIcon(data[position].packageName))
        holder.logTime.text = data[position].time
        holder.logMsg.text = data[position].msg
        holder.logRoot.setOnClickListener(null)
        holder.logRoot.setOnClickListener {
            MaterialAlertDialogBuilder(context, dialogCentered)
                .setTitle(context.getAppLabel(data[position].packageName))
                .setView(
                    NestedScrollView(context).apply {
                        addView(
                            MaterialTextView(context).apply {
                                setPadding(20.dp, 0, 20.dp, 20.dp)
                                val msg = data[position].msg
                                val throwable = data[position].throwable.toString()
                                text = msg + if (throwable != "null") "\n\n$throwable" else ""
                            }
                        )
                    }
                )
                .show()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ViewHolder(binding: LayoutLoginfoItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val logRoot = binding.root
        val logIcon: ImageView = binding.logIcon
        val logTime: TextView = binding.logTime
        val logMsg: TextView = binding.logMsg
    }
}