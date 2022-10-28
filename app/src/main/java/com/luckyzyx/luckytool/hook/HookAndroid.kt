package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.LongType
import com.luckyzyx.luckytool.hook.apps.android.*
import com.luckyzyx.luckytool.utils.tools.A13
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

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

        //应用分身限制
        loadHooker(MultiApp())

        //USB安装确认
        loadHooker(ADBInstallConfirm())

        //移除遮罩Splash Screen
        if (SDK >= A13) loadHooker(AppSplashScreen())

        //移除72小时密码验证
        if (!prefs(XposedPrefs).getBoolean("remove_72hour_password_verification",false)) return
        findClass("android.app.admin.DevicePolicyManager").hook {
            injectMember {
                method {
                    name = "getRequiredStrongAuthTimeout"
                    returnType = LongType
                }
                replaceTo(0L)
            }
        }
    }
}
