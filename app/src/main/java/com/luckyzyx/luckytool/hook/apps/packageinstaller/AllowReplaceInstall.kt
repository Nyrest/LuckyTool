package com.luckyzyx.luckytool.hook.apps.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.XposedPrefs

class AllowReplaceInstall : YukiBaseHooker() {
    override fun onHook() {
        val member: String = when(prefs(XposedPrefs).getString(packageName)){
            "7bc7db7","e1a2c58","a222497" -> "Q"
            "75fe984","532ffef" -> "P"
            "38477f0" -> "R"
            //d132ce2
            //faec6ba
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