package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreference
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentXposedBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.ui.refactor.ColorPickerPreference
import com.luckyzyx.luckytool.utils.XposedPrefs
import com.luckyzyx.luckytool.utils.dp2px
import com.luckyzyx.luckytool.utils.getAppVersion
import rikka.core.util.ResourceUtils

class XposedFragment : Fragment() {
    private lateinit var binding: FragmentXposedBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentXposedBinding.inflate(inflater)
        initToolbarTabs()
        return binding.root
    }

    private fun initToolbarTabs() {
        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.nav_xposed)
        setHasOptionsMenu(true)

        val navHostFragment = childFragmentManager.findFragmentById(R.id.xposed_fragment_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val tabs = binding.tablayout
        tabs.apply {
            addTab(tabs.newTab().setText(requireActivity().getString(R.string.system_app)),0,true)
            addTab(tabs.newTab().setText(requireActivity().getString(R.string.other_app)),1,false)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        0 -> {
                            navController.popBackStack()
                            navController.navigate(R.id.systemApp)
                        }
                        1 -> {
                            navController.popBackStack()
                            navController.navigate(R.id.otherApp)
                        }
                    }
                }
                //取消选中
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                //再次选中
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
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
        if (item.itemId == 2) bottomSheet()
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("SetTextI18n")
    fun bottomSheet(){
        val xposedScope = resources.getStringArray(R.array.xposed_scope)
        var str = getString(R.string.scope_version_info)+"\n"
        for (scope in xposedScope) {
            var iscommit = true
            if (scope == "android") iscommit = false
            if (scope == "com.east2d.everyimage") iscommit = false
            str += "\n$scope\n${getString(R.string.version_first) + getAppVersion(requireActivity(), scope, iscommit)}\n"
        }
        val nestedScrollView = NestedScrollView(requireActivity()).apply {
            setPadding(dp2px(requireActivity(),16F).toInt())
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

class SystemApp : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Android)
                    summary = getString(R.string.corepatch)
                    key = "Android"
                    setIcon(R.mipmap.android_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_system)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.StatusBar)
                    summary = getString(R.string.StatusBarNotice)+","+getString(R.string.StatusBarIcon)+","+getString(R.string.StatusBarTiles)
                    key = "StatusBar"
                    setIcon(R.mipmap.systemui_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_statusBar)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.StatusBarClock)
                    summary = getString(R.string.TopStatusBarClock)+","+getString(R.string.DropDownStatusBarClock)
                    key = "StatusBarClock"
                    setIcon(R.mipmap.alarmclock_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_statusBarClock)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Desktop)
                    summary = getString(R.string.launcher_layout_row_colume)+","+getString(R.string.remove_appicon_dot)
                    key = "Desktop"
                    setIcon(R.mipmap.launcher_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_desktop)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.LockScreen)
                    summary = getString(R.string.remove_lock_screen_redone)+","+getString(R.string.remove_lock_screen_camera)
                    key = "LockScreen"
                    setIcon(R.mipmap.systemui_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_lockScreen)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Screenshot)
                    summary = getString(R.string.remove_system_screenshot_delay)+","+getString(R.string.remove_screenshot_privacy_limit)
                    key = "Screenshot"
                    setIcon(R.mipmap.screenshot_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_screenshot)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Application)
                    summary = getString(R.string.skip_apk_scan)+","+getString(R.string.unlock_startup_limit)
                    key = "Application"
                    setIcon(R.mipmap.packageinstaller_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_application)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Camera)
                    summary = getString(R.string.remove_watermark_word_limit)
                    key = "Camera"
                    setIcon(R.mipmap.camera_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_camera)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.OplusGames)
                    summary = getString(R.string.remove_root_check)
                    key = "OplusGames"
                    setIcon(R.mipmap.oplusgames_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_oplusGames)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.ThemeStore)
                    summary = getString(R.string.unlock_themestore_vip)
                    key = "ThemeStore"
                    setIcon(R.mipmap.themestore_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_themeStore)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.CloudService)
                    summary = getString(R.string.remove_network_limit)
                    key = "CloudService"
                    setIcon(R.mipmap.cloudservice_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemApp_to_cloudService)
                        true
                    }
                }
            )
        }
    }
}

class OtherApp : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Everyimage)
                    key = "com.east2d.everyimage"
                    summary = getString(R.string.version_first)+ getAppVersion(requireActivity(),key)
                    setIcon(R.mipmap.everyimage_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_otherApp_to_everyimage)
                        true
                    }
                }
            )
        }
    }
}

