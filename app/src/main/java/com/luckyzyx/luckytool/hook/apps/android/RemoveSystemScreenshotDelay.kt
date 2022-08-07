package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.type.java.LongType

class RemoveSystemScreenshotDelay : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.server.policy.PhoneWindowManager").hook {
            injectMember {
                method {
                    name = "getScreenshotChordLongPressDelay"
                    returnType = LongType
                }
                replaceTo(0L)
            }.onNoSuchMemberFailure {
                loggerE(msg = "NoSuchMember->getScreenshotChordLongPressDelay")
            }
        }.onHookClassNotFoundFailure {
            loggerE(msg = "ClassNotFound->PhoneWindowManager")
        }
    }
}