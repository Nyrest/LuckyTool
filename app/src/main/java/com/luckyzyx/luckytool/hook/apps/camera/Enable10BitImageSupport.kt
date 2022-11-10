package com.luckyzyx.luckytool.hook.apps.camera

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class Enable10BitImageSupport : YukiBaseHooker() {
    override fun onHook() {
        //Source CameraUnitUtils PRE_KEY_10BIT_HEIC_ENCODE
        findClass("com.oplus.ocs.camera.appinterface.adapter.CameraUnitUtils").hook {
            injectMember {
                method {
                    name = "getVendorTagConfig"
                    paramCount = 1
                }
                beforeHook {
                    val args = args(0).cast<String>()
                    if (args == "com.oplus.10bits.heic.encode.support") result = "1"
                }
            }
        }
    }
}