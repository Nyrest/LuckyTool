package com.luckyzyx.luckytool.hook.apps.battery

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class UnlockStartupLimitV13 : YukiBaseHooker() {
    override fun onHook() {
        //Source StartupManager
        //Search -> ? 5 : 20; -1 -> Method
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray()
        val clazz = when(appSet[2]){
            "6f0072e" -> "i7.b"
            "51b4747" -> "q7.b"
            "aca22d0" -> "u7.b"
            else -> "u7.b"
        }
        clazz.hook {
            injectMember {
                method {
                    name = "g"
                    emptyParam()
                    returnType = IntType
                }
                replaceTo(1000)
            }
        }
    }
}