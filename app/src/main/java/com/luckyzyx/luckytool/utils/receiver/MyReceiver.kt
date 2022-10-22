package com.luckyzyx.luckytool.utils.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.highcapable.yukihookapi.hook.factory.modulePrefs
import com.luckyzyx.luckytool.utils.tools.ShellUtils
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import com.luckyzyx.luckytool.utils.tools.getInt

class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != null && intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            //开机自启强制刷新率
            if (context.modulePrefs(XposedPrefs).getBoolean("fps_autostart", false)) {
                val fps = context.getInt(XposedPrefs, "current_fps", -1)
                if (fps == -1) return
                ShellUtils.execCommand("su -c service call SurfaceFlinger 1035 i32 $fps", true)
            }
        }
    }
}