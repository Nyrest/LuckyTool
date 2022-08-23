package com.luckyzyx.luckytool.hook.apps.launcher

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
class UnlockTaskLocksV11 : YukiBaseHooker() {
    override fun onHook() {
        //Source ColorLockManager
        findClass(name = "com.coloros.quickstep.applock.ColorLockManager").hook {
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