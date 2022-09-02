package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusBarSecurePayment : YukiBaseHooker() {
    override fun onHook() {
        //安全支付图标
        findClass("com.oplusos.systemui.ext.SecurePaymentControllerExt").hook {
            injectMember {
                method {
                    name = "handlePaymentDetectionMessage"
                    paramCount = 1
                }
                replaceTo(null)
            }
        }
    }
}
class RemoveStatusBarSecurePaymentV13 : YukiBaseHooker() {
    override fun onHook() {
        //安全支付图标
        findClass("com.oplus.systemui.statusbar.phone.securepay.SecurePaymentControllerExImpl").hook {
            injectMember {
                method {
                    name = "handlePaymentDetectionMessage"
                    paramCount = 1
                }
                replaceTo(null)
            }
        }
    }
}