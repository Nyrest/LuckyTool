package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.launcher.RemoveAppUpdateDot
import com.luckyzyx.colorosext.hook.launcher.UnlockTaskLocks

class HookLauncher : YukiBaseHooker(){
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //解锁最近任务限制
        if (prefs(prefsFile).getBoolean("unlock_task_locks",false)) loadHooker(UnlockTaskLocks())

        //移除APP更新蓝点
        if (prefs(prefsFile).getBoolean("remove_app_update_dot",false)) loadHooker(RemoveAppUpdateDot())
    }
}
