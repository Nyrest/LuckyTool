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
                    name = "b"
                    param(ContextClass)
                    paramCount = 1
                }
                afterHook {
                    field {
                        name = "d"
                        type = IntType
                    }.get().set(10000)
                }
            }
        }
    }
    class UnlockStartupLimitColorOS : YukiBaseHooker() {
        override fun onHook() {
            //Source StratupManager
            //Search -> auto_start_max_allow_count -2 -> method,+2 -> field
            VariousClass(
                "com.coloros.safecenter.startupapp.b", //6f0072e
            ).clazz.hook {
                injectMember {
                    method {
                        name = "c"
                        param(ContextClass)
                        paramCount = 1
                    }
                    afterHook {
                        field {
                            name = "b"
                            type = IntType
                        }.get().set(10000)
                    }
                }
            }
        }
    }
}