package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.highcapable.yukihookapi.YukiHookAPI
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentHomeBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.tools.*
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

        if (YukiHookAPI.Status.isXposedModuleActive && enableModule){
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
                    requireActivity().putBoolean(XposedPrefs, "enable_module", isChecked)
                    (activity as MainActivity).restart()
                }
            }
        }

        UpdateTool.checkUpdate(requireActivity(), getVersionName, getVersionCode) { versionName, versionCode, function ->
            binding.checkUpdateView.apply {
                isVisible = true
                setOnClickListener { function() }
            }
            binding.updateView.text = getString(R.string.check_update_hint)+"  -->  $versionName($versionCode)"
        }

        binding.fpsTitle.text = getString(R.string.fps_title)
        binding.fpsSummary.text = getString(R.string.fps_summary)

        binding.fps.setOnClickListener {
            val fpsDialog = MaterialAlertDialogBuilder(requireActivity()).apply {
                setView(R.layout.layout_fps_dialog)
            }.show()

            fpsDialog.findViewById<ListView>(R.id.fps_list)?.apply {
                adapter = ArrayAdapter(context,android.R.layout.simple_list_item_1,context.getFpsMode())
                onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
                    context.putInt(XposedPrefs, "current_fps", position)
                    ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 $position", true)
                }
            }
            fpsDialog.findViewById<SwitchMaterial>(R.id.fps_mode)?.apply {
                text = getString(R.string.fps_autostart)
                isChecked = context.getBoolean(XposedPrefs,"fps_autostart",false)
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        context.putBoolean(XposedPrefs,"fps_autostart",isChecked)
                    }
                }
            }
            fpsDialog.findViewById<SwitchMaterial>(R.id.fps_window)?.apply {
                text = getString(R.string.display_refresh_rate)
                setOnCheckedChangeListener { buttonView, isChecked ->
                    if (buttonView.isPressed) {
                        ShellUtils.execCommand("su -c service call SurfaceFlinger 1034 i32 ${if (isChecked) 1 else 0}", true)
                    }
                }
            }
            fpsDialog.findViewById<MaterialButton>(R.id.fps_recover)?.apply {
                text = getString(R.string.Restore_default_refresh_rate)
                setOnClickListener {
                    ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 -1", true)
                }
            }
        }

        binding.systemInfo.text =
            """
                ${getString(R.string.brand)}: ${Build.BRAND}
                ${getString(R.string.model)}: ${Build.MODEL}
                ${getString(R.string.system)}: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT})[$getColorOSVersion]
                ${getString(R.string.device)}: ${Build.DEVICE}
                ${getString(R.string.build_version)}: ${Build.DISPLAY}
                ${getString(R.string.flash)}: ${getFlashInfo()}
            """.trimIndent()
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
                setMessage("忆清鸣、luckyzyx")
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
                    0 -> (activity as MainActivity).restartScope(requireActivity())
                    1 -> ShellUtils.execCommand("reboot",true)
                    2 -> ShellUtils.execCommand("killall zygote",true)
                }
            }
            .show()
    }
}