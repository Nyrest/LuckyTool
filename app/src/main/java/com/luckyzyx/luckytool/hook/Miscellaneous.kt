package com.luckyzyx.luckytool.hook

import android.os.Build.VERSION.SDK_INT
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.exsystemservice.RemoveWarningDialogThatAppRunsOnDesktop
import com.luckyzyx.luckytool.hook.apps.settings.DisableDPIRebootRecovery
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.tools.XposedPrefs
import rikka.core.BuildConfig

class Miscellaneous : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //禁用复制悬浮窗
            if (prefs(XposedPrefs).getBoolean("disable_duplicate_floating_window",false)){
                if (SDK_INT >= 33) loadHooker(DisableDuplicateFloatingWindow())
            }

            //充电纹波
            if (prefs(XposedPrefs).getBoolean("show_charging_ripple",false)) {
                if (SDK_INT >= 31) loadHooker(ShowChargingRipple())
            }

            //禁用耳机高音量警告
            if (prefs(XposedPrefs).getBoolean("disable_headphone_high_volume_warning",false)){
                loadHooker(DisableHeadphoneHighVolumeWarning())
            }

            //电源菜单相关
            if (prefs(XposedPrefs).getBoolean("power_menu_enable",false)){
                if(SDK_INT in 31..32) loadHooker(PowerMenu())
            }

            //禁用OTG自动关闭
            if (prefs(XposedPrefs).getBoolean("disable_otg_auto_off",false)){
                loadHooker(DisableOTGAutoOff())
            }

            //移除低电量对话框警告
            if (prefs(XposedPrefs).getBoolean("remove_low_battery_dialog_warning",false)){
                loadHooker(RemoveLowBatteryDialogWarning())
            }

        }
        loadApp("com.android.settings"){
            //禁用DPI重启恢复
            if (prefs(XposedPrefs).getBoolean("disable_dpi_reboot_recovery",false)) {
                loadHooker(DisableDPIRebootRecovery())
            }
            if (BuildConfig.DEBUG){
                findClass("com.oplus.settings.feature.deviceinfo.DeviceRamInfoItemPreference").hook {
                    injectMember {
                        method {
                            name = "onBindViewHolder"
                        }
                        beforeHook {
                            //扩展大小
                            field {
                                name = "H"
                            }.get(instance).set(1024*1024*100)
                            //扩展大小图标
                            field {
                                name = "I"
                            }.get(instance).setTrue()
                            //跳转箭头
                            field {
                                name = "J"
                            }.get(instance).setTrue()
                        }
                    }
                }
            }
        }
        loadApp("com.oplus.exsystemservice"){
            //移除应用运行在桌面上警告对话框
            if (prefs(XposedPrefs).getBoolean("remove_warning_dialog_that_app_runs_on_desktop",false)){
                loadHooker(RemoveWarningDialogThatAppRunsOnDesktop())
            }
        }
    }
}