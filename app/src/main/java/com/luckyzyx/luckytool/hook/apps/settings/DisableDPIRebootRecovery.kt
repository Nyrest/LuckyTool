package com.luckyzyx.luckytool.hook.apps.settings

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

class DisableDPIRebootRecovery : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val member = when (appSet[2]) {
            //C13
            "a6de75c" -> arrayOf("fi.n", "A0")
            "9470266" -> arrayOf("gi.n", "A0")
            //C12
            "cd12d6b" -> arrayOf("uf.m", "l0")
            "4bb0ba5" -> arrayOf("uf.l", "n0")
            "feb9e19" -> arrayOf("uf.l", "m0")
            "1801866" -> arrayOf("tf.l", "n0")
            else -> arrayOf("DPI_Lock", "lock")
        }
        // Class OplusDensityPreference -> display_density_forced
        // Source CustomizeFeatureUtils
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