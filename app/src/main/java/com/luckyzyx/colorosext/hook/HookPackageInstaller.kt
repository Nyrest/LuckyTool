package com.luckyzyx.tools.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.tools.hook.packageinstaller.AllowReplaceInstall
import com.luckyzyx.tools.hook.packageinstaller.RemoveInstallAds
import com.luckyzyx.tools.hook.packageinstaller.ReplaseAospInstaller
import com.luckyzyx.tools.hook.packageinstaller.SkipApkScan


class HookPackageInstaller : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        //跳过安装扫描
        if (prefs(PrefsFile).getBoolean("skip_apk_scan", false)) loadHooker(SkipApkScan())

        //低/相同版本警告
        if (prefs(PrefsFile).getBoolean("allow_replace_install",false)) loadHooker(AllowReplaceInstall())

        //移除安装完成广告
        if (prefs(PrefsFile).getBoolean("remove_install_ads",false)) loadHooker(RemoveInstallAds())

        //ColorOS安装器替换为原生安装器
        if (prefs(PrefsFile).getBoolean("replase_aosp_installer",false)) loadHooker(ReplaseAospInstaller())

    }
}