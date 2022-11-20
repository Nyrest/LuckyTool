package com.luckyzyx.luckytool.hook.apps.packageinstaller

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class ReplaseAospInstaller : YukiBaseHooker() {
    override fun onHook() {
        //search class -> DeleteStagedFileOnResult
        //search class.method -> onCreate +4 -> class.method
//        findClass("com.android.packageinstaller.DeleteStagedFileOnResult").hook {
//            injectMember {
//                method {
//                    name = "onCreate"
//                    param(BundleClass)
//                }
//                beforeHook {
//                    args(0).setNull()
//                    VariousClass(
//                        "com.android.packageinstaller.oplus.common.j",
//                        "com.android.packageinstaller.oplus.common.FeatureOption"
//                    ).toClass().field {
//                        type(BooleanType).index(1)
//                    }.get().setTrue()
//                }
//            }
//        }

        //Source FeatureOption
        findClass("com.android.packageinstaller.oplus.common.FeatureOption").hook {
            injectMember {
                method {
                    name = "setIsClosedSuperFirewall"
                    paramCount = 1
                }
                afterHook {
                    field { name = "sIsClosedSuperFirewall" }.get().setTrue()
                }
            }
        }
    }
}