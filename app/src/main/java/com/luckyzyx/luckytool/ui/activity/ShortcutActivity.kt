package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.tools.jumpRunningApp
import kotlin.system.exitProcess

@Obfuscate
class ShortcutActivity : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shortcut = intent.extras
        when(shortcut?.getString("Shortcut")){
            "lsposed" -> ShellUtils.execCommand("am start 'intent:#Intent;action=android.intent.action.MAIN;category=org.lsposed.manager.LAUNCH_MANAGER;launchFlags=0x80000;component=com.android.shell/.BugreportWarningActivity;end'",true)
            "oplusGames" -> ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true)
            "processManager" -> jumpRunningApp(this)
            "chargingTest" -> jumpBatteryInfo(this)
        }
        finishAffinity()
        exitProcess(0)
    }
}