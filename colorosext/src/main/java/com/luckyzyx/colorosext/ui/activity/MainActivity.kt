package com.luckyzyx.colorosext.ui.activity

import android.annotation.SuppressLint
import android.app.UiModeManager
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.luckyzyx.colorosext.R
import com.luckyzyx.colorosext.ui.fragment.HomeFragment
import com.luckyzyx.colorosext.ui.fragment.MagiskFragment
import com.luckyzyx.colorosext.ui.fragment.SettingsFragment
import com.luckyzyx.colorosext.ui.fragment.XposedFragment
import com.luckyzyx.colorosext.utils.SPUtils
import com.luckyzyx.colorosext.utils.SettingsPreference
import com.luckyzyx.colorosext.utils.ShellUtils


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkTheme(this)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) switchFragment(HomeFragment(),false)

        initMaterialToolbar()
        initBottomNavigationView()

    }

    private fun initMaterialToolbar() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    private fun checkTheme(context: Context) {
        if (SPUtils.getBoolean(context, SettingsPreference, "use_md3", false)) {
            context.setTheme(R.style.Theme_ColorOSExt_MD3)
        } else {
            context.setTheme(R.style.Theme_ColorOSExt)
        }
    }

    @Suppress("unused")
    fun isDarkMode(context: Context, isSystem: Boolean): Boolean {
        return if (isSystem) {
            val uiModeManager =
                context.getSystemService(UI_MODE_SERVICE) as UiModeManager
            uiModeManager.nightMode == UiModeManager.MODE_NIGHT_YES
        } else {
            val uiMode = context.resources.configuration.uiMode
            uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(0, 1, 0, "重启").setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        )
        menu.add(0, 2, 0, "设置").setIcon(R.drawable.ic_baseline_settings_24).setShowAsActionFlags(
            MenuItem.SHOW_AS_ACTION_IF_ROOM
        )
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == 1) refreshmode(this)
        if (item.itemId == 2) switchFragment(SettingsFragment(), true)
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("NonConstantResourceId")
    private fun initBottomNavigationView() {
        switchFragment(HomeFragment(), false)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNavigationView.selectedItemId = R.id.nav_item_home
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
        bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_item_home -> switchFragment(HomeFragment(), false)
                R.id.nav_item_xposed -> switchFragment(XposedFragment(), false)
                R.id.nav_item_magisk -> switchFragment(MagiskFragment(), false)
            }
            true
        }
    }

    private fun switchFragment(fragment: Fragment, backStack: Boolean) {
        val fragmentManager = supportFragmentManager
        val current = fragmentManager.findFragmentById(R.id.fragment_container)
        if (current != null && current.javaClass == fragment.javaClass) return
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        //fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        if (backStack) {
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        } else {
            fragmentTransaction.commitNow()
        }
    }

    private fun refreshmode(context: Context) {
        val list = arrayOf("重启作用域","重启系统", "关闭系统", "Recovery", "BootLoader")
        MaterialAlertDialogBuilder(context)
            .setCancelable(true)
            .setItems(list){ _: DialogInterface, i: Int ->
                when (i) {
                    0 -> restartScope()
                    1 -> ShellUtils.execCommand("reboot", true)
                    2 -> ShellUtils.execCommand("reboot -p", true)
                    3 -> ShellUtils.execCommand("reboot recovery", true)
                    4 -> ShellUtils.execCommand("reboot bootloader", true)
                }
            }.show()
    }

    private fun restartScope(){
        val commands = arrayOf(
            "kill -9 `pgrep systemui`",
            "am force-stop com.android.packageinstaller",
            "am force-stop com.heytap.themestore",
            "am force-stop com.oplus.safecenter",
            "am force-stop com.oplus.games",
            "am force-stop com.oplus.camera",
            "am force-stop com.android.launcher",
            "am force-stop com.heytap.cloud",
            "am force-stop com.coloros.alarmclock",
            "am force-stop com.east2d.everyimage",
            "am force-stop com.chan.cwallpaper"
        )
        MaterialAlertDialogBuilder(this)
            .setMessage("重启除系统框架外的全部作用域?")
            .setPositiveButton("确定") { _: DialogInterface?, _: Int -> ShellUtils.execCommand(commands, true) }
            .setNeutralButton("取消", null)
            .show()
    }
}