class System : ModulePreferenceFragment(), OnSharedPreferenceChangeListener {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    setTitle(R.string.corepatch)
                    setSummary(R.string.corepatch_summer)
                    key = "CorePatch"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle(R.string.downgr)
                    setSummary(R.string.downgr_summary)
                    key = "downgrade"
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle(R.string.authcreak)
                    setSummary(R.string.authcreak_summary)
                    key = "authcreak"
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle(R.string.digestCreak)
                    setSummary(R.string.digestCreak_summary)
                    key = "digestCreak"
                    setDefaultValue(true)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle(R.string.UsePreSig)
                    setSummary(R.string.UsePreSig_summary)
                    key = "UsePreSig"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle(R.string.enhancedMode)
                    setSummary(R.string.enhancedMode_summary)
                    key = "enhancedMode"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        super.onSharedPreferenceChanged(sharedPreferences, key)
        if (key == "UsePreSig" && sharedPreferences!!.getBoolean(key, false)) {
            MaterialAlertDialogBuilder(requireActivity())
                .setMessage(R.string.usepresig_warn)
                .setPositiveButton("OK", null)
                .show()
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}

class StatusBar : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.StatusBarNotice)
                    summary = getString(R.string.StatusBarNotice_summer)
                    key = "StatusBarNotice"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_statusbar_top_notification)
                    summary = getString(R.string.remove_statusbar_top_notification_summer)
                    key = "remove_statusbar_top_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_vpn_active_notification)
                    summary = getString(R.string.remove_vpn_active_notification_summer)
                    key = "remove_vpn_active_notification"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_statusbar_devmode)
                    key = "remove_statusbar_devmode"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_charging_completed)
                    key = "remove_charging_completed"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.StatusBarIcon)
                    summary = getString(R.string.StatusBarIcon_summer)
                    key = "StatusBarIcon"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_statusbar_battery_percent)
                    key = "remove_statusbar_battery_percent"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.set_network_speed)
                    key = "set_network_speed"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_statusbar_securepayment_icon)
                    key = "remove_statusbar_securepayment_icon"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_statusbar_user_switcher)
                    key = "remove_statusbar_user_switcher"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.StatusBarTiles)
                    summary = getString(R.string.StatusBarTiles_summer)
                    key = "StatusBarTiles"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_statusbar_bottom_networkwarn)
                    key = "remove_statusbar_bottom_networkwarn"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                EditTextPreference(requireActivity()).apply {
                    title = getString(R.string.set_statusbar_tiles_column)
                    summary = getString(R.string.set_statusbar_tiles_column_summer)
                    key = "set_statusbar_tiles_column"
                    dialogMessage = getString(R.string.set_statusbar_tiles_column_dialogmessage)
                    setDefaultValue("")
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class StatusBarClock : ModulePreferenceFragment(){
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.TopStatusBarClock)
                    summary = getString(R.string.TopStatusBarClock_summer)
                    key = "TopStatusBarClock"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.statusbar_clock_enable)
                    key = "statusbar_clock_enable"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.statusbar_clock_show_second)
                    key = "statusbar_clock_show_second"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.statusbar_clock_show_period)
                    key = "statusbar_clock_show_period"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.DropDownStatusBarClock)
                    summary = getString(R.string.DropDownStatusBarClock_summer)
                    key = "DropDownStatusBarClock"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_statusbar_clock_redone)
                    key = "remove_statusbar_clock_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.statusbar_dropdown_clock_show_second)
                    key = "statusbar_dropdown_clock_show_second"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
        preferenceScreen.findPreference<SwitchPreference>("statusbar_clock_show_second")?.dependency = "statusbar_clock_enable"
        preferenceScreen.findPreference<SwitchPreference>("statusbar_clock_show_period")?.dependency = "statusbar_clock_enable"
    }
}

class Desktop : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_alarmclock_widget_redone)
                    key = "remove_alarmclock_widget_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_appicon_dot)
                    key = "remove_appicon_dot"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                EditTextPreference(requireActivity()).apply {
                    title = getString(R.string.launcher_layout_row_colume)
                    summary = getString(R.string.launcher_layout_row_colume_summer)
                    dialogTitle = title
                    dialogMessage = getString(R.string.launcher_layout_row_colume_message)
                    key = "launcher_layout_row_colume"
                    setDefaultValue("")
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class LockScreen : ModulePreferenceFragment(){
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_lock_screen_redone)
                    key = "remove_lock_screen_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_lock_screen_camera)
                    key = "remove_lock_screen_camera"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                ColorPickerPreference(requireActivity(), XposedPrefs).apply {
                    title = getString(R.string.set_lock_screen_textview_color)
                    summary = getString(R.string.set_lock_screen_textview_color_summer)
                    key = "set_lock_screen_textview_color"
                    isIconSpaceReserved = false
                    dialogTitle = title as String
                    cornerRadius = 30
                    neutral = resources.getString(android.R.string.cancel)
                    positive = resources.getString(android.R.string.ok)
                }
            )
        }
    }
}

class Screenshot : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.disable_flag_secure)
                    summary = getString(R.string.disable_flag_secure_summer)
                    key = "disable_flag_secure"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_system_screenshot_delay)
                    summary = getString(R.string.remove_system_screenshot_delay_summer)
                    key = "remove_system_screenshot_delay"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_screenshot_privacy_limit)
                    summary = getString(R.string.remove_screenshot_privacy_limit_summer)
                    key = "remove_screenshot_privacy_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class Application : ModulePreferenceFragment(){
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.ApplyOtherRestrictions)
                    key = "ApplyOtherRestrictions"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.unlock_startup_limit)
                    summary = getString(R.string.unlock_startup_limit_summer)
                    key = "unlock_startup_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.unlock_task_locks)
                    key = "unlock_task_locks"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.AppInstallationRelated)
                    key = "PackageInstaller"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.skip_apk_scan)
                    summary = getString(R.string.skip_apk_scan_summer)
                    key = "skip_apk_scan"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.allow_downgrade_install)
                    summary = getString(R.string.allow_downgrade_install_summer)
                    key = "allow_downgrade_install"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_install_ads)
                    summary = getString(R.string.remove_install_ads_summer)
                    key = "remove_install_ads"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.replase_aosp_installer)
                    summary = getString(R.string.replase_aosp_installer_summer)
                    key = "replase_aosp_installer"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class Camera : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_watermark_word_limit)
                    key = "remove_watermark_word_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class OplusGames : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_root_check)
                    summary = getString(R.string.remove_root_check_summer)
                    key = "remove_root_check"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class CloudService : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_network_limit)
                    summary = getString(R.string.remove_network_limit_summer)
                    key = "remove_network_limit"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class ThemeStore : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.unlock_themestore_vip)
                    summary = getString(R.string.unlock_themestore_vip_summer)
                    key = "unlock_themestore_vip"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class Everyimage : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.skip_startup_page)
                    key = "skip_startup_page"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.vip_download)
                    key = "vip_download"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}