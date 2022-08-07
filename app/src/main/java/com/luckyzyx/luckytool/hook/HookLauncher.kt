package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.launcher.LauncherLayoutRowColume
import com.luckyzyx.luckytool.hook.apps.launcher.RemoveAppUpdateDot
import com.luckyzyx.luckytool.hook.apps.launcher.UnlockTaskLocks
import com.luckyzyx.luckytool.utils.XposedPrefs

class HookLauncher : YukiBaseHooker(){
    override fun onHook() {
        //解锁最近任务限制
        if (prefs(XposedPrefs).getBoolean("unlock_task_locks",false)) loadHooker(UnlockTaskLocks())

        //移除APP更新蓝点
        if (prefs(XposedPrefs).getBoolean("remove_appicon_dot",false)) loadHooker(RemoveAppUpdateDot())

        //设置桌面布局行和列
        if (prefs(XposedPrefs).getString("launcher_layout_row_colume","") != "") loadHooker(LauncherLayoutRowColume())

    }
}
