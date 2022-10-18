package com.luckyzyx.luckytool.hook.apps.battery

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
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
                "811ab09" -> "f"
                "45704ff" -> "c"
                else -> "gt_mode_status"
            }
        //Source GTModeBroadcastReceiver
        //Search getIntForUser gt_mode_block_message_setting
        findClass("com.oplus.performance.GTModeBroadcastReceiver").hook {
            injectMember {
                method {
                    name = member
                    paramCount = 1
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }
    }
}