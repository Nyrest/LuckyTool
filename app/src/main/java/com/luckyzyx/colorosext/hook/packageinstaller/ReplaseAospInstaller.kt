package com.luckyzyx.tools.hook.packageinstaller

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class ReplaseAospInstaller : YukiBaseHooker() {
    override fun onHook() {
        //use AOSP installer
        //search class -> DeleteStagedFileOnResult
        //search class.method -> onCreate +4 -> class.method
        findClass("com.android.packageinstaller.DeleteStagedFileOnResult").hook{
            injectMember {
                method {
                    name = "onCreate"
                    param(BundleClass)
                }
                beforeHook {
                    VariousClass(
                        "com.android.packageinstaller.oplus.common.j",
                        "com.android.packageinstaller.oplus.common.FeatureOption"
                    ).clazz.field {
                        name {
                            //7bc7db7,e1a2c58,75fe984,532ffef,38477f0,a222497
                            equalsOf(other = "f",isIgnoreCase = false)
                            //d132ce2,faec6ba
                            equalsOf(other = "sIsClosedSuperFirewall",isIgnoreCase = false)
                        }
                        type = BooleanType
                    }.get().setTrue()
                }
            }
        }

    }
}