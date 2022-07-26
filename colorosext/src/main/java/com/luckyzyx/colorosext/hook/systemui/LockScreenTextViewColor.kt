package com.luckyzyx.colorosext.hook.systemui

import android.graphics.Color
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class LockScreenTextViewColor : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        findClass("com.oplusos.systemui.keyguard.clock.RedHorizontalSingleClockView").hook {
            injectMember {
                method {
                    name = "setTextColor"
                    param(IntType)
                }
                beforeHook {
                    if (prefs(prefsFile).getString("set_lock_screen_textview_color","") != ""){
                        args(0).set(Color.parseColor(prefs(prefsFile).getString("set_lock_screen_textview_color","")))
                    }
                }
            }
        }
    }
}