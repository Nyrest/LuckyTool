package com.luckyzyx.luckytool.hook.apps.launcher

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.IntArrayClass
import com.luckyzyx.luckytool.utils.XposedPrefs
import java.util.stream.Collectors

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
        val getStr = prefs(XposedPrefs).getString("launcher_layout_row_colume","")
        val splitStr = getStr.split("@", limit = 2)
        val colume = splitStr[0].split(",").stream().map(Integer::parseInt).collect(Collectors.toList()).toIntArray()
        val row = splitStr[1].split(",").stream().map(Integer::parseInt).collect(Collectors.toList()).toIntArray()
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
                    }.get().set(colume)
                    field {
                        name = "MIN_MAX_ROW"
                        type = IntArrayClass
                    }.get().set(row)
                }
            }
        }
    }
}