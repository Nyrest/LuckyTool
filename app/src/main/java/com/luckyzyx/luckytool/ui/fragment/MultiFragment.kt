package com.luckyzyx.luckytool.ui.fragment

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withIO
import com.luckyzyx.luckytool.databinding.FragmentMultiBinding
import com.luckyzyx.luckytool.ui.adapter.AppInfo
import com.luckyzyx.luckytool.ui.adapter.MultiInfoAdapter
import com.luckyzyx.luckytool.utils.tools.PackageUtils

class MultiFragment : Fragment() {

    private lateinit var binding: FragmentMultiBinding
    private var appListAllDatas = ArrayList<AppInfo>()
    private var multiInfoAdapter: MultiInfoAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMultiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchViewLayout.apply {
            hint = "Name / PackageName"
            isHintEnabled = true
            isHintAnimationEnabled = true
        }
        binding.searchView.apply {
            addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    multiInfoAdapter?.getFilter?.filter(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
        binding.swipeRefreshLayout.apply {
            setOnRefreshListener {
                loadData()
            }
        }

        if (appListAllDatas.isEmpty()) {
            loadData()
        }
    }

    /**
     * 加载数据
     */
    private fun loadData(){
        binding.swipeRefreshLayout.isRefreshing = true
        binding.searchViewLayout.isEnabled = false
        appListAllDatas.clear()
        scopeLife {
            withIO {
                val packageManager = requireActivity().packageManager
                val appinfos = PackageUtils(packageManager).getInstalledApplications(0)
                for (i in appinfos) {
                    if (i.flags and ApplicationInfo.FLAG_SYSTEM == 1) continue
                    appListAllDatas.add(
                        AppInfo(
                            i.loadIcon(packageManager),
                            i.loadLabel(packageManager),
                            i.packageName,
                        )
                    )
                }
            }
            multiInfoAdapter = MultiInfoAdapter(requireActivity(), appListAllDatas)
            binding.recyclerView.apply {
                adapter = multiInfoAdapter
                layoutManager = LinearLayoutManager(context)
            }
            binding.swipeRefreshLayout.isRefreshing = false
            binding.searchViewLayout.isEnabled = true
        }
    }
}