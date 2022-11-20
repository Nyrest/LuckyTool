package com.luckyzyx.luckytool.hook.apps.packageinstaller

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import java.util.*

@Suppress("LocalVariableName")
class SkipApkScan : YukiBaseHooker() {
    override fun onHook() {
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray().apply {
            Arrays.sort(this)
            forEach {
                this[this.indexOf(it)] = it.substring(2)
            }
        }
        val OPIA = "com.android.packageinstaller.oplus.OPlusPackageInstallerActivity"
        val ADRU = "com.android.packageinstaller.oplus.utils.AppDetailRedirectionUtils"
        val member: Array<String> =
            when (appSet[2]) {
                "7bc7db7", "e1a2c58" -> {
                    arrayOf(OPIA, "L", "C", "K")
                }
                "75fe984", "532ffef" -> {
                    arrayOf(OPIA, "L", "D", "i")
                }
                "38477f0" -> {
                    arrayOf(OPIA, "M", "D", "k")
                }
                "a222497" -> {
                    arrayOf(OPIA, "M", "E", "j")
                }
                "d1fd8fc", "890f77b" -> {
                    arrayOf(ADRU, "shouldStartAppDetail", "checkToScanRisk", "initiateInstall")
                }
                //d132ce2,faec6ba,860700c,3d2dbd1
                else -> {
                    arrayOf(OPIA, "isStartAppDetail", "checkToScanRisk", "initiateInstall")
                }
            }
        //Source OPlusPackageInstallerActivity ? AppDetailRedirectionUtils
        findClass(member[0]).hook {
            //skip appdetail,search isStartAppDetail
            //search Method count_canceled_by_app_detail -4 ? -5
            injectMember {
                method {
                    name = member[1]
                    if (member[0] == OPIA) returnType = BooleanType
                    if (member[0] == ADRU) returnType = IntType
                }
                if (member[0] == OPIA) replaceToFalse()
                if (member[0] == ADRU) replaceTo(9)
            }
        }
        findClass(OPIA).hook {
            //skip app scan, search method checkToScanRisk
            //search -> "button_type", "install_old_version_button" -5 -> Method
            //replace to initiateInstall
            //search -> "button_type", "install_old_version_button" -11 -> Method
            injectMember {
                method {
                    name = member[2]
                }
                replaceUnit {
                    method {
                        name = member[3]
                    }.get(instance).call()
                }
            }
        }
    }
}
