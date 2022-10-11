package com.luckyzyx.luckytool.ui.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.highcapable.yukihookapi.hook.factory.dataChannel
import com.highcapable.yukihookapi.hook.log.YukiLoggerData
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentLogsBinding
import com.luckyzyx.luckytool.ui.adapter.LogInfoViewAdapter
import rikka.core.util.ResourceUtils

class LoggerFragment : Fragment() {

    private lateinit var binding: FragmentLogsBinding

    private var listData = ArrayList<YukiLoggerData>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLogsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.loglistView.apply {
            adapter = LogInfoViewAdapter(requireActivity(),listData)
            layoutManager = LinearLayoutManager(requireActivity())
        }
        if (listData.isEmpty()) loadLogger()
    }

    private fun loadLogger(){
        listData.clear()
        requireActivity().resources.getStringArray(R.array.xposed_scope).forEach { scope ->
            requireActivity().dataChannel(scope).obtainLoggerInMemoryData { its ->
                its.takeIf { e -> e.isNotEmpty() }?.forEach { e -> listData.add(e) }
                binding.loglistView.adapter?.notifyDataSetChanged()
                binding.loglistView.isVisible = listData.isNotEmpty()
                binding.logNodataView.apply {
                    text = context.getString(R.string.log_no_data)
                    isVisible = listData.isEmpty()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadLogger()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ResourceUtils.isNightMode(resources.configuration)){
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
//        menu.add(0, 2, 0, getString(R.string.menu_versioninfo)).apply {
//            setIcon(R.drawable.ic_baseline_save_24)
//            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
//            if (ResourceUtils.isNightMode(resources.configuration)){
//                iconTintList = ColorStateList.valueOf(Color.WHITE)
//            }
//        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) loadLogger()
        return super.onOptionsItemSelected(item)
    }
}