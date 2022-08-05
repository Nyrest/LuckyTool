package com.luckyzyx.luckytool.hook

import android.os.Build.VERSION.SDK_INT
import com.highcapable.yukihookapi.annotation.xposed.InjectYukiHookWithXposed
import com.highcapable.yukihookapi.hook.factory.configs
import com.highcapable.yukihookapi.hook.factory.encase
import com.highcapable.yukihookapi.hook.xposed.bridge.event.YukiXposedEvent
import com.highcapable.yukihookapi.hook.xposed.proxy.IYukiHookXposedInit
import com.luckyzyx.luckytool.hook.apps.CorePatch.CorePatchForR
import com.luckyzyx.luckytool.hook.apps.CorePatch.CorePatchForS
import com.luckyzyx.luckytool.utils.XposedPrefs
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
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
        //系统界面
        loadApp("com.android.systemui", HookSystemUI())
        //相机
        loadApp("com.oplus.camera", HookCamera())
        //系统桌面
        loadApp("com.android.launcher", HookLauncher())
        //安全中心
        loadApp("com.oplus.safecenter", HookSafeCenter())
        //时钟
        loadApp("com.coloros.alarmclock", HookAlarmClock())
        //主题商店
        loadApp("com.heytap.themestore", HookThemeStore())
        //云服务
        loadApp("com.heytap.cloud", HookCloudService())
        //游戏助手
        loadApp("com.oplus.games", HookOplusGames())
        //截屏
        loadApp("com.oplus.screenshot",HookScreenshot())
        //应用包安装程序
        loadApp("com.android.packageinstaller", HookPackageInstaller())
        //其他APP
        loadApp { HookOtherApp() }

    }

    override fun onXposedEvent() {
        YukiXposedEvent.onInitZygote { startupParam: IXposedHookZygoteInit.StartupParam ->
            run {
                when (SDK_INT) {
                    30 -> CorePatchForR().initZygote(startupParam)
                    31 -> CorePatchForS().initZygote(startupParam)
                    else -> XposedBridge.log("[CorePatch] 不支持的Android版本: $SDK_INT")
                }
            }
        }
        YukiXposedEvent.onHandleLoadPackage { lpparam: XC_LoadPackage.LoadPackageParam ->
            run {
                if (lpparam.packageName == "android" && lpparam.processName == "android") {
                    when (SDK_INT) {
                        30 -> CorePatchForR().handleLoadPackage(lpparam)
                        31 -> CorePatchForS().handleLoadPackage(lpparam)
                        else -> XposedBridge.log("[CorePatch] 不支持的Android版本: $SDK_INT")
                    }
                }
            }
        }
    }
}