package com.luckyzyx.luckytool.hook.apps.systemui

import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarBatteryPercent : YukiBaseHooker() {
    override fun onHook() {
        //Source StatBatteryMeterView
        //Method "%" -9
        findClass("com.oplusos.systemui.statusbar.widget.StatBatteryMeterView").hook {
            injectMember {
                method {
                    name = "updatePercentText"
                }
                afterHook {
                    field { name = "batteryPercentText" }.get(instance).cast<TextView>()?.apply {
                        text = text.toString().replace("%","")
                    }
                }
            }
        }
    }
}