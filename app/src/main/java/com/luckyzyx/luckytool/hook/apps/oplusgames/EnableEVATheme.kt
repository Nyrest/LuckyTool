package com.luckyzyx.luckytool.hook.apps.oplusgames

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

class EnableEVATheme : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val clazz = "com.coloros.gamespaceui"
        val member = when (appSet[2]) {
            "5e73d53", "d2010a8" -> arrayOf("m.e0", "I")
            "46a4071" -> arrayOf("m.e0","H")
            "1e58f62" -> arrayOf("m.e0","u")
            "8c399bb" -> arrayOf("m.c0","t")
            "d664479" -> arrayOf("m.a0","t")
            "ddf7681" -> arrayOf("m.c0","t")
            else -> arrayOf("Class", "EVA")
        }
        //Source SystemPropertiesHelper
        //Search isEvaThemePhone
        findClass("$clazz.${member[0]}").hook {
            injectMember {
                method {
                    name = member[1]
                    returnType = BooleanType
                }
                replaceToTrue()
            }
        }
    }
}