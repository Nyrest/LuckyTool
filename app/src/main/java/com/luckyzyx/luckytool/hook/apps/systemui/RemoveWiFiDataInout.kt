package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveWiFiDataInout : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplus.systemui.statusbar.phone.signal.OplusStatusBarWifiViewExImpl").hook {
            injectMember {
                method {
                    name = "initViewState"
                }
                afterHook {
                    field {
                        name = "mWifiActivity"
                    }.get(instance).cast<View>()?.isVisible = false
                }
            }
            injectMember {
                method {
                    name = "updateState"
                }
                afterHook {
                    field {
                        name = "mWifiActivity"
                    }.get(instance).cast<View>()?.isVisible = false
                }
            }
        }
    }
}