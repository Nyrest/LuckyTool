package com.luckyzyx.colorosext.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.luckyzyx.colorosext.databinding.FragmentXposedBinding
import com.luckyzyx.colorosext.ui.adapter.scope.*

class XposedFragment : Fragment() {

    private lateinit var viewModel: ScopeViewModel
    private lateinit var binding: FragmentXposedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentXposedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initScope()
    }

    //加载页面
    private fun initScope(){
        //短列表
        val shortList = false

        val modelFactory = SavedStateViewModelFactory(requireActivity().application, this)
        viewModel = ViewModelProvider(this, modelFactory)[ScopeViewModel::class.java]
        viewModel.scopeList.observe(viewLifecycleOwner) {
            //判断限制List Size
            val list = if (shortList) it.subList(0, 1) else it
            binding.recyclerView.adapter = setAdapter(list)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
    }

    //设置适配器
    private fun setAdapter(list: List<Scope>): ScopeAdapter {
        return ScopeAdapter(list) { groupID: Int, childID: Int ->
            Toast.makeText(requireActivity(), "Group:$groupID Child:$childID", Toast.LENGTH_SHORT).show()
        }
    }

    //创建数据
    private fun createData(): ScopeWrapper {
        return ScopeWrapper(
            listOf(
                Scope(1, "系统框架", ""),
                Scope(2, "系统界面", ""),
                Scope(3, "系统桌面", ""),
                Scope(4, "时钟", ""),
                Scope(5, "相机", ""),
                Scope(6, "安全中心", ""),
                Scope(7, "应用包安装程序", ""),
                Scope(8, "游戏助手", ""),
                Scope(9, "云服务", ""),
                Scope(10, "好多动漫", ""),
                Scope(11, "图凌", ""),
            ),
            listOf(
                Func(1,1, "方法1", null, "key1", null),
                Func(2,1, "方法2", null, "key2", null),
                Func(3,1, "方法3", null, "key3", null),
                Func(4,1, "方法4", null, "key1", null),
                Func(5,1, "方法5", null, "key2", null),
                Func(6,1, "方法6", null, "key3", null),
                Func(7,1, "方法7", null, "key1", null),
                Func(8,1, "方法8", null, "key2", null),
                Func(9,1, "方法9", null, "key3", null),
            )
        )
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.scopeList.value.isNullOrEmpty()) {
            viewModel.loadData(createData())
        }
    }
}