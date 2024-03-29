package com.luckyzyx.luckytool.hook.apps.battery

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

class RemoveHighPerformanceModeNotifications : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val member: String = when (appSet[2]) {
            "811ab09", "e6c5d54", "99a5850", "7c40d0e", "3170adb", "cccc914", "3fd0101", "9b68881" -> "f"
            "45704ff", "8ad4152", "ed3f508", "881457d" -> "c"
            else -> "GTModeStatus"
        }
        //Source GTModeBroadcastReceiver
        //Search getIntForUser gt_mode_block_message_setting
        findClass("com.oplus.performance.GTModeBroadcastReceiver").hook {
            injectMember {
                method {
                    name = member
                    param(ContextClass)
                    paramCount = 1
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }
    }
}