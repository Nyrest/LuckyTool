package com.luckyzyx.luckytool.hook.apps.camera

import com.highcapable.yukihookapi.hook.bean.VariousClass
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType

class RemoveWatermarkWordLimit : YukiBaseHooker() {
    override fun onHook() {
        //Source CameraSubSettingFragment
        VariousClass(
            "com.oplus.camera.ui.menu.setting.p$7"
        ).hook {
            injectMember {
                method {
                    name = "filter"
                    paramCount = 6
                    returnType = CharSequenceType
                }
                replaceTo(null)
            }
        }
    }
}