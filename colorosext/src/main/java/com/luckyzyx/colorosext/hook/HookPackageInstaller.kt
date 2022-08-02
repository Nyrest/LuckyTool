package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.apps.packageinstaller.AllowReplaceInstall
import com.luckyzyx.colorosext.hook.apps.packageinstaller.RemoveInstallAds
import com.luckyzyx.colorosext.hook.apps.packageinstaller.ReplaseAospInstaller
import com.luckyzyx.colorosext.hook.apps.packageinstaller.SkipApkScan
import com.luckyzyx.colorosext.utils.XposedPrefs


class HookPackageInstaller : YukiBaseHooker() {
    override fun onHook() {
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