package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentXposedBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.tools.*
import rikka.core.util.ResourceUtils
import java.util.*

class XposedFragment : Fragment() {
    private lateinit var binding: FragmentXposedBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentXposedBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        ).apply {
            if (ResourceUtils.isNightMode(resources.configuration)){
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.menu_versioninfo)).setIcon(R.drawable.ic_baseline_info_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        ).apply {
            if (ResourceUtils.isNightMode(resources.configuration)){
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) (activity as MainActivity).restartScope(requireActivity())
        if (item.itemId == 2) requireActivity().bottomSheet()
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun Context.bottomSheet(){
        val xposedScope = resources.getStringArray(R.array.xposed_scope)
        Arrays.sort(xposedScope)
        var str = getString(R.string.scope_version_info)
        for (scope in xposedScope) {
            val arrayList = getAppVersion(scope)
            if (arrayList.isEmpty()) continue
            str += "\n\n${getString(R.string.app_label)}: ${getAppLabel(scope)}\n${getString(R.string.package_name)}: $scope\n${getString(R.string.version)}: ${arrayList[0]}(${arrayList[1]})[${arrayList[2]}]"
        }
        val nestedScrollView = NestedScrollView(requireActivity()).apply {
            setPadding(8.dp,0,8.dp,0)
            addView(
                TextView(requireActivity()).apply {
                    textSize = 16F
                    text = str
                }
            )
        }
        val bottomSheetDialog = BottomSheetDialog(requireActivity())
        bottomSheetDialog.setContentView(nestedScrollView)
        bottomSheetDialog.show()
    }
}

class ScopeApp : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(requireActivity()).apply {
                    key = "android"
                    setIcon(android.R.mipmap.sym_def_app_icon)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.corepatch)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_android)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "StatusBar"
                    icon = requireActivity().getAppIcon("com.android.systemui")
                    title = getString(R.string.StatusBar)
                    summary = getString(R.string.StatusBarNotice)+","+getString(R.string.StatusBarIcon)+","+getString(R.string.StatusBarClock)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_statusBar)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.android.launcher"
                    icon = requireActivity().getAppIcon(key)
                    title = getString(R.string.Desktop)
                    summary = getString(R.string.launcher_layout_row_colume)+","+getString(R.string.remove_appicon_dot)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_desktop)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.LockScreen)
                    summary = getString(R.string.remove_lock_screen_redone)+","+getString(R.string.remove_lock_screen_camera)
                    key = "LockScreen"
                    icon = requireActivity().getAppIcon("com.android.systemui")
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_lockScreen)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.oplus.screenshot"
                    icon = requireActivity().getAppIcon(key)
                    title = getString(R.string.Screenshot)
                    summary = getString(R.string.remove_system_screenshot_delay)+","+getString(R.string.remove_screenshot_privacy_limit)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_screenshot)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.android.packageinstaller"
                    icon = requireActivity().getAppIcon(key)
                    title = getString(R.string.Application)
                    summary = getString(R.string.skip_apk_scan)+","+getString(R.string.unlock_startup_limit)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_application)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "Miscellaneous"
                    icon = requireActivity().getAppIcon("com.android.systemui")
                    title = getString(R.string.Miscellaneous)
                    summary = getString(R.string.Miscellaneous_summary)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_miscellaneous)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    arrayOf("com.oplus.camera","com.oneplus.camera").forEach {
                        if (requireActivity().checkPackName(it)) key = it
                    }
                    icon = requireActivity().getAppIcon(key)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.remove_watermark_word_limit)
                    isVisible = requireActivity().checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_camera)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.oplus.games"
                    icon = requireActivity().getAppIcon(key)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.remove_root_check)
                    isVisible = requireActivity().checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_oplusGames)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.heytap.themestore"
                    icon = requireActivity().getAppIcon(key)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.unlock_themestore_vip)
                    isVisible = requireActivity().checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_themeStore)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.heytap.cloud"
                    icon = requireActivity().getAppIcon(key)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.remove_network_limit)
                    isVisible = requireActivity().checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_cloudService)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.east2d.everyimage"
                    icon = requireActivity().getAppIcon(key)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.skip_startup_page)+","+getString(R.string.vip_download)
                    isVisible = requireActivity().checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_everyimage)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.ruet_cse_1503050.ragib.appbackup.pro"
                    icon = requireActivity().getAppIcon(key)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.remove_pro_license)
                    isVisible = requireActivity().checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_alphaBackupPro)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    key = "com.vphonegaga.titan"
                    icon = requireActivity().getAppIcon(key)
                    title = requireActivity().getAppLabel(key)
                    summary = getString(R.string.enable_gs_vip_function)
                    isVisible = requireActivity().checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_scopeApp_to_GSVirtualMachine)
                        true
                    }
                }
            )
        }
    }
}