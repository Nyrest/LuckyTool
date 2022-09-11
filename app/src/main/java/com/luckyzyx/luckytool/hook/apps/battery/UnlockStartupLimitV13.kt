package com.luckyzyx.luckytool.hook.apps.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

class UnlockStartupLimitV13 : YukiBaseHooker() {
    override fun onHook() {
        //Source StartupManager
        //Search -> ? 5 : 20; -1 -> Method
        val appSet = prefs(XposedPrefs).getStringSet(packageName, HashSet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val clazz = when (appSet[2]) {
            "6f0072e" -> "i7.b"
            "51b4747" -> "q7.b"
            "aca22d0", "b509955" -> "u7.b"
            "6a8ec66" -> "y7.b"
            else -> "StartupManager"
        }
        findClass(clazz).hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }
                replaceTo(1000)
            }
        }
    }
}