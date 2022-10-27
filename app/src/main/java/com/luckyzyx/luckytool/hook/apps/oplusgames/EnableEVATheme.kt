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
            //7130,7123,7100
            "5e73d53", "50f51f1", "d2010a8", "0d7d77d" -> arrayOf("m.e0", "I")
            "46a4071" -> arrayOf("m.e0", "H")//790
            "1e58f62" -> arrayOf("m.e0", "u")//780
            "ddf7681" -> arrayOf("m.c0", "t")//773
            "8c399bb" -> arrayOf("m.c0", "t")//770
            "d664479" -> arrayOf("m.a0", "t")//760
            else -> arrayOf("NoClass", "NoEVA")
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