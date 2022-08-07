package com.luckyzyx.luckytool.ui.fragment

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentXposedBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.ui.refactor.ColorPickerPreference
import com.luckyzyx.luckytool.utils.XposedPrefs
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
                            navController.navigate(R.id.systemScope)
                        }
                        1 -> {
                            navController.popBackStack()
                            navController.navigate(R.id.otherScope)
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) (activity as MainActivity).restartScope(requireActivity())
        return super.onOptionsItemSelected(item)
    }
}

class SystemScope : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Android)
                    key = "android"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, false)
                    setIcon(R.mipmap.android_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeAndroid)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.SystemUI)
                    key = "com.android.systemui"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.systemui_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeSystemUI)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Launcher)
                    key = "com.android.launcher"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.launcher_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeLauncher)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.AlarmClock)
                    key = "com.coloros.alarmclock"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.alarmclock_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeClock)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Camera)
                    key = "com.oplus.camera"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.camera_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeCamera)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.ThemeStore)
                    key = "com.heytap.themestore"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.themestore_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeThemeStore)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.PackageInstaller)
                    key = "com.android.packageinstaller"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.packageinstaller_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopePackageInstall)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.OplusGames)
                    key = "com.oplus.games"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.oplusgames_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeOplusGames)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.SafeCenter)
                    key = "com.oplus.safecenter"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.securecenter_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeSafeCenter)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Screenshot)
                    key = "com.oplus.screenshot"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.screenshot_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeScreenshot)
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.CloudService)
                    key = "com.heytap.cloud"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.cloudservice_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_systemScope_to_scopeCloudService)
                        true
                    }
                }
            )
        }
    }
}

class OtherScope : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.Everyimage)
                    key = "com.east2d.everyimage"
                    summary = getString(R.string.version_summer_first) + getAppVersion(requireActivity(), key, false)
                    setIcon(R.mipmap.everyimage_icon)
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.xposed_fragment_container).navigate(R.id.action_otherScope_to_scopeEveryimage)
                        true
                    }
                }
            )
        }
    }
}

class ScopeAndroid : ModulePreferenceFragment(),
    OnSharedPreferenceChangeListener {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.Android)
                    summary = getString(R.string.android_summer)
                    key = "Android"
                    isIconSpaceReserved = false
                }
            )
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
                    title = getString(R.string.remove_statusbar_top_notification)
                    summary = getString(R.string.remove_statusbar_top_notification_summer)
                    key = "remove_statusbar_top_notification"
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

class ScopeSystemUI : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.SystemUI)
                    summary = getString(R.string.restart_scope_summer)
                    key = "SystemUI"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.StatusBarClock)
                    summary = getString(R.string.StatusBarClock_summer)
                    key = "StatusBarClock"
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
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.StatusBarNotice)
                    summary = getString(R.string.StatusBarNotice_summer)
                    key = "StatusBarNotice"
                    isIconSpaceReserved = false
                }
            )
//            addPreference(
//                SwitchPreference(requireActivity()).apply {
//                    title = getString(R.string.doubletap_statusbar_lockscreen)
//                    key = "doubletap_statusbar_lockscreen"
//                    setDefaultValue(false)
//                    isIconSpaceReserved = false
//                }
//            )
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
                    title = getString(R.string.remove_statusbar_bottom_networkwarn)
                    key = "remove_statusbar_bottom_networkwarn"
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
                    title = getString(R.string.LockScreen)
                    summary = getString(R.string.LockScreen_summer)
                    key = "LockScreen"
                    isIconSpaceReserved = false
                }
            )
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

class ScopeLauncher : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.Launcher)
                    summary = getString(R.string.restart_scope_summer)
                    key = "Launcher"
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

class ScopeClock : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.AlarmClock)
                    summary = getString(R.string.restart_scope_summer)
                    key = "AlarmClock"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = getString(R.string.remove_alarmclock_widget_redone)
                    key = "remove_alarmclock_widget_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class ScopeCamera : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.Camera)
                    summary = getString(R.string.restart_scope_summer)
                    key = "Camera"
                    isIconSpaceReserved = false
                }
            )
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

class ScopeScreenshot : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.Screenshot)
                    summary = getString(R.string.restart_scope_summer)
                    key = "Screenshot"
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

class ScopePackageInstall : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.PackageInstaller)
                    summary = getString(R.string.restart_scope_summer)
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

class ScopeOplusGames : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.OplusGames)
                    summary = getString(R.string.restart_scope_summer)
                    key = "OplusGames"
                    isIconSpaceReserved = false
                }
            )
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

class ScopeCloudService : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.CloudService)
                    summary = getString(R.string.restart_scope_summer)
                    key = "CloudService"
                    isIconSpaceReserved = false
                }
            )
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

class ScopeThemeStore : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.ThemeStore)
                    summary = getString(R.string.restart_scope_summer)
                    key = "ThemeStore"
                    isIconSpaceReserved = false
                }
            )
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

class ScopeSafeCenter : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.SafeCenter)
                    summary = getString(R.string.restart_scope_summer)
                    key = "SafeCenter"
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
        }
    }
}

class ScopeEveryimage : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = getString(R.string.Everyimage)
                    summary = getString(R.string.restart_scope_summer)
                    key = "Everyimage"
                    isIconSpaceReserved = false
                }
            )
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