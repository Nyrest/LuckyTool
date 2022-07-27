package com.luckyzyx.colorosext.ui.adapter.scope

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.luckyzyx.colorosext.databinding.FuncItemBinding
import com.luckyzyx.colorosext.databinding.ScopeItemBinding
import pokercc.android.expandablerecyclerview.ExpandableAdapter

private class ScopeVH(val itemBinding: ScopeItemBinding) : ExpandableAdapter.ViewHolder(itemBinding.root)
private class FuncVH(val itemBinding: FuncItemBinding) : ExpandableAdapter.ViewHolder(itemBinding.root)

class ScopeAdapter(private val data:List<Scope>, private val onItemClick: (groupID: Int, childID:Int) -> Unit) : ExpandableAdapter<ExpandableAdapter.ViewHolder>() {

    //获取方法数量
    override fun getChildCount(groupPosition: Int): Int = data[groupPosition].funcs.size
    //获取作用域数量
    override fun getGroupCount(): Int = data.size
    //绑定子viewholder事件
    override fun onBindChildViewHolder(
        holder: ViewHolder,
        groupPosition: Int,
        childPosition: Int,
        payloads: List<Any>
    ) {
        val child = data[groupPosition].funcs[childPosition]
        if (payloads.isEmpty()){
            (holder as FuncVH).apply {
                itemBinding.title.text = child.title
                holder.itemView.setOnClickListener{
                    onItemClick.invoke(groupPosition,childPosition)
                }
            }
        }
    }

    //绑定父viewholder事件
    override fun onBindGroupViewHolder(
        holder: ViewHolder,
        groupPosition: Int,
        expand: Boolean,
        payloads: List<Any>
    ) {
        val group = data[groupPosition]
        if (payloads.isEmpty()){
            (holder as ScopeVH).apply {
                itemBinding.title.text = group.title
            }
        }
    }

    //创建子viewholder
    override fun onCreateChildViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return FuncVH(FuncItemBinding.inflate(inflater, viewGroup, false))
    }
    //创建父viewholder
    override fun onCreateGroupViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        return ScopeVH(ScopeItemBinding.inflate(inflater, viewGroup, false))
    }

    //父viewholder折叠展开事件
    override fun onGroupViewHolderExpandChange(
        holder: ViewHolder,
        groupPosition: Int,
        animDuration: Long,
        expand: Boolean
    ) {
        val arrowImage = (holder as ScopeVH).itemBinding.arrowImage
        if (expand) {
            ObjectAnimator.ofFloat(arrowImage, View.ROTATION, +90f)
                .setDuration(animDuration)
                .start()
        } else {
            ObjectAnimator.ofFloat(arrowImage, View.ROTATION, 0f)
                .setDuration(animDuration)
                .start()
        }
    }
}