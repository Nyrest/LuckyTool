package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarSecurePayment : YukiBaseHooker() {
    override fun onHook() {
        //安全支付图标
        VariousClass(
            "com.oplusos.systemui.ext.SecurePaymentControllerExt",
            "com.oplus.systemui.statusbar.phone.securepay.SecurePaymentControllerExImpl"
        ).hook {
            injectMember {
                method {
                    name = "handlePaymentDetectionMessage"
                    paramCount = 1
                }
                intercept()
            }
        }
    }
}