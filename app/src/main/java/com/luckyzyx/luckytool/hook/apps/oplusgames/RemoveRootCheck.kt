package com.luckyzyx.luckytool.hook.apps.oplusgames

import android.os.Bundle
import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass

class RemoveRootCheck : YukiBaseHooker() {
    override fun onHook() {
        //Source COSASDKManager
        //Search -> dynamic_feature_cool_ex -> Method
        //("isSafe")) : null; -> isSafe:0
        VariousClass(
            "com.oplus.x.c", //f86f767,ce65873
            "com.oplus.f.c", //424d87a
            "com.oplus.a0.c", //b1a0f1c
        ).hook {
            injectMember {
                method {
                    name = "b"
                    emptyParam()
                    returnType(BundleClass)
                }.remedys {
                    method {
                        name = "c"
                        emptyParam()
                        returnType(BundleClass)
                    }
                    method {
                        name = "e"
                        emptyParam()
                        returnType(BundleClass)
                    }
                }.wait {
                    afterHook {
                        result<Bundle>()?.putInt("isSafe", 0)
                    }
                }
            }
        }
    }
}