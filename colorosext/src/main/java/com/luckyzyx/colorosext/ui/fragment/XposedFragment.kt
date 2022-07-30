package com.luckyzyx.colorosext.ui.fragment

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.SwitchPreference
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.colorosext.R
import com.luckyzyx.colorosext.databinding.FragmentXposedBinding
import com.luckyzyx.colorosext.ui.refactor.ColorPickerPreference
import com.luckyzyx.colorosext.ui.refactor.setOnHandleBackPressed
import com.luckyzyx.colorosext.utils.XposedPrefs
import com.luckyzyx.colorosext.utils.getAppVersion
import com.luckyzyx.colorosext.utils.toast

class XposedFragment : Fragment() {
    private lateinit var binding: FragmentXposedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handlerBackPressedDispatcher()
    }
    private fun handlerBackPressedDispatcher(){
        setOnHandleBackPressed(false) {
            val fragmentManager = childFragmentManager
            val current = fragmentManager.findFragmentById(R.id.fragment_container)
            if (current !is SystemScope && current !is OtherScope) {
                fragmentManager.popBackStack()
            }else{
                requireActivity().finish()
            }
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentXposedBinding.inflate(inflater)
        initTabs()
        return binding.root
    }

    private fun initTabs() {
        val tabs = binding.tablayout
        tabs.apply {
            addTab(tabs.newTab().setText("系统APP"),0,true)
            addTab(tabs.newTab().setText("其他APP"),1,false)
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when(tab?.position){
                        0 -> switchFragment(SystemScope(),true)
                        1 -> switchFragment(OtherScope(),true)
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    //取消选中
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    //再次选中
                }
            })
        }
        switchFragment(SystemScope(),true)
    }

    private fun switchFragment(fragment: Fragment, returnable: Boolean) {
        val fragmentManager = childFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        if (returnable) {
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            fragmentTransaction.commitNow()
        }
    }

}

class SystemScope : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(requireActivity()).apply {
                    title = "系统框架"
                    key = "android"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, false)
                    setIcon(R.mipmap.android_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeAndroid"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "系统界面"
                    key = "com.android.systemui"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.systemui_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeSystemUI"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "系统桌面"
                    key = "com.android.launcher"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.launcher_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeLauncher"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "时钟"
                    key = "com.coloros.alarmclock"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.alarmclock_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeClock"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "相机"
                    key = "com.oplus.camera"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.camera_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeCamera"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "主题商店"
                    key = "com.heytap.themestore"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.themestore_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeThemeStore"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "应用包安装程序"
                    key = "com.android.packageinstaller"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.packageinstaller_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopePackageInstall"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "游戏助手"
                    key = "com.oplus.games"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.oplusgames_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeOplusGames"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "安全中心"
                    key = "com.oplus.safecenter"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.securecenter_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeSafeCenter"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "云服务"
                    key = "com.heytap.cloud"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, true)
                    setIcon(R.mipmap.cloudservice_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeCloudService"
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
                    title = "好多动漫"
                    key = "com.east2d.everyimage"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key, false)
                    setIcon(R.mipmap.everyimage_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeEveryimage"
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
                    title = "系统框架"
                    summary = "不生效可尝试重启"
                    key = "Android"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    title = "移除状态栏应用上层通知"
                    summary = "移除状态栏“此应用在其他应用上层显示”"
                    key = "remove_statusbar_top_notification"
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
                    title = "系统界面"
                    summary = "右上角菜单重启作用域"
                    key = "SystemUI"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = "状态栏"
                    summary = "顶部状态栏以及下拉状态栏"
                    key = "StatusBar"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除下拉状态栏时钟红1")
                    key = "remove_statusbar_clock_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("状态栏时钟显秒")
                    key = "statusbar_clock_show_second"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("状态栏时钟显示时段")
                    key = "statusbar_clock_show_period"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("下拉状态栏时钟显秒")
                    key = "statusbar_clock_show_second"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除状态栏开发者选项通知")
                    key = "remove_statusbar_devmode"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除充电完成通知")
                    key = "remove_charging_completed"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("设置状态栏网速刷新率1s")
                    key = "set_network_speed"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除下拉状态栏底部网络警告")
                    key = "remove_statusbar_bottom_networkwarn"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除状态栏支付保护图标")
                    key = "remove_statusbar_securepayment_icon"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    setTitle("锁屏")
                    setSummary("锁屏相关")
                    key = "LockScreen"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除锁屏时钟红1")
                    key = "remove_lock_screen_redone"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除锁屏右下角相机")
                    key = "remove_lock_screen_camera"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                ColorPickerPreference(requireActivity(), XposedPrefs).apply {
                    title = "设置锁屏组件字体颜色"
                    summary = "无需重启作用域设置字体颜色"
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
                    setTitle("系统桌面")
                    setSummary("右上角菜单重启作用域")
                    key = "Launcher"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("解锁后台任务锁定数量")
                    key = "unlock_task_locks"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除APP图标蓝点")
                    key = "remove_appicon_dot"
                    setDefaultValue(false)
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
                    setTitle("时钟")
                    setSummary("右上角菜单重启作用域")
                    key = "AlarmClock"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除桌面时钟组件红一")
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
                    setTitle("相机")
                    setSummary("右上角菜单重启作用域")
                    key = "Camera"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除水印字数限制")
                    key = "remove_watermark_word_limit"
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
                    setTitle("应用包安装程序")
                    setSummary("右上角菜单重启作用域")
                    key = "PackageInstaller"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("跳过Apk扫描")
                    setSummary("Apk病毒扫描相关")
                    key = "skip_apk_scan"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("允许降级安装")
                    setSummary("移除低/相同版本警告等,必须搭配核心破解使用")
                    key = "allow_downgrade_install"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除安装完成广告")
                    setSummary("安全应用推荐/软件商店广告")
                    key = "remove_install_ads"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("替换AOSP安装器")
                    setSummary("替换为原生安装器")
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
                    setTitle("游戏助手")
                    setSummary("右上角菜单重启作用域")
                    key = "OplusGames"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除游戏滤镜检测")
                    setSummary("移除游戏滤镜的Root检测")
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
                    setTitle("云服务")
                    setSummary("右上角菜单重启作用域")
                    key = "CloudService"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("移除数据网络限制")
                    setSummary("移除备份/恢复无法使用移动数据的限制")
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
                    setTitle("主题商店")
                    setSummary("右上角菜单重启作用域")
                    key = "ThemeStore"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("解锁部分功能VIP")
                    setSummary("解锁部分功能,详情页面点击应用")
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
                    setTitle("安全中心")
                    setSummary("右上角菜单重启作用域")
                    key = "SafeCenter"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("解锁自启数量限制")
                    setSummary("解锁应用自启动数量限制")
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
                    setTitle("好多动漫")
                    setSummary("右上角菜单重启作用域")
                    key = "Everyimage"
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("跳过启动页")
                    key = "skip_startup_page"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    setTitle("下载原图")
                    key = "vip_download"
                    setDefaultValue(false)
                    isIconSpaceReserved = false
                }
            )
        }
    }
}