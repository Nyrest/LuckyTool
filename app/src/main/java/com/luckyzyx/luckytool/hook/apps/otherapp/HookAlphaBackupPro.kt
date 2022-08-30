package com.luckyzyx.luckytool.hook.apps.otherapp

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookAlphaBackupPro : YukiBaseHooker() {
    override fun onHook() {
        //移除许可证检测
        if (prefs(XposedPrefs).getBoolean("remove_check_license",false)) {
            findClass("com.ruet_cse_1503050.ragib.appbackup.pro.activities.HomeActivity").hook {
                injectMember {
                    method {
                        name = "initializeData"
                        emptyParam()
                    }
                    afterHook {
                        field {
                            name = "licenseStateData"
                        }.get(instance).set("valid_licence")
                    }
                }
            }
        }

    }
}