package com.luckyzyx.colorosext.hook.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class UnlockTaskLocks : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusLockManager
        findClass(name = "com.oplus.quickstep.applock.OplusLockManager").hook {
            injectMember {
                constructor {
                    paramCount = 1
                }
                afterHook {
                    field {
                        name = "mLockAppLimit"
                    }.get(instance).set(999)
                }
            }
        }
    }
}