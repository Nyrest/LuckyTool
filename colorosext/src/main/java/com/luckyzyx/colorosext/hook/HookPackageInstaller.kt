package com.luckyzyx.colorosext.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.colorosext.hook.packageinstaller.AllowReplaceInstall
import com.luckyzyx.colorosext.hook.packageinstaller.RemoveInstallAds
import com.luckyzyx.colorosext.hook.packageinstaller.ReplaseAospInstaller
import com.luckyzyx.colorosext.hook.packageinstaller.SkipApkScan


class HookPackageInstaller : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //跳过安装扫描
        if (prefs(prefsFile).getBoolean("skip_apk_scan", false)) loadHooker(SkipApkScan())

        //低/相同版本警告
        if (prefs(prefsFile).getBoolean("allow_replace_install",false)) loadHooker(AllowReplaceInstall())

        //移除安装完成广告
        if (prefs(prefsFile).getBoolean("remove_install_ads",false)) loadHooker(RemoveInstallAds())

        //ColorOS安装器替换为原生安装器
        if (prefs(prefsFile).getBoolean("replase_aosp_installer",false)) loadHooker(ReplaseAospInstaller())

    }
}