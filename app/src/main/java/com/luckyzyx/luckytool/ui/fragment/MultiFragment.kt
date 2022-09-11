package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.progressindicator.LinearProgressIndicator
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentMultiBinding
import com.luckyzyx.luckytool.ui.adapter.AppInfo
import com.luckyzyx.luckytool.ui.adapter.AppInfoViewAdapter
import kotlinx.coroutines.*

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

    @SuppressLint("SetTextI18n")
    @OptIn(DelicateCoroutinesApi::class)
    fun loadData(){
        val progressDialog = MaterialAlertDialogBuilder(requireActivity()).apply {
            setCancelable(false)
            setView(R.layout.layout_mutli_progress)
        }.create()
        GlobalScope.launch(Dispatchers.Main){
            progressDialog.show()
            withContext(Dispatchers.IO){
                val packageManager = requireActivity().packageManager
                val appinfos = packageManager.getInstalledApplications(0)
                for (i in appinfos) {
                    withContext(Dispatchers.Main){
                        progressDialog.findViewById<TextView>(R.id.tv)?.apply {
                            text = "${getString(R.string.loading)} ${appinfos.indexOf(i)} / ${appinfos.size}"
                            gravity = TextView.TEXT_ALIGNMENT_CENTER
                        }
                        progressDialog.findViewById<LinearProgressIndicator>(R.id.progress)?.apply {
                            max = appinfos.size
                            setProgress(appinfos.indexOf(i),true)
                        }
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
            appInfoAdapter = AppInfoViewAdapter(requireActivity(), appListAllDatas)
            binding.recyclerView.apply {
                adapter = appInfoAdapter
                layoutManager = LinearLayoutManager(requireActivity())
            }
        }
    }
}