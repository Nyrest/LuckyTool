package com.luckyzyx.luckytool.hook.apps.battery

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType

class UnlockStartupLimitV13 : YukiBaseHooker() {
    override fun onHook() {
        //Source StartupManager
        //Search -> ? 5 : 20; -1 -> Method
        searchClass {
            from("i7", "q7", "u7", "y7").absolute()
            simpleName { it.length == 1 }
            constructor { param(ContextClass) }.count(1)
            field().count(4)
            field { type = ContextClass }.count(1)
            method {
                emptyParam()
                returnType = IntType
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }
                replaceTo(1000)
            }
        }







    }
}