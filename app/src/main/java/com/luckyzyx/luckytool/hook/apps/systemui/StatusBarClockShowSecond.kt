package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.res.Configuration
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import org.w3c.dom.Text

class StatusBarClockShowSecond : YukiBaseHooker() {
    override fun onHook() {
        //Source Clock
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                method {
                    name = "setShowSecondsAndUpdate"
                    param(BooleanType)
                }
                beforeHook {
                    args(0).setTrue()
                }
            }
        }
    }
}