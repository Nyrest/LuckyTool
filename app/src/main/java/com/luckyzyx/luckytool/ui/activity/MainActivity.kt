package com.luckyzyx.luckytool.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.highcapable.yukihookapi.hook.factory.modulePrefs
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.ActivityMainBinding
import com.luckyzyx.luckytool.utils.*
import kotlin.system.exitProcess

@Obfuscate
@Suppress("PrivatePropertyName")
class MainActivity : AppCompatActivity() {
    private val KEY_PREFIX = MainActivity::class.java.name + '.'
    private val EXTRA_SAVED_INSTANCE_STATE = KEY_PREFIX + "SAVED_INSTANCE_STATE"

    private lateinit var binding: ActivityMainBinding

    private fun newIntent(context: Context): Intent {
        return Intent(context, MainActivity::class.java)
    }

    private fun newIntent(savedInstanceState: Bundle, context: Context): Intent {
        return newIntent(context).putExtra(EXTRA_SAVED_INSTANCE_STATE, savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ThemeUtil(this).isDynamicColor()){
            DynamicColors.applyToActivityIfAvailable(this)
        }
        binding = ActivityMainBinding.inflate(layoutInflater)

        checkTheme()
        setContentView(binding.root)

        checkPrefsRW()
        initNavigationFragment()
    }

    private fun initNavigationFragment(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController: NavController = navHostFragment.navController
        val bottomNavigationView = binding.navView
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun checkTheme() {
        when(SPUtils.getString(this, SettingsPrefs,"dark_theme","MODE_NIGHT_FOLLOW_SYSTEM")){
            "MODE_NIGHT_FOLLOW_SYSTEM" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "MODE_NIGHT_NO" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "MODE_NIGHT_YES" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("WorldReadableFiles")
    private fun checkPrefsRW() {
        try {
            getSharedPreferences(SettingsPrefs, MODE_WORLD_READABLE)
            getSharedPreferences(XposedPrefs, MODE_WORLD_READABLE)
            getSharedPreferences(MagiskPrefs, MODE_WORLD_READABLE)
        } catch (ignored: SecurityException) {
            MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setMessage(getString(R.string.unsupported_xposed))
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int -> exitProcess(0) } //.setNegativeButton(R.string.ignore, null)
                .show()
        }
    }

    private val isParasitic get() = !Process.isApplicationUid(Process.myUid())

    @Suppress("DEPRECATION")
    fun restart() {
        if (BuildCompat.isAtLeastS() || isParasitic) {
            recreate()
        } else {
            try {
                val savedInstanceState = Bundle()
                onSaveInstanceState(savedInstanceState)
                finish()
                startActivity(newIntent(savedInstanceState, this))
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            } catch (e: Throwable) {
                recreate()
            }
        }
    }

    fun restartScope(context: Context) {
        val xposedScope = resources.getStringArray(R.array.xposed_scope)
        val commands = ArrayList<String>()
        for (scope in xposedScope) {
            if (scope == "android") continue
            if (scope == "com.android.systemui") {
                commands.add("kill -9 `pgrep systemui`")
                continue
            }
            commands.add("am force-stop $scope")
        }
        //Fix status bar display seconds
        val result = safeOf(default = "error"){
            if (SPUtils.getBoolean(context, XposedPrefs,"statusbar_clock_show_second",false)) {
                if(ShellUtils.execCommand("settings get secure clock_seconds",true,true).successMsg.toInt() != 1){
                    commands.add("settings put secure clock_seconds 1")
                }
            }
        }
        if (result == "error") toast(context,"Error:getShowSecondResult")
        MaterialAlertDialogBuilder(context)
            .setMessage(getString(R.string.restart_scope_message))
            .setPositiveButton(getString(android.R.string.ok)) { _: DialogInterface?, _: Int ->
                modulePrefs.clearCache()
                ShellUtils.execCommand(commands, true)
            }
            .setNeutralButton(getString(android.R.string.cancel), null)
            .show()
    }
}