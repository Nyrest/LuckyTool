package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textview.MaterialTextView
import com.highcapable.yukihookapi.YukiHookAPI
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentHomeBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.ui.activity.OTAActivity
import com.luckyzyx.luckytool.utils.tools.*
import com.luckyzyx.luckytool.utils.tools.UpdateTool.coolmarketUrl
import rikka.core.util.ResourceUtils

@Obfuscate
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var enableModule: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableModule = requireActivity().getBoolean(XposedPrefs,"enable_module",false)

        if (enableModule && YukiHookAPI.Status.isXposedModuleActive){
            binding.statusIcon.setImageResource(R.drawable.ic_round_check_24)
            binding.statusTitle.text = getString(R.string.module_isactivated)
        }else{
            binding.statusCard.setCardBackgroundColor(Color.GRAY)
            binding.statusIcon.setImageResource(R.drawable.ic_round_warning_24)
            binding.statusTitle.text = getString(R.string.module_notactive)
        }

        binding.statusSummary.apply {
            text = getString(R.string.module_version)
            text = "$text$getVersionName($getVersionCode)"
        }
        binding.enableModule.apply {
            text = context.getString(R.string.enable_module)
            isChecked = enableModule
            setOnCheckedChangeListener { buttonView, isChecked ->
                if (buttonView.isPressed) {
                    context.putBoolean(XposedPrefs, "enable_module", isChecked)
                    (activity as MainActivity).restart()
                }
            }
        }

        UpdateTool.checkUpdate(requireActivity(), getVersionName, getVersionCode) { versionName, versionCode, function ->
            if (!requireActivity().getBoolean(SettingsPrefs,"auto_check_update",true)) return@checkUpdate
            binding.updateView.apply {
                isVisible = true
                text = getString(R.string.check_update_hint)+"  -->  $versionName($versionCode)"
                binding.statusCard.setOnClickListener { function() }
            }
        }

        binding.fpsTitle.text = getString(R.string.fps_title)
        binding.fpsSummary.text = getString(R.string.fps_summary)
        binding.fps.apply {
            setOnClickListener {
                val fpsDialog = MaterialAlertDialogBuilder(context).apply {
                    setView(R.layout.layout_fps_dialog)
                }.show()
                val fpsData = context.getFpsMode()
                val currentFps = context.getInt(SettingsPrefs, "current_fps", -1)
                val fpsAutostart = context.getBoolean(SettingsPrefs,"fps_autostart",false)
                val fpsMode = fpsDialog.findViewById<MaterialSwitch>(R.id.fps_mode)?.apply {
                    text = getString(R.string.fps_autostart)
                    isChecked = fpsAutostart
                    isEnabled = currentFps != -1
                    setOnCheckedChangeListener { buttonView, isChecked ->
                        if (buttonView.isPressed) {
                            context.putBoolean(SettingsPrefs,"fps_autostart",isChecked)
                        }
                    }
                }
                val fpsList = fpsDialog.findViewById<ListView>(R.id.fps_list)?.apply {
                    isVisible = fpsData.isNotEmpty()
                    choiceMode = ListView.CHOICE_MODE_SINGLE
                    adapter = ArrayAdapter(context,android.R.layout.simple_list_item_single_choice,fpsData)
                    setItemChecked(currentFps, currentFps != -1)
                    onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                        fpsMode?.isEnabled = true
                        context.putInt(SettingsPrefs, "current_fps", position)
                        ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 $position", true)
                    }
                }
                fpsDialog.findViewById<MaterialButton>(R.id.fps_show)?.apply {
                    text = getString(R.string.display_refresh_rate)
                    var status = false
                    setOnClickListener {
                        status = !status
                        ShellUtils.execCommand("su -c service call SurfaceFlinger 1034 i32 ${if (status) 1 else 0}", true)
                    }
                }
                fpsDialog.findViewById<MaterialButton>(R.id.fps_recover)?.apply {
                    text = getString(R.string.Restore_default_refresh_rate)
                    setOnClickListener {
                        fpsMode?.isChecked = false
                        fpsMode?.isEnabled = false
                        context.putBoolean(SettingsPrefs,"fps_autostart",false)
                        fpsList?.setItemChecked(context.getInt(SettingsPrefs, "current_fps",-1),false)
                        context.putInt(SettingsPrefs, "current_fps",-1)
                        ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 -1", true)
                    }
                }
            }
        }

        binding.systemInfo.apply {
            text =
                """
                ${getString(R.string.brand)}: ${Build.BRAND}
                ${getString(R.string.model)}: ${Build.MODEL}
                ${getString(R.string.system)}: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT})[$getColorOSVersion]
                ${getString(R.string.device)}: ${Build.DEVICE}
                ${getString(R.string.build_version)}: ${Build.DISPLAY}
                ${getString(R.string.flash)}: ${getFlashInfo()}
            """.trimIndent()
            setOnLongClickListener {
                if (!context.getBoolean(SettingsPrefs, "hidden_function", false)) {
                    val guid = getGuid
                    MaterialAlertDialogBuilder(context, dialogCentered).apply {
                        setTitle(context.getString(R.string.device_guid))
                        setMessage(guid)
                        setNeutralButton(android.R.string.cancel, null)
                        setPositiveButton(android.R.string.copy) { _, _ ->
                            context.copyStr(guid)
                        }
                        show()
                    }
                } else {
                    MaterialAlertDialogBuilder(context, dialogCentered).apply {
                        setTitle("下载功能")
                        setView(
                            MaterialTextView(context).apply {
                                setPadding(20.dp)
                                text = coolmarketUrl
                            }
                        )
                        setNeutralButton(android.R.string.cancel, null)
                        setPositiveButton(android.R.string.copy) { _, _ ->
                            UpdateTool.downloadFile(context, "coolmarket.apk", coolmarketUrl)
                        }
                        show()
                    }
                }
                true
            }
        }

        if (!requireActivity().getBoolean(SettingsPrefs, "hidden_function", false)) return
        binding.startOta.apply {
            isVisible = true
            text = "跳转OTA页面"
            setOnClickListener {
                startActivity(Intent(requireActivity(), OTAActivity::class.java))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ResourceUtils.isNightMode(resources.configuration)){
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.menu_settings)).apply {
            setIcon(R.drawable.ic_baseline_info_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ResourceUtils.isNightMode(resources.configuration)){
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) refreshmode(requireActivity())
        if (item.itemId == 2) {
            MaterialAlertDialogBuilder(requireActivity()).apply {
                setTitle(getString(R.string.about_author))
                setView(
                    MaterialTextView(context).apply {
                        setPadding(20.dp)
                        text = "忆清鸣、luckyzyx"
                        setOnLongClickListener{
                            val hideFunc = context.getBoolean(SettingsPrefs,"hidden_function",false)
                            context.putBoolean(SettingsPrefs,"hidden_function",!hideFunc)
                            context.toast("${!hideFunc}")
                            true
                        }
                    }
                )
            }.show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshmode(context: Context) {
        val list = arrayOf(getString(R.string.restart_scope),getString(R.string.reboot),getString(R.string.fast_reboot))
        MaterialAlertDialogBuilder(context)
            .setCancelable(true)
            .setItems(list) { _: DialogInterface?, i: Int ->
                when (i) {
                    0 -> (activity as MainActivity).restartScope(context)
                    1 -> ShellUtils.execCommand("reboot",true)
                    2 -> ShellUtils.execCommand("killall zygote",true)
                }
            }
            .show()
    }
}