package com.luckyzyx.luckytool.ui.activity

import android.app.Activity
import android.os.Bundle
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.jumpRunningApp

@Obfuscate
class ShortcutActivity : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val shortcut = intent.extras
        when(shortcut?.getString("Shortcut")){
            "oplusGames" -> ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true)
            "processManager" -> jumpRunningApp(this)
        }
        finishAffinity()
    }
}