package com.luckyzyx.colorosext.ui.fragment

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import com.luckyzyx.colorosext.R
import com.luckyzyx.colorosext.utils.XposedPreference
import com.luckyzyx.colorosext.utils.getAppVersion

class XposedFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(requireActivity()).apply {
                    title = "系统框架"
                    key = "android"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.android_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeAndroid"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "系统界面"
                    key = "com.android.systemui"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.systemui_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeSystemUI"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "系统桌面"
                    key = "com.android.launcher"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.launcher_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeLauncher"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "时钟"
                    key = "com.coloros.alarmclock"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.alarmclock_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeClock"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "相机"
                    key = "com.oplus.camera"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.camera_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeCamera"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "主题商店"
                    key = "com.heytap.themestore"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.themestore_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeThemeStore"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "应用包安装程序"
                    key = "com.android.packageinstaller"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.packageinstaller_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopePackageInstall"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "游戏助手"
                    key = "com.oplus.games"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.oplusgames_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeOplusGames"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "安全中心"
                    key = "com.oplus.safecenter"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.securecenter_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeAndroid"
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "云服务"
                    key = "com.heytap.cloud"
                    setSummary(R.string.version_summer_first)
                    summary = summary as String + getAppVersion(requireActivity(), key)
                    setIcon(R.mipmap.cloudservice_icon)
                    fragment = "com.luckyzyx.colorosext.ui.fragment.ScopeAndroid"
                }
            )
        }
    }
}

class ScopeAndroid : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    title = "系统框架"
                    summary = "不生效可尝试重启"
                    isIconSpaceReserved = false
                }
            )
        }
    }
}

class ScopeSystemUI : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopeLauncher : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopeClock : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopeCamera : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopePackageInstall : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopeOplusGames : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopeCloudService : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopeThemeStore : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}

class ScopeSafeCenter : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = XposedPreference
    }
}