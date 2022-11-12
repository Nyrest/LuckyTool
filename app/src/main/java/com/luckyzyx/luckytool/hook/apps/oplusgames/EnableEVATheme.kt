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
        val oldClass = "com.coloros.gamespaceui"
        val member = when (appSet[2]) {
            //7140,7141,7142
            "cfded43", "46a2960", "bbf8f04" -> arrayOf("ic.e0", "A")
            //7130,7131,7133,7123,7100
            "5e73d53", "50f51f1", "d2010a8", "0d7d77d", "e862acc" -> arrayOf("$oldClass.m.e0", "I")
            "46a4071" -> arrayOf("$oldClass.m.e0", "H")//790
            "1e58f62" -> arrayOf("$oldClass.m.e0", "u")//780
            "ddf7681" -> arrayOf("$oldClass.m.c0", "t")//773
            "8c399bb" -> arrayOf("$oldClass.m.c0", "t")//770
            "d664479" -> arrayOf("$oldClass.m.a0", "t")//760
            else -> arrayOf("NoEVAClass", "NoEVA")
        }
        //Source SystemPropertiesHelper
        //Search isEvaThemePhone
        findClass(member[0]).hook {
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