package com.luckyzyx.colorosext.ui.fragment

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.*
import com.luckyzyx.colorosext.R
import com.luckyzyx.colorosext.ui.activity.MainActivity
import com.luckyzyx.colorosext.utils.SettingsPreference
import com.luckyzyx.colorosext.utils.getColorOSVersion

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().findViewById<TextView>(R.id.xposed_info).text = getColorOSVersion
    }
}
class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SettingsPreference
        preferenceScreen = initScreen()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    private fun initScreen(): PreferenceScreen {
        return preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    setTitle(R.string.theme_title)
                    setSummary(R.string.theme_title_summer)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    key = "use_md3"
                    setDefaultValue(false)
                    setTitle(R.string.use_material3)
                    setSummary(R.string.use_material3_summer)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    setTitle(R.string.about_title)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    setTitle(R.string.author)
                    setSummary(R.string.author_summer)
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("coolmarket://u/1930284"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    setTitle(R.string.update_url)
                    setSummary(R.string.update_url_summer)
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("coolmarket://apk/com.luckyzyx.colorosext"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    setTitle(R.string.open_source)
                    setSummary(R.string.open_source_summer)
                    isIconSpaceReserved = false
                    fragment = "com.luckyzyx.colorosext.ui.fragment.AboutFragment"
                }
            )
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        when (key) {
            "theme_color", "use_md3" -> {
                if (requireActivity().application != null) {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    requireActivity().overridePendingTransition(
                        R.anim.start_anim,
                        R.anim.out_anim
                    )
                    requireActivity().finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }
}

class AboutFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = initScreen()
    }

    private fun initScreen(): PreferenceScreen {
        return preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    setTitle(R.string.open_source)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "Xposed"
                    summary = "rovo89 , Apache License 2.0"
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/rovo89/Xposed"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "LSPosed"
                    summary = "LSPosed , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/LSPosed/LSPosed"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "YukiHookAPI"
                    summary = "fankes , MIT License"
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/fankes/YukiHookAPI"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "ColorOSNotifyIcon"
                    summary = "fankes , AGPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/fankes/ColorOSNotifyIcon"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "ColorOSTool"
                    summary = "Oosl , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/Oosl/ColorOSTool"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "WooBoxForColorOS"
                    summary = "Simplicity-Team , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/Simplicity-Team/WooBoxForColorOS"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "CorePatch"
                    summary = "LSPosed , GPL-2.0 license"
                    isIconSpaceReserved = false
                    intent = Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/LSPosed/CorePatch"))
                }
            )
        }
    }
}