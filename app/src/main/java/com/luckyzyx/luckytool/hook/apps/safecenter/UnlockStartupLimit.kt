package com.luckyzyx.luckytool.hook.apps.safecenter

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType

class UnlockStartupLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source StratupManager
        //Search -> update max allow count -5 -> method,-1 -> field
        VariousClass(
            "com.oplus.safecenter.startupapp.a", //6f0072e
        ).clazz.hook {
            injectMember {
                method {
                    name {
                        equalsOf(other = "b",isIgnoreCase = false)
                    }
                    param(ContextClass)
                }
                afterHook {
                    field {
                        name {
                            equalsOf(other = "d",isIgnoreCase = false)
                        }
                        type = IntType
                    }.get().set(10000)
                }
            }
        }
    }
}