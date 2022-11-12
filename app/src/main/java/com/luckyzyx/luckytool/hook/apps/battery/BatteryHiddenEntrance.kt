package com.luckyzyx.luckytool.hook.apps.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class BatteryHiddenEntrance : YukiBaseHooker() {
    override fun onHook() {
        val openScreenPowerSave = prefs(XposedPrefs).getBoolean("open_screen_power_save", false)
        val openBatteryHealth = prefs(XposedPrefs).getBoolean("open_battery_health", false)
        if (!(openScreenPowerSave || openBatteryHealth)) return
        //Source AppFeature
        //Search Static Field cabc_level_dynamic_enable batteryhealth
        searchClass {
            from("com.oplus.a.c", "y3").absolute()
            field {
                type = ListClass
            }.count(1)
            field {
                type = IntType
            }.count(1..3)
            method {
                param(ContextClass)
            }.count(1)
            method {
                returnType = IntType
            }.count(1..2)
        }.get().takeIf { it != null }?.hook {
            injectMember {
                method {
                    param(ContextClass)
                }
                afterHook {
                    if (openScreenPowerSave) field { name = "a" }.get().setTrue()
                    if (openBatteryHealth) field { name = "w" }.get().setTrue()
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> BatteryHiddenEntrance")
    }
}