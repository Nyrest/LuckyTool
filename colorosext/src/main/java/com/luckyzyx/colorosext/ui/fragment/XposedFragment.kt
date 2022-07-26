package com.luckyzyx.colorosext.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.luckyzyx.colorosext.databinding.FragmentXposedBinding
import com.luckyzyx.colorosext.ui.adapter.textbook.TextBookAdapter
import com.luckyzyx.colorosext.ui.adapter.textbook.TextBookDecorator
import com.luckyzyx.colorosext.ui.adapter.textbook.TextBookListViewModel
import com.luckyzyx.colorosext.ui.adapter.textbook.TextBookSpanLookup

class XposedFragment : Fragment() {

    private lateinit var binding: FragmentXposedBinding
    private lateinit var viewModel: TextBookListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentXposedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //创建ViewModelProvider ，它将通过给定的Factory创建ViewModels并将它们保留在给定的ViewModelStoreOwner的存储中
        val viewModelProvider = ViewModelProvider(viewModelStore, SavedStateViewModelFactory(requireActivity().application, this))
        //网格中的列数
        val spanCount = 1
        //创建一个垂直的 GridLayoutManager
        val gridLayoutManager = GridLayoutManager(requireActivity(), spanCount)
        //绑定到layout
        binding.recyclerView.layoutManager = gridLayoutManager
        //添加布局
        binding.recyclerView.addItemDecoration(TextBookDecorator(spanCount))
        //获取viewModel
        viewModel = viewModelProvider.get(TextBookListViewModel::class.java)
        //观察者事件
        viewModel.textBookLists.observe(viewLifecycleOwner) {
            val textBookAdapter = TextBookAdapter(it!!)
            binding.recyclerView.adapter = textBookAdapter
            gridLayoutManager.spanSizeLookup = TextBookSpanLookup(spanCount, textBookAdapter)
        }
        //加载数据
        viewModel.loadData()
    }

    override fun onResume() {
        super.onResume()
        if (viewModel.textBookLists.value?.isEmpty() == true) {
            viewModel.loadData()
        }
    }
}