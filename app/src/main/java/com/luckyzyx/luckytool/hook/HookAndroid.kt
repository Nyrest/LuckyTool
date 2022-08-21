package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.android.*

class HookAndroid : YukiBaseHooker() {

    override fun onHook() {
        //禁用FLAG_SECURE
        loadHooker(DisableFlagSecure())

        //移除状态栏上层警告
        loadHooker(RemoveStatusBarTopNotification())

        //移除系统截屏延迟
        loadHooker(RemoveSystemScreenshotDelay())

        //移除VPN已激活通知
        loadHooker(RemoveVPNActiveNotification())

        //应用多开限制
        loadHooker(MultiApp())

        //USB安装确认
        loadHooker(ADBInstallConfirm())

        //应用安装签名校验
        loadHooker(AppInstallationSignatureVerification())

    }
}
