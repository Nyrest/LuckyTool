package com.luckyzyx.colorosext.ui.adapter.textbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.luckyzyx.colorosext.databinding.FragmentXposedBinding

class TextBookListActivity : AppCompatActivity() {

    private lateinit var binding: FragmentXposedBinding
    private lateinit var viewModel: TextBookListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentXposedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //创建ViewModelProvider ，它将通过给定的Factory创建ViewModels并将它们保留在给定的ViewModelStoreOwner的存储中
        val viewModelProvider = ViewModelProvider(this, SavedStateViewModelFactory(application, this))
        //网格中的列数
        val spanCount = 1
        //创建一个垂直的 GridLayoutManager
        val gridLayoutManager = GridLayoutManager(this, spanCount)
        //绑定到layout
        binding.recyclerView.layoutManager = gridLayoutManager
        //添加布局
        binding.recyclerView.addItemDecoration(TextBookDecorator(spanCount))
        //获取viewModel
        viewModel = viewModelProvider.get(TextBookListViewModel::class.java)
        //
        viewModel.textBookLists.observe(this) {
            val textBookAdapter = TextBookAdapter(it!!)
            binding.recyclerView.adapter = textBookAdapter
            gridLayoutManager.spanSizeLookup = TextBookSpanLookup(spanCount, textBookAdapter)
        }
    }

    //恢复时判断重载数据
    override fun onResume() {
        super.onResume()
        if (viewModel.textBookLists.value?.isNotEmpty() != true) {
            viewModel.loadData()
        }
    }

}

