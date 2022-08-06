package com.luckyzyx.luckytool.hook.apps.systemui

import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class RemoveStatusBarClockRedOne : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                method {
                    name = "setShouldShowOpStyle"
                    param(BooleanType)
                }
                beforeHook {
                    args(0).setFalse()
                }
            }.onNoSuchMemberFailure {
                loggerE(msg = "MethodNotFound->setTextWithRedOneStyle")
            }
        }
        findClass("com.oplusos.systemui.ext.BaseClockExt").hook {
            injectMember {
                method {
                    name {
                        //C12.1
                        equalsOf(other = "setTextWithRedOneStyle",isIgnoreCase = false)
                    }
                    paramCount = 2
                }
                beforeHook {
                    args(0).cast<TextView>()?.text = args(1).cast<CharSequence>().toString()
                    resultFalse()
                }
            }.onNoSuchMemberFailure {
                loggerE(msg = "MethodNotFound->setTextWithRedOneStyle")
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound->BaseClockExt")
        }
    }
}