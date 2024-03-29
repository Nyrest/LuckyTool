package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.battery.UnlockStartupLimitV13
import com.luckyzyx.luckytool.hook.apps.launcher.UnlockTaskLocks
import com.luckyzyx.luckytool.hook.apps.launcher.UnlockTaskLocksV11
import com.luckyzyx.luckytool.hook.apps.packageinstaller.AllowReplaceInstall
import com.luckyzyx.luckytool.hook.apps.packageinstaller.RemoveInstallAds
import com.luckyzyx.luckytool.hook.apps.packageinstaller.ReplaseAospInstaller
import com.luckyzyx.luckytool.hook.apps.packageinstaller.SkipApkScan
import com.luckyzyx.luckytool.hook.apps.safecenter.UnlockStartupLimit
import com.luckyzyx.luckytool.hook.apps.safecenter.UnlockStartupLimitV11
import com.luckyzyx.luckytool.utils.tools.A13
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class Application : YukiBaseHooker() {
    override fun onHook() {

        //移除自启数量限制
        if (prefs(XposedPrefs).getBoolean("unlock_startup_limit",false)) {
            if (SDK >= A13){
                //电池
                loadApp("com.oplus.battery",UnlockStartupLimitV13())
            }else{
                //安全中心
                loadApp("com.oplus.safecenter",UnlockStartupLimit())
                //Android11
                loadApp("com.coloros.safecenter", UnlockStartupLimitV11())
            }
        }

        //解锁后台任务限制
        if (prefs(XposedPrefs).getBoolean("unlock_task_locks",false)) {
            //系统桌面
            loadApp("com.android.launcher",UnlockTaskLocks())
            //Android11
            loadApp("com.oppo.launcher", UnlockTaskLocksV11())
        }

        //应用包安装程序
        loadApp("com.android.packageinstaller"){
            //跳过安装扫描
            if (prefs(XposedPrefs).getBoolean("skip_apk_scan", false)) loadHooker(SkipApkScan())

            //低/相同版本警告
            if (prefs(XposedPrefs).getBoolean("allow_downgrade_install",false)) loadHooker(AllowReplaceInstall())

            //移除安装完成广告
            if (prefs(XposedPrefs).getBoolean("remove_install_ads",false)) loadHooker(RemoveInstallAds())

            //ColorOS安装器替换为原生安装器
            if (prefs(XposedPrefs).getBoolean("replase_aosp_installer",false)) loadHooker(ReplaseAospInstaller())

        }
    }
}