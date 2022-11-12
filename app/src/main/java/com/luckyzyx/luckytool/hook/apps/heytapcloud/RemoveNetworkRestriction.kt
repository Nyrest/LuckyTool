package com.luckyzyx.luckytool.hook.apps.heytapcloud

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.ContextClass
import com.highcapable.yukihookapi.hook.type.java.IntType

class RemoveNetworkRestriction : YukiBaseHooker() {
    override fun onHook() {
        //Source NetworkUtil
        //Search -> MOBILE -> method
        searchClass {
            from("com.cloud.base.commonsdk.baseutils", "t2")
            constructor().count(0)
            field().count(0)
            method().count(14)
            method {
                param(ContextClass)
            }.count(7)
            method {
                param(IntType)
            }.count(5)
            method {
                modifiers { isPublic && isStatic }
                emptyParam()
                returnType = IntType
            }.count(1)
        }.get().takeIf { it != null }?.hook {
            injectMember {
                method {
                    modifiers { isPublic && isStatic }
                    emptyParam()
                    returnType = IntType
                }
                afterHook {
                    if (result<Int>() == 1) result = 2
                }
            }
        } ?: loggerD(msg = "$packageName\nError -> RemoveNetworkRestriction")
    }
}