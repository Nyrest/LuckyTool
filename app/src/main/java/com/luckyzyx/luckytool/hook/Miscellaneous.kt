package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.exsystemservice.RemoveWarningDialogThatAppRunsOnDesktop
import com.luckyzyx.luckytool.hook.apps.externalstorage.RemoveStorageLimit
import com.luckyzyx.luckytool.hook.apps.settings.DisableDPIRebootRecovery
import com.luckyzyx.luckytool.hook.apps.systemui.*
import com.luckyzyx.luckytool.utils.tools.*

class Miscellaneous : YukiBaseHooker() {
    override fun onHook() {
        loadApp("com.android.systemui"){
            //禁用复制悬浮窗
            if (prefs(XposedPrefs).getBoolean("disable_duplicate_floating_window",false)){
                if (SDK >= A13) loadHooker(DisableDuplicateFloatingWindow())
            }

            //充电纹波
            if (prefs(XposedPrefs).getBoolean("show_charging_ripple",false)) {
                if (SDK >= A12) loadHooker(ShowChargingRipple())
            }

            //禁用耳机高音量警告
            if (prefs(XposedPrefs).getBoolean("disable_headphone_high_volume_warning",false)){
                loadHooker(DisableHeadphoneHighVolumeWarning())
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
            @Suppress("ConstantConditionIf")
            if (false){
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
        loadApp("com.android.externalstorage"){
            //移除存储限制
            if (prefs(XposedPrefs).getBoolean("remove_storage_limit",false)) {
                loadHooker(RemoveStorageLimit())
            }
        }
    }
}