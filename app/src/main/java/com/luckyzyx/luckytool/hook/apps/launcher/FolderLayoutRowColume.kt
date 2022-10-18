package com.luckyzyx.luckytool.hook.apps.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class FolderLayoutRowColume : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.launcher3.folder.FolderGridOrganizer").hook {
            injectMember {
                constructor {
                    paramCount = 1
                }
                afterHook {
                    val x = field {
                        name = "mMaxCountX"
                    }.get(instance)
                    val y = field {
                        name = "mMaxCountY"
                    }.get(instance)
                    x.set(4)
                    y.set(4)
                    field {
                        name = "mMaxItemsPerPage"
                    }.get(instance).set(x.cast<Int>()!! * y.cast<Int>()!!)
                }
            }
        }
    }
}