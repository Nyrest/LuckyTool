package com.luckyzyx.luckytool.hook.apps.screenshot

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveScreenshotPrivacyLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source ScreenshotContext
        findClass("com.oplus.screenshot.screenshot.core.ScreenshotContext").hook {
            injectMember {
                method {
                    name = "setScreenshotReject"
                }
                beforeHook {
                    args(0).set("ACCEPTED")
                }
            }
        }
    }
}