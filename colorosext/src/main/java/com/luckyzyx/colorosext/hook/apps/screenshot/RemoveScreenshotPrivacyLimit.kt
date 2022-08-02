package com.luckyzyx.colorosext.hook.apps.screenshot

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveScreenshotPrivacyLimit : YukiBaseHooker() {
    override fun onHook() {
        findClass("$packageName.screenshot.core.ScreenshotContext").hook {
            injectMember {
                method {
                    name = "setScreenshotReject"
                }
                beforeHook {
                    if (args[0].toString() == "SECURE_WINDOW"){
                        args(0).set("SECURE_WINDOW")
                    }
                }
            }
        }
    }
}