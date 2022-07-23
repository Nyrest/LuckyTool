package com.luckyzyx.tools.hook.systemui

import android.graphics.Color
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class LockScreenTextViewColor : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        findClass("com.oplusos.systemui.keyguard.clock.RedHorizontalSingleClockView").hook {
            injectMember {
                method {
                    name = "setTextColor"
                    param(IntType)
                }
                beforeHook {
                    if (prefs(PrefsFile).getString("set_lock_screen_textview_color","") != ""){
                        args(0).set(Color.parseColor(prefs(PrefsFile).getString("set_lock_screen_textview_color","")))
                    }
                }
            }
        }
    }
}