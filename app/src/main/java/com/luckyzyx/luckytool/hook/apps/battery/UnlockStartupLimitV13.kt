package com.luckyzyx.luckytool.hook.apps.battery

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class UnlockStartupLimitV13 : YukiBaseHooker() {
    override fun onHook() {
        //Source StartupManager
        //Search -> ? 5 : 20; -1 -> Method
        VariousClass(
            "i7.b", //6f0072e
        ).clazz.hook {
            injectMember {
                method {
                    name = "g"
                    emptyParam()
                    returnType = IntType
                }
                replaceTo(1000)
            }
        }
    }
}