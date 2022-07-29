package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.launcher.RemoveAppUpdateDot
import com.luckyzyx.colorosext.hook.launcher.UnlockTaskLocks
import com.luckyzyx.colorosext.utils.XposedPrefs

class HookLauncher : YukiBaseHooker(){
    override fun onHook() {
        //解锁最近任务限制
        if (prefs(XposedPrefs).getBoolean("unlock_task_locks",false)) loadHooker(UnlockTaskLocks())

        //移除APP更新蓝点
        if (prefs(XposedPrefs).getBoolean("remove_appicon_dot",false)) loadHooker(RemoveAppUpdateDot())
    }
}
