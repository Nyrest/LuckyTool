package com.luckyzyx.luckytool.hook.apps.heytapcloud

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        //Source NetworkUtil
        //Search -> MOBILE -> method
        VariousClass(
            "com.cloud.base.commonsdk.baseutils.al"
        ).hook {
            injectMember {
                method {
                    //2cd987d
                    name = "a"
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