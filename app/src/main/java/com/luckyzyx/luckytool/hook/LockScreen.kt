package com.luckyzyx.luckytool.hook

import android.graphics.Color
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.systemui.LockScreenTextViewColor
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveLockScreenCamera
import com.luckyzyx.luckytool.hook.apps.systemui.RemoveLockScreenRedOne
import com.luckyzyx.luckytool.utils.XposedPrefs

class LockScreen : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui") {
            //移除锁屏时钟红1
            if (prefs(XposedPrefs).getBoolean("remove_lock_screen_redone",false)) loadHooker(
                RemoveLockScreenRedOne()
            )
            //设置锁屏组件文本颜色
            if (prefs(XposedPrefs).getInt("set_lock_screen_textview_color", Color.BLACK) != Color.BLACK) loadHooker(
                LockScreenTextViewColor()
            )
            //移除锁屏右下角相机
            if (prefs(XposedPrefs).getBoolean("remove_lock_screen_camera",false)) loadHooker(
                RemoveLockScreenCamera()
            )
        }
    }
}