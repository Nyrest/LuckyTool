package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.LockScreenCentered
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveLockScreenCamera
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveLockScreenRedOne
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class LockScreen : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //移除锁屏时钟红1
            if (prefs(XposedPrefs).getBoolean("remove_lock_screen_redone",false)) loadHooker(
                RemoveLockScreenRedOne()
            )
            //移除锁屏右下角相机
            if (prefs(XposedPrefs).getBoolean("remove_lock_screen_camera",false)) loadHooker(
                RemoveLockScreenCamera()
            )

            //锁屏组件居中
            if (prefs(XposedPrefs).getBoolean("set_lock_screen_centered",false)) loadHooker(
                LockScreenCentered()
            )

        }
    }
}