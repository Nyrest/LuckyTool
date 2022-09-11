package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.*
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.YukiHookAPI
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
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
            MaterialAlertDialogBuilder(requireActivity())
                .setCancelable(true)
                .setItems(requireActivity().getFpsMode()) { _, which ->
                    val fpsList = ArrayList<String>()
                    fpsList.add(requireActivity().getFpsMode().size.toString())
                    if (requireActivity().getFpsMode().lastIndex == which) {
                        ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 -1", true)
                        fpsList.add("-1")
                    }else {
                        ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 $which", true)
                        fpsList.add(which.toString())
                    }
                    requireActivity().putStringSet(XposedPrefs,"current_fps",fpsList.toSet())
                }
                .show()
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
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        ).apply {
            if (ResourceUtils.isNightMode(resources.configuration)){
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.menu_settings)).setIcon(R.drawable.ic_baseline_settings_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        ).apply {
            if (ResourceUtils.isNightMode(resources.configuration)){
                this.iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) refreshmode(requireActivity())
        if (item.itemId == 2) requireActivity().findNavController(R.id.nav_host_fragment_container).navigate(R.id.action_homeFragment_to_settingsFragment)
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

@Obfuscate
class SettingsFragment : PreferenceFragmentCompat(), OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = SettingsPrefs
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                PreferenceCategory(requireActivity()).apply {
                    setTitle(R.string.theme_title)
                    setSummary(R.string.theme_title_summary)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    key = "use_dynamic_color"
                    setDefaultValue(false)
                    setTitle(R.string.use_dynamic_color)
                    setSummary(R.string.use_dynamic_color_summary)
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
                    title = getString(R.string.other_settings)
                    isIconSpaceReserved = false
                }
            )
            addPreference(
                SwitchPreference(requireActivity()).apply {
                    key = "hide_desktop_appicon"
                    setDefaultValue(false)
                    title = getString(R.string.hide_desktop_appicon)
                    summary = getString(R.string.hide_desktop_appicon_summary)
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
                    title = getString(R.string.donate)
                    summary = getString(R.string.donate_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        val donateList = arrayOf(getString(R.string.qq),getString(R.string.wechat),getString(
                                                    R.string.alipay))
                        MaterialAlertDialogBuilder(requireActivity())
                            .setItems(donateList) { _, which ->
                                when (which) {
                                    0 -> {
                                        MaterialAlertDialogBuilder(
                                            requireActivity(),
                                            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
                                        )
                                            .setTitle(getString(R.string.qq))
                                            .setView(
                                                ImageView(requireActivity()).apply {
                                                    setPadding(20.dp)
                                                    setImageBitmap(baseDecode(Base64().qqCode))
                                                }
                                            )
                                            .show()
                                    }
                                    1 -> {
                                        MaterialAlertDialogBuilder(
                                            requireActivity(),
                                            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
                                        )
                                            .setTitle(getString(R.string.wechat))
                                            .setView(
                                                ImageView(requireActivity()).apply {
                                                    setPadding(20.dp)
                                                    setImageBitmap(baseDecode(Base64().wechatCode))
                                                }
                                            )
                                            .show()
                                    }
                                    2 -> {
                                        MaterialAlertDialogBuilder(
                                            requireActivity(),
                                            com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered
                                        )
                                            .setTitle(getString(R.string.alipay))
                                            .setView(
                                                ImageView(requireActivity()).apply {
                                                    setPadding(20.dp)
                                                    setImageBitmap(baseDecode(Base64().alipayCode))
                                                }
                                            )
                                            .show()
                                    }
                                }
                            }
                            .show()
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.feedback_download)
                    summary = getString(R.string.feedback_download_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        val updatelist = arrayOf(getString(R.string.coolmarket),getString(R.string.telegram_channel),getString(R.string.telegram_group),getString(R.string.lsposed_repo))
                        MaterialAlertDialogBuilder(requireActivity())
                            .setItems(updatelist) { _, which ->
                                when (which) {
                                    0 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("coolmarket://u/1930284")))
                                    1 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/LuckyTool")))
                                    2 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/+F42pfv-c0h4zNDc9")))
                                    3 -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://modules.lsposed.org/module/com.luckyzyx.luckytool")))
                                }
                            }.show()
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    title = getString(R.string.participate_translation)
                    summary = getString(R.string.participate_translation_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://crwd.in/luckytool")))
                        true
                    }
                }
            )
            addPreference(
                Preference(requireActivity()).apply {
                    setTitle(R.string.open_source)
                    setSummary(R.string.open_source_summary)
                    isIconSpaceReserved = false
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_settingsFragment_to_sourceFragment)
                        true
                    }
                }
            )
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "use_dynamic_color") (activity as MainActivity).restart()
        if (key == "dark_theme") (activity as MainActivity).restart()
        if (key == "hide_desktop_appicon") sharedPreferences?.let { requireActivity().setDesktopIcon(it.getBoolean("hide_desktop_appicon",false)) }
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

@Obfuscate
class SourceFragment : ModulePreferenceFragment() {
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
            addPreference(
                Preference(requireActivity()).apply {
                    title = "Disable-FLAG_SECURE"
                    summary = "VarunS2002 , GPL-3.0 license"
                    isIconSpaceReserved = false
                    intent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/VarunS2002/Xposed-Disable-FLAG_SECURE")
                    )
                }
            )
        }
    }
}