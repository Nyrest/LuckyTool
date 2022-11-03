package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class Remove72HourPasswordVerification : YukiBaseHooker() {
    override fun onHook() {
        //Source LockPatternUtils
        findClass("com.android.internal.widget.LockPatternUtils").hook {
            injectMember {
                method {
                    name = "hasSecureLockScreen"
                }
                if (prefs(XposedPrefs).getBoolean("remove_72hour_password_verification", false)) replaceToFalse()
            }
        }
    }
}