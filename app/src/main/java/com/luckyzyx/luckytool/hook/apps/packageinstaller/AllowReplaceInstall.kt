package com.luckyzyx.luckytool.hook.apps.packageinstaller

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

class AllowReplaceInstall : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val member: String = when(appSet[2]){
            "7bc7db7","e1a2c58","a222497" -> "Q"
            "75fe984","532ffef" -> "P"
            "38477f0" -> "R"
            else -> "isReplaceInstall"
        }
        //Allow replace install,Low/same version warning
        //search ->  ? 1 : 0; -> Method
        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            injectMember {
                method {
                    name = member
                    returnType = BooleanType
                }
                replaceToFalse()
            }
        }
    }
}