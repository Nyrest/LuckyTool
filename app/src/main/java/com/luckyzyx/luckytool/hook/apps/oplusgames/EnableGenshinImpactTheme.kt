package com.luckyzyx.luckytool.hook.apps.oplusgames

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

class EnableGenshinImpactTheme : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val member = when (appSet[2]) {
            "bbf8f04" -> arrayOf("ic.i", "m")
            else -> arrayOf("NoGenshinImpactClass", "NoGenshinImpact")
        }
        //Source FunctionHelper
        //OnePlus Ace Pro Genshin Impact Limited Edition
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