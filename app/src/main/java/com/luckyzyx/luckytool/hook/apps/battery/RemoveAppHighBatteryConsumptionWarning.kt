package com.luckyzyx.luckytool.hook.apps.battery

import android.app.NotificationManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.HandlerClass

class RemoveAppHighBatteryConsumptionWarning : YukiBaseHooker() {
    override fun onHook() {
        // Source NotifyUtil
        // Search power_consumption_optimization_title / pco_notification_text / String \n String
        searchClass {
            from("c4","com.oplus.a.g")
            constructor().count(1)
            field {
                type = NotificationManager::class.java
            }.count(1)
            field {
                type = HandlerClass
            }.count(1)
            method {
                modifiers { isPublic }
                param(String,Boolean)
                paramCount = 2
            }.count(4)
        }.get()?.hook {
            injectMember {
                method {
                    paramCount = 2
                    param(String,Boolean).index(0)
                }
                replaceTo(null)
            }
            injectMember {
                method {
                    paramCount = 2
                    param(String,Boolean).index(1)
                }
                replaceTo(null)
            }
            injectMember {
                method {
                    paramCount = 2
                    param(String,Boolean).index(2)
                }
                replaceTo(null)
            }
            injectMember {
                method {
                    paramCount = 2
                    param(String,Boolean).index(3)
                }
                replaceTo(null)
            }
        }
    }
}