package com.luckyzyx.luckytool.hook

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.*
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.log.loggerE
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.luckyzyx.luckytool.hook.apps.CorePatch.CorePatchForR
import com.luckyzyx.luckytool.hook.apps.CorePatch.CorePatchForS
import com.luckyzyx.luckytool.hook.apps.CorePatch.CorePatchForSv2
import com.luckyzyx.luckytool.hook.apps.CorePatch.CorePatchForT
import com.luckyzyx.luckytool.hook.apps.Miscellaneous
import com.luckyzyx.luckytool.hook.statusbar.*
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

@InjectYukiHookWithXposed
class MainHook : IYukiHookXposedInit {

    override fun onInit() {
        configs {
            // 全局调试用的 TAG,在 Logcat 控制台过滤此 TAG 可找到详细日志
            debugTag = "LuckyTool"
            // 是否开启调试模式,请注意 - 若作为发布版本请务必关闭调试功能防止对用户设备造成大量日志填充
            isDebug = false
        }
    }

    override fun onHook() = encase {
        if (prefs(XposedPrefs).getBoolean("enable_module").not()) return@encase
        //系统框架
        loadSystem(HookAndroid())
        loadZygote(HookZygote())

        //状态栏通知
        loadApp(hooker = StatusBarNotice())
        //状态栏图标
        loadApp(hooker = StatusBarIcon())
        //状态栏磁贴
        loadApp(hooker = StatusBarTiles())
        //状态栏时钟
        loadApp(hooker = StatusBarClock())
        //状态栏日期
        loadApp(hooker = StatusBarDate())
        //桌面
        loadApp(hooker = Desktop())
        //锁屏
        loadApp(hooker = LockScreen())
        //截屏
        loadApp(hooker = Screenshot())
        //应用
        loadApp(hooker = Application())
        //杂项
        loadApp(hooker = Miscellaneous())

        //相机
        loadApp("com.oplus.camera", HookCamera())
        //主题商店
        loadApp("com.heytap.themestore", HookThemeStore())
        //云服务
        loadApp("com.heytap.cloud", HookCloudService())
        //游戏助手
        loadApp("com.oplus.games", HookOplusGames())

        //其他APP
        loadApp(hooker = HookOtherApp())
    }

    override fun onXposedEvent() {
        YukiXposedEvent.onInitZygote { startupParam: IXposedHookZygoteInit.StartupParam ->
            run {
                when (SDK_INT) {
                    33 -> CorePatchForT().initZygote(startupParam)
                    S_V2 -> CorePatchForSv2().initZygote(startupParam)
                    S -> CorePatchForS().initZygote(startupParam)
                    R -> CorePatchForR().initZygote(startupParam)
                    else -> loggerE(msg = "Unsupported Version of Android -> $SDK_INT")
                }
            }
        }
        YukiXposedEvent.onHandleLoadPackage { lpparam: XC_LoadPackage.LoadPackageParam ->
            run {
                if (lpparam.packageName == "android" && lpparam.processName == "android") {
                    when (SDK_INT) {
                        33 -> CorePatchForT().handleLoadPackage(lpparam)
                        S_V2 -> CorePatchForSv2().handleLoadPackage(lpparam)
                        S -> CorePatchForS().handleLoadPackage(lpparam)
                        R -> CorePatchForR().handleLoadPackage(lpparam)
                        else -> loggerE(msg = "Unsupported Version of Android -> $SDK_INT")
                    }
                }
            }
        }
    }
}