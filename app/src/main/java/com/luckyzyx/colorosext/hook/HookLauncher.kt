package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.launcher.RemoveAppUpdateDot
import com.luckyzyx.tools.hook.launcher.UnlockTaskLocks

class HookLauncher : YukiBaseHooker(){
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //解锁最近任务限制
        if (prefs(PrefsFile).getBoolean("unlock_task_locks",false)) loadHooker(UnlockTaskLocks())

        //移除APP更新蓝点
        if (prefs(PrefsFile).getBoolean("remove_app_update_dot",false)) loadHooker(RemoveAppUpdateDot())
    }
}
