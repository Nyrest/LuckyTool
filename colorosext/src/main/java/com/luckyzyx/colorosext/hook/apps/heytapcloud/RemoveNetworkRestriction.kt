package com.luckyzyx.colorosext.hook.apps.heytapcloud

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntType

class RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        VariousClass(
            "com.cloud.base.commonsdk.baseutils.al"
        ).hook {
            injectMember {
                method {
                    name {
                        equalsOf(other = "a",isIgnoreCase = false) //2cd987d
                    }
                    emptyParam()
                    returnType = IntType
                }
                afterHook {
                    if (result<Int>() == 1){
                        result = 2
                    }
                }
            }
        }
    }
}