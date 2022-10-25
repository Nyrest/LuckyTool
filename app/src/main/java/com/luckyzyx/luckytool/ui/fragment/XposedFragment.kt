package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.highcapable.yukihookapi.hook.xposed.prefs.ui.ModulePreferenceFragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.ui.activity.MainActivity
import com.luckyzyx.luckytool.utils.tools.*
import rikka.core.util.ResourceUtils
import java.util.*

class XposedFragment : ModulePreferenceFragment() {
    override fun onCreatePreferencesInModuleApp(savedInstanceState: Bundle?, rootKey: String?) {
        setHasOptionsMenu(true)
        preferenceScreen = preferenceManager.createPreferenceScreen(requireActivity()).apply {
            addPreference(
                Preference(context).apply {
                    key = "android"
                    context.getXPIcon(android.R.mipmap.sym_def_app_icon) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = context.getAppLabel(key)
                    summary = getString(R.string.corepatch)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_android,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "StatusBar"
                    context.getXPIcon("com.android.systemui") { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = getString(R.string.StatusBar)
                    summary = getString(R.string.StatusBarNotice)+","+getString(R.string.StatusBarIcon)+","+getString(R.string.StatusBarClock)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_statusBar,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.android.launcher"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = getString(R.string.Desktop)
                    summary = getString(R.string.launcher_layout_row_colume)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_desktop,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "LockScreen"
                    context.getXPIcon("com.android.systemui") { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = getString(R.string.LockScreen)
                    summary = getString(R.string.remove_lock_screen_redone)+","+getString(R.string.remove_lock_screen_bottom_right_camera)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_lockScreen,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.oplus.screenshot"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = getString(R.string.Screenshot)
                    summary = getString(R.string.remove_system_screenshot_delay)+","+getString(R.string.remove_screenshot_privacy_limit)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_screenshot,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.android.packageinstaller"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = getString(R.string.Application)
                    summary = getString(R.string.skip_apk_scan)+","+getString(R.string.unlock_startup_limit)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_application,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "Miscellaneous"
                    context.getXPIcon("com.android.systemui") { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = getString(R.string.Miscellaneous)
                    summary = getString(R.string.Miscellaneous_summary)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_miscellaneous,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    val keys = arrayOf("com.oplus.camera","com.oneplus.camera")
                    key = context.checkKey(key,keys)
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = context.getAppLabel(key)
                    summary = getString(R.string.remove_watermark_word_limit)
                    isVisible = context.checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_camera,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.oplus.games"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = context.getAppLabel(key)
                    summary = getString(R.string.remove_root_check)+","+getString(R.string.enable_developer_page)
                    isVisible = context.checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_oplusGames,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.heytap.themestore"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = context.getAppLabel(key)
                    summary = getString(R.string.unlock_themestore_vip)
                    isVisible = context.checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_themeStore,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.heytap.cloud"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = context.getAppLabel(key)
                    summary = getString(R.string.remove_network_limit)
                    isVisible = context.checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_cloudService,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.east2d.everyimage"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = context.getAppLabel(key)
                    summary = getString(R.string.skip_startup_page)+","+getString(R.string.vip_download)
                    isVisible = context.checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_everyimage,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
            addPreference(
                Preference(context).apply {
                    key = "com.ruet_cse_1503050.ragib.appbackup.pro"
                    context.getXPIcon(key) { resource, show ->
                        icon = resource
                        isIconSpaceReserved = show
                    }
                    title = context.getAppLabel(key)
                    summary = getString(R.string.remove_pro_license)
                    isVisible = context.checkPackName(key)
                    setOnPreferenceClickListener {
                        findNavController().navigate(R.id.action_nav_xposed_to_alphaBackupPro,Bundle().apply {
                            putCharSequence("title_label",title)
                        })
                        true
                    }
                }
            )
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(0, 1, 0, getString(R.string.menu_reboot)).apply {
            setIcon(R.drawable.ic_baseline_refresh_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ResourceUtils.isNightMode(resources.configuration)){
                iconTintList = ColorStateList.valueOf(Color.WHITE)
            }
        }
        menu.add(0, 2, 0, getString(R.string.menu_versioninfo)).apply {
            setIcon(R.drawable.ic_baseline_extension_24)
            setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            if (ResourceUtils.isNightMode(resources.configuration)){
                iconTintList = ColorStateList.valueOf(Color.WHITE)
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
        val nestedScrollView = NestedScrollView(this).apply {
            setPadding(10.dp,20.dp,10.dp,20.dp)
            addView(
                TextView(context).apply {
                    textSize = 16F
                    text = str
                }
            )
        }
        val bottomSheetDialog = BottomSheetDialog(this)
        bottomSheetDialog.setContentView(nestedScrollView)
        bottomSheetDialog.show()
    }
}