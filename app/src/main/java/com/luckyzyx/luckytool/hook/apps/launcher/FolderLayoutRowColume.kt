package com.luckyzyx.luckytool.hook.apps.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class FolderLayoutRowColume : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.launcher3.InvariantDeviceProfile").hook {
            injectMember {
                method {
                    name = "initGrid"
                }
                afterHook {
//                    field {
//                        name = "numFolderRows"
//                    }.get(instance).set(4)
                    field {
                        name = "numFolderColumns"
                    }.get(instance).set(4)
                }
            }
        }
    }
}