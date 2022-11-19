package com.luckyzyx.luckytool.hook.statusbar

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.NetworkSpeed

class StatusBarNetWorkSpeed : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //设置状态栏网速刷新率
            loadHooker(NetworkSpeed())
        }
    }
}