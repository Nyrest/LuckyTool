package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.View
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class PowerMenu : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.systemui.globalactions.GlobalActionsDialog.ActionsDialog").hook {
            //显示SOS按钮
            injectMember {
                method {
                    name = "isShowSosButton"
                    returnType = BooleanType
                }
                replaceAny { prefs(XposedPrefs).getBoolean("power_menu_sos_button",false) }
            }
            //显示手动锁定
            injectMember {
                method {
                    name = "isShowManuallyLock"
                    returnType = BooleanType
                }
                replaceAny { prefs(XposedPrefs).getBoolean("power_menu_lock_button",false) }
            }
            //支持手动锁定
            injectMember {
                method {
                    name = "isSupportManuallyLock"
                    returnType = BooleanType
                }
                replaceAny { prefs(XposedPrefs).getBoolean("power_menu_lock_button",false) }
            }
            //菜单排列方式 -> false横向true默认竖向
            injectMember {
                method {
                    name = "isSimpleLayout"
                    returnType = BooleanType
                }
                replaceAny { prefs(XposedPrefs).getBoolean("power_menu_simple_layout",true) }
            }
            //移除添加控件UI
            injectMember {
                method {
                    name = "initializeLayout"
                }
                afterHook {
                    if (prefs(XposedPrefs).getBoolean("power_menu_remove_add_controls",false)){
                        (field {
                            name = "mPanelView"
                        }.get(instance).cast<View>()?.parent as View).isVisible = false
                    }
                }
            }
        }
    }
}