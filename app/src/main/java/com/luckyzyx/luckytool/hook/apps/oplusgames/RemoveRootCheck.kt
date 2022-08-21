package com.luckyzyx.luckytool.hook.apps.oplusgames

import android.os.Bundle
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.hasMethod
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.android.BundleClass

class RemoveRootCheck : YukiBaseHooker() {
    override fun onHook() {
        //Source COSASDKManager
        //Search -> dynamic_feature_cool_ex -> Method
        //("isSafe")) : null; -> isSafe:0
        val clazzList = ArrayList<String>()
        val indexList = ArrayList<Int>()
        clazzList.add("com.oplus.x.c") //f86f767,ce65873
        clazzList.add("com.oplus.f.c") //424d87a
        clazzList.add("com.oplus.a0.c") //b1a0f1c
        clazzList.add("com.oplus.a0.g") //46a4071
        if (clazzList.isEmpty()) return
        for (i in clazzList) {
            if (i.hasClass) {
                indexList.add(clazzList.indexOf(i))
            }
        }
        if (indexList.isEmpty()) {
            loggerD(msg = "ClassListNotFound -> $packageName}")
            return
        }
        for (c in indexList) {
            if (!clazzList[c].clazz.hasMethod {
                    name { onlyLetters() }
                    emptyParam()
                    returnType(BundleClass)
                }) continue
            clazzList[c].clazz.hook {
                injectMember {
                    method {
                        name {
                            onlyLetters()
                        }
                        emptyParam()
                        returnType(BundleClass)
                    }
                    afterHook {
                        result<Bundle>()?.putInt("isSafe", 0)
                    }
                }
            }
        }
    }
}