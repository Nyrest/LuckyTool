package com.luckyzyx.luckytool.hook.apps.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.XposedPrefs

class SkipApkScan : YukiBaseHooker() {
    override fun onHook() {
        val member: Array<String> =
            when (prefs(XposedPrefs).getString(packageName)) {
                "7bc7db7", "e1a2c58" -> {
                    arrayOf("L", "C", "K")
                }
                "75fe984", "532ffef" -> {
                    arrayOf("L", "D", "i")
                }
                "38477f0" -> {
                    arrayOf("M", "D", "k")
                }
                "a222497" -> {
                    arrayOf("M", "E", "j")
                }
                //d132ce2,faec6ba,860700c
                else -> {
                    arrayOf("isStartAppDetail", "checkToScanRisk", "initiateInstall")
                }
            }

        //Source OPlusPackageInstallerActivity
        findClass("com.android.packageinstaller.oplus.OPlusPackageInstallerActivity").hook {
            //skip appdetail,search isStartAppDetail
            //search -> count_canceled_by_app_detail -4 -> Method
            injectMember {
                method {
                    name = member[0]
                    returnType = BooleanType
                }
                replaceToFalse()
            }
            //skip app scan, search method checkToScanRisk
            //search -> "button_type", "install_old_version_button" -5 -> Method
            //replace to initiateInstall
            //search -> "button_type", "install_old_version_button" -11 -> Method
            injectMember {
                method {
                    name = member[1]
                }
                replaceUnit {
                    method {
                        name = member[2]
                    }.get(instance).call()
                }
            }
        }
    }
}