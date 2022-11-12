package com.luckyzyx.luckytool.ui.activity

import android.content.ComponentName
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.os.Bundle
import android.os.Process
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.BuildCompat
import androidx.core.widget.NestedScrollView
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.DynamicColors
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.highcapable.yukihookapi.hook.factory.modulePrefs
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.ActivityMainBinding
import com.luckyzyx.luckytool.utils.tools.*
import kotlin.system.exitProcess

@Obfuscate
@Suppress("PrivatePropertyName")
open class MainActivity : AppCompatActivity() {
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
        checkTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationFragment()
        initDynamicShortcuts()
//        initAgreement(true)

        checkPrefsRW()

    }

    open fun initAgreement(status: Boolean) {
        if (status && getBoolean(SettingsPrefs,"read_agreement",false)) return
        MaterialAlertDialogBuilder(this).apply {
            setView(
                NestedScrollView(context).apply {
                    addView(
                        MaterialTextView(context).apply {
                            setPadding(20.dp, 20.dp, 20.dp, 0)
                            val userAgreement = "<a href='https://gitee.com/luckyzyx/luckyzyx/raw/master/UserAgreement.md'>《用户协议》</a>"
                            val privacyPolicy = "<a href='https://gitee.com/luckyzyx/luckyzyx/raw/master/PrivacyAgreement.md'>《隐私政策》</a>"
                            text = Html.fromHtml("${getString(R.string.read_agreement)} $userAgreement $privacyPolicy",0)
                            movementMethod = LinkMovementMethod.getInstance()
                        }
                    )
                }
            )
            setCancelable(false)
            setPositiveButton(android.R.string.ok) { _, _ ->
                putBoolean(SettingsPrefs,"read_agreement",true)
            }
            setNeutralButton(android.R.string.cancel) { _, _ ->
                putBoolean(SettingsPrefs,"read_agreement",false)
                exitProcess(0)
            }
            show()
        }
    }

    private fun initDynamicShortcuts(){
        val status = packageManager.getComponentEnabledSetting(ComponentName(packageName, "${packageName}.Hide"))
        if(status == 2) return
        val shortcutManager = getSystemService(ShortcutManager::class.java)
        val lsposed = ShortcutInfo.Builder(this, "lsposed").apply {
            setShortLabel("LSPosed")
            setIcon(Icon.createWithResource(packageName,R.mipmap.android_icon))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut","lsposed")
            intent.setClassName(packageName,"$packageName.ui.activity.ShortcutActivity")
            setIntent(intent)
        }.build()
        val oplusGames = ShortcutInfo.Builder(this, "oplusGames").apply {
            setShortLabel(getAppLabel("com.oplus.games").toString())
            setIcon(Icon.createWithResource(packageName,R.mipmap.oplusgames_icon))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut","oplusGames")
            intent.setClassName("com.oplus.games","business.compact.activity.GameBoxCoverActivity")
            setIntent(intent)
        }.build()
        val chargingTest = ShortcutInfo.Builder(this, "chargingTest").apply {
            setShortLabel(getString(R.string.charging_test))
            setIcon(Icon.createWithResource(packageName,R.drawable.ic_baseline_charging_station_24))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut","chargingTest")
            intent.setClassName(packageName,"$packageName.ui.activity.ShortcutActivity")
            setIntent(intent)
        }.build()
        val processManager = ShortcutInfo.Builder(this, "processManager").apply {
            setShortLabel(getString(R.string.process_manager))
            setIcon(Icon.createWithResource(packageName,R.mipmap.android_icon))
            val intent = Intent(Intent.ACTION_VIEW)
            intent.putExtra("Shortcut","processManager")
            intent.setClassName(packageName,"$packageName.ui.activity.ShortcutActivity")
            setIntent(intent)
        }.build()
        shortcutManager.dynamicShortcuts = listOf(lsposed,oplusGames,chargingTest,processManager)
    }

    private fun initNavigationFragment(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_other,
            R.id.nav_function,
            R.id.nav_home,
            R.id.nav_log    ,
            R.id.nav_setting,
        ).build()
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController,appBarConfiguration)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val bottomNavigationView = binding.navView
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
        bottomNavigationView.setupWithNavController(navController)
    }

    private fun checkTheme() {
        if (ThemeUtil(this).isDynamicColor()){
            DynamicColors.applyToActivityIfAvailable(this)
        }
        when(getString(SettingsPrefs,"dark_theme","MODE_NIGHT_FOLLOW_SYSTEM")){
            "MODE_NIGHT_FOLLOW_SYSTEM" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            "MODE_NIGHT_NO" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "MODE_NIGHT_YES" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }

    @Suppress("DEPRECATION")
    private fun checkPrefsRW() {
        try {
            getSharedPreferences(SettingsPrefs, MODE_WORLD_READABLE)
            getSharedPreferences(XposedPrefs, MODE_WORLD_READABLE)
            getSharedPreferences(MagiskPrefs, MODE_WORLD_READABLE)
            getSharedPreferences(OtherPrefs, MODE_WORLD_READABLE)
        } catch (ignored: SecurityException) {
            MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setMessage(getString(R.string.unsupported_xposed))
                .setPositiveButton(android.R.string.ok) { _: DialogInterface?, _: Int -> exitProcess(0) }
                //.setNegativeButton(R.string.ignore, null)
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
            commands.add("killall $scope")
            commands.add("am force-stop $scope")
            context.getAppVersion(scope)
        }
        safeOfNull {
            if (getBoolean(XposedPrefs, "statusbar_clock_enable", false) && getBoolean(XposedPrefs, "statusbar_clock_show_second", false)) {
                commands.add("settings put secure clock_seconds 0")
            } else {
                commands.add("settings put secure clock_seconds 0")
            }
        }
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