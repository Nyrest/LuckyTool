package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class MultiApp : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.am.OplusMultiAppConfigManager").hook {
            injectMember {
                method {
                    name = "getAllowedList"
                    returnType = ListClass
                }
                afterHook {
                    if (prefs(XposedPrefs).getBoolean("multi_app_enable", false)) result =
                        prefs(XposedPrefs).getStringSet("enabledMulti", HashSet()).toList()
                }
            }
        }
    }
}