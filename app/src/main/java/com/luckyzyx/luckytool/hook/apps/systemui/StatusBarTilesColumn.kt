package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.res.Configuration
import android.view.ViewGroup
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBarTilesColumn : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.android.systemui.qs.TileLayout").hook {
            injectMember {
                method {
                    name = "updateColumns"
                }
                afterHook {
                    val getStr = prefs(XposedPrefs).getString("set_statusbar_tiles_column","")
                    if (getStr == "") return@afterHook
                    val splitStr = getStr.split("@", limit = 2)
                    val columnV = splitStr[0].toInt()
                    val columnH = splitStr[1].toInt()
                    val viewGroup = instance as ViewGroup
                    val mConfiguration = viewGroup.context.resources.configuration
                    if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        field { name = "mColumns" }.get(instance).apply {
                            if (columnV != 4) this.set(columnV)
                        }
                    } else {
                        field { name = "mColumns" }.get(instance).apply {
                            if (columnH != 6) this.set(columnH)
                        }
                    }
                    viewGroup.requestLayout()
                }
            }
        }
    }
}