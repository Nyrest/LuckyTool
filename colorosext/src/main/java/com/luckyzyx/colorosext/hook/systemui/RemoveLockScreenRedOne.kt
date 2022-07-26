package com.luckyzyx.colorosext.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.StringType

class RemoveLockScreenRedOne : YukiBaseHooker() {
    override fun onHook() {
        "com.oplusos.systemui.keyguard.clock.RedTextClock".clazz.field {
            name = "NUMBER_ONE"
            type = StringType
        }.get().set("")
    }
}