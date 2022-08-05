package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.databinding.FragmentHomeBinding
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.*
import com.luckyzyx.luckytool.R

@Obfuscate
class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var enableModule: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater)
        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableModule = SPUtils.getBoolean(requireActivity(), XposedPrefs,"enable_module",false)

        if (YukiHookAPI.Status.isXposedModuleActive && enableModule){
            binding.statusIcon.setImageResource(R.drawable.ic_round_check_24)
            binding.statusTitle.text = "模块已激活"
        }else{
            binding.statusCard.setCardBackgroundColor(Color.GRAY)
            binding.statusIcon.setImageResource(R.drawable.ic_round_warning_24)
            binding.statusTitle.text = "模块未激活"
        }

        binding.statusSummary.apply {
            text = "模块版本:"
            text = "$text$getBuildVersion"
        }
        binding.enableModule.apply {
            text = "启用模块"
            isChecked = enableModule
        }.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                SPUtils.putBoolean(requireActivity(), XposedPrefs,"enable_module",isChecked)
                (activity as MainActivity).restart()
            }
        }
        binding.fpsTitle.text = getString(R.string.fps_title)
        binding.fpsSummary.text = getString(R.string.fps_summer)

        binding.fps.setOnClickListener {
            MaterialAlertDialogBuilder(requireActivity())
                .setCancelable(true)
                .setItems(getFpsMode()) { _, which ->
                    ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 $which", true)
                }
                .show()
        }
        binding.systemInfo.text =
            """
                ${getString(R.string.brand)}: ${Build.BRAND}
                ${getString(R.string.model)}: ${Build.MODEL}
                ${getString(R.string.system)}: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT})
                ${getString(R.string.device)}: ${Build.DEVICE}
                ${getString(R.string.build_version)}: ${Build.DISPLAY}
                ${getString(R.string.flash)}: ${ShellUtils.execCommand("cat /sys/class/block/sda/device/inquiry", true, true).successMsg}
            """.trimIndent()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        )
        menu.add(0, 2, 0, getString(R.string.menu_settings)).setIcon(R.drawable.ic_baseline_settings_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) refreshmode(requireActivity())
        if (item.itemId == 2) requireActivity().findNavController(R.id.nav_host_fragment_container).navigate(R.id.action_homeFragment_to_settingsFragment)
        return super.onOptionsItemSelected(item)
    }

    private fun refreshmode(context: Context) {
        val list = arrayOf(getString(R.string.restart_scope),getString(R.string.reboot), getString(R.string.reboot_shutdown), getString(R.string.reboot_recovery), getString(
                    R.string.reboot_bootloader))
        MaterialAlertDialogBuilder(context)
            .setCancelable(true)
            .setItems(list) { _: DialogInterface?, i: Int ->
                when (i) {
                    0 -> (activity as MainActivity).restartScope(requireActivity())
                    1 -> ShellUtils.execCommand("reboot", true)
                    2 -> ShellUtils.execCommand("reboot -p", true)
                    3 -> ShellUtils.execCommand("reboot recovery", true)
                    4 -> ShellUtils.execCommand("reboot bootloader", true)
                }
            }
            .show()
    }
}

class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SettingsPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    setTitle(R.string.theme_title)
                    setSummary(R.string.theme_title_summer)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    key = "use_dynamic_color"
                    setDefaultValue(false)
                    setTitle(R.string.use_dynamic_color)
                    setSummary(R.string.use_dynamic_color_summer)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                DropDownPreference(requireActivity()).apply {
                    key = "dark_theme"
                    title = getString(R.string.dark_theme)
                    summary = "%s"
                    entries = resources.getStringArray(R.array.dark_theme)
                    entryValues = resources.getStringArray(R.array.dark_theme_value)
                    setDefaultValue("MODE_NIGHT_FOLLOW_SYSTEM")
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
                    setTitle(R.string.contact_details)
                    setSummary(R.string.contact_details_summer)
                    isIconSpaceReserved = false
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        val updatelist = arrayOf(getString(R.string.telegram_channel),getString(R.string.telegram_group),getString(R.string.coolmarket))
                        MaterialAlertDialogBuilder(requireActivity())
                            .setItems(updatelist) { _, which ->
                                when (which) {
                                    0 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/LuckyTool")))
                                    1 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+F42pfv-c0h4zNDc9")))
                                    2 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("coolmarket://u/1930284")))
                                }
                            }.show()
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    setTitle(R.string.download_url)
                    setSummary(R.string.download_url_summer)
                    isIconSpaceReserved = false
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        val updatelist = arrayOf(getString(R.string.coolmarket),getString(R.string.github_repo))
                        MaterialAlertDialogBuilder(requireActivity())
                            .setItems(updatelist) { _, which ->
                                when (which) {
                                    0 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("coolmarket://apk/com.luckyzyx.luckytool")))
                                    1 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/luckyzyx/LuckyTool/releases")))
                                }
                            }.show()
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.github_repo)
                    summary = getString(R.string.github_repo_summer)
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/luckyzyx/LuckyTool")
                    )
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    setTitle(R.string.open_source)
                    setSummary(R.string.open_source_summer)
                    isIconSpaceReserved = false
                    onPreferenceClickListener = Preference.OnPreferenceClickListener {
                        requireActivity().findNavController(R.id.nav_host_fragment_container).navigate(R.id.action_settingsFragment_to_aboutFragment)
                        true
                    }
                }
            )
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "use_dynamic_color") (activity as MainActivity).restart()
        if (key == "dark_theme") (activity as MainActivity).restart()
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

class AboutFragment : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
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
                    intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/rovo89/Xposed"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "LSPosed"
                    summary = "LSPosed , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/LSPosed/LSPosed"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "YukiHookAPI"
                    summary = "fankes , MIT License"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/fankes/YukiHookAPI")
                    )
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "ColorOSNotifyIcon"
                    summary = "fankes , AGPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/fankes/ColorOSNotifyIcon")
                    )
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "ColorOSTool"
                    summary = "Oosl , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent =
                        Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Oosl/ColorOSTool"))
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "WooBoxForColorOS"
                    summary = "Simplicity-Team , GPL-3.0 License"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/Simplicity-Team/WooBoxForColorOS")
                    )
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = "CorePatch"
                    summary = "LSPosed , GPL-2.0 license"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/LSPosed/CorePatch")
                    )
                }
            )
        }
    }
}