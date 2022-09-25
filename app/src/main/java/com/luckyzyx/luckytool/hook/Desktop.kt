package com.luckyzyx.luckytool.hook

import android.os.Build.VERSION.SDK_INT
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.alarmclock.RemoveAlarmClockWidgetRedOne
import com.luckyzyx.luckytool.hook.apps.launcher.FolderLayoutRowColume
import com.luckyzyx.luckytool.hook.apps.launcher.LauncherLayoutRowColume
import com.luckyzyx.luckytool.hook.apps.launcher.RemoveAppUpdateDot
import com.luckyzyx.luckytool.hook.apps.launcher.RemoveAppUpdateDotV13
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class Desktop : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.coloros.alarmclock"){
            //移除桌面时钟组件红一
            if (prefs(XposedPrefs).getBoolean("remove_alarmclock_widget_redone",false)) loadHooker(
                RemoveAlarmClockWidgetRedOne()
            )
        }
        loadApp("com.android.launcher"){
            //移除APP更新圆点
            if (prefs(XposedPrefs).getBoolean("remove_appicon_dot",false)) {
                if (SDK_INT >= 33){
                    loadHooker(RemoveAppUpdateDotV13())
                }else{
                    loadHooker(RemoveAppUpdateDot())
                }
            }
            //设置桌面布局行和列
            if (prefs(XposedPrefs).getBoolean("launcher_layout_enable",false)) loadHooker(
                LauncherLayoutRowColume()
            )
            //设置桌面文件夹行列数
            if (prefs(XposedPrefs).getBoolean("set_folder_layout_4x4",false)) loadHooker(
                FolderLayoutRowColume()
            )
        }
    }
}