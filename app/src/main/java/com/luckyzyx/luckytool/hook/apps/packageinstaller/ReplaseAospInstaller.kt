package com.luckyzyx.luckytool.hook.apps.packageinstaller

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class ReplaseAospInstaller : YukiBaseHooker() {
    override fun onHook() {
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
                    ).toClass().field {
                        type(BooleanType).index(1)
                    }.get().setTrue()
                }
            }
        }

    }
}