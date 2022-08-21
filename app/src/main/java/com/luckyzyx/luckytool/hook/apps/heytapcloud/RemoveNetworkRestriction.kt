package com.luckyzyx.luckytool.hook.apps.heytapcloud

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        //Source NetworkUtil
        //Search -> MOBILE -> method
        VariousClass(
            "com.cloud.base.commonsdk.baseutils.al",
            "com.cloud.base.commonsdk.baseutils.o0",
        ).hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = IntType
                }
                afterHook {
                    if (result<Int>() == 1) result = 2
                }
            }
        }
    }
}