package com.luckyzyx.luckytool.ui.fragment

import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.drake.net.utils.scopeLife
import com.drake.net.utils.withIO
import com.drake.net.utils.withMain
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentMultiBinding
import com.luckyzyx.luckytool.ui.adapter.AppInfo
import com.luckyzyx.luckytool.ui.adapter.AppInfoViewAdapter
import com.luckyzyx.luckytool.utils.tools.PackageUtils

class MultiFragment : Fragment() {

    private lateinit var binding: FragmentMultiBinding
    private var appListAllDatas = ArrayList<AppInfo>()
    private var appInfoAdapter: AppInfoViewAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMultiBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                   return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    appInfoAdapter?.getFilter?.filter(newText.toString())
                    return true
                }
            })
        }

        if (appListAllDatas.isEmpty()) {
            loadData()
        }
    }

    private fun loadData(){
        val progressDialog = MaterialAlertDialogBuilder(requireActivity()).apply {
            setCancelable(false)
            setMessage("")
        }.create()
        scopeLife {
            progressDialog.show()
            withIO {
                val packageManager = requireActivity().packageManager
                val appinfos = PackageUtils(packageManager).getInstalledApplications(0)
                for (i in appinfos) {
                    withMain {
                        progressDialog.setMessage("${getString(R.string.loading)} ${appinfos.indexOf(i)} / ${appinfos.size}")
                    }
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
            progressDialog.dismiss()
            binding.recyclerView.apply {
                adapter = AppInfoViewAdapter(context, appListAllDatas)
                layoutManager = LinearLayoutManager(context)
            }
        }
    }
}