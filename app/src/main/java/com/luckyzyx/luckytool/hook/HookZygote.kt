package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.android.DisableFlagSecureZygote

class HookZygote : YukiBaseHooker() {
    override fun onHook() {
        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecureZygote())
    }
}