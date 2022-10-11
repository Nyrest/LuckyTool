package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.StringType

class RemoveLockScreenRedOne : YukiBaseHooker() {
    override fun onHook() {
        //Source RedTextClock
        "com.oplusos.systemui.keyguard.clock.RedTextClock".toClass().field {
            name = "NUMBER_ONE"
            type = StringType
        }.get().set("")
    }
}