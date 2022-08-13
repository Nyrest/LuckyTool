package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.res.Configuration
import android.view.ViewGroup
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBarTilesColumn : YukiBaseHooker() {
    override fun onHook() {

        val columnUnexpanded = prefs(XposedPrefs).getInt("tile_unexpanded_columns",6)
        val columnExpandedVertical = prefs(XposedPrefs).getInt("tile_expanded_columns_vertical",4)
        val columnExpandedHorizontal = prefs(XposedPrefs).getInt("tile_expanded_columns_horizontal",6)

        //未展开列数
        findClass("com.android.systemui.qs.QuickQSPanel").hook {
            injectMember {
                method {
                    name = "getNumQuickTiles"
                }
                replaceTo(columnUnexpanded)
            }
        }
        //展开列数
        findClass("com.android.systemui.qs.TileLayout").hook {
            injectMember {
                method {
                    name = "updateColumns"
                }
                afterHook {
                    val viewGroup = instance as ViewGroup
                    val mConfiguration = viewGroup.context.resources.configuration
                    if (mConfiguration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        field { name = "mColumns" }.get(instance).set(columnExpandedVertical)
                    } else {
                        field { name = "mColumns" }.get(instance).set(columnExpandedHorizontal)
                    }
                    viewGroup.requestLayout()
                }
            }
        }
    }
}