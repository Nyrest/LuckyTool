package com.luckyzyx.luckytool.hook.apps.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntArrayClass
import com.luckyzyx.luckytool.utils.XposedPrefs

class LauncherLayoutRowColume : YukiBaseHooker() {
    override fun onHook() {
        //Source UiConfig
        findClass("com.android.launcher.UiConfig").hook {
            injectMember {
                method {
                    name = "isSupportLayout"
                }
                replaceToTrue()
            }
        }
        val maxRows = prefs(XposedPrefs).getInt("launcher_layout_max_rows",6)
        val maxColumns = prefs(XposedPrefs).getInt("launcher_layout_max_columns",4)
        //Source ToggleBarLayoutAdapter
        findClass("com.android.launcher.togglebar.adapter.ToggleBarLayoutAdapter").hook {
            injectMember {
                method {
                    name = "initToggleBarLayoutConfigs"
                }
                beforeHook {
                    field {
                        name = "MIN_MAX_COLUMN"
                        type = IntArrayClass
                    }.get().cast<IntArray>()?.set(1,maxColumns)
                    field {
                        name = "MIN_MAX_ROW"
                        type = IntArrayClass
                    }.get().cast<IntArray>()?.set(1,maxRows)
                }
            }
        }
    }
}