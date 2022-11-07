package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.LockScreenCentered
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveLockScreenBottomButton
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveLockScreenBottomSOSButton
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveLockScreenRedOne
import com.luckyzyx.luckytool.utils.tools.A13
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs


class LockScreen : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //移除锁屏时钟红1
            if (prefs(XposedPrefs).getBoolean("remove_lock_screen_redone", false)) loadHooker(
                RemoveLockScreenRedOne()
            )

            //移除锁屏下方按钮
            if (prefs(XposedPrefs).getBoolean("remove_lock_screen_bottom_left_button", false) ||
                prefs(XposedPrefs).getBoolean("remove_lock_screen_bottom_right_camera", false)
            ) {
                loadHooker(RemoveLockScreenBottomButton())
            }

            //锁屏组件居中
            if (prefs(XposedPrefs).getBoolean("set_lock_screen_centered", false)) loadHooker(
                LockScreenCentered()
            )

            //移除SOS紧急联络按钮
            if (prefs(XposedPrefs).getBoolean("remove_lock_screen_bottom_sos_button", false)) {
                if (SDK >= A13) loadHooker(RemoveLockScreenBottomSOSButton())
            }

        }
    }
}