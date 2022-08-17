package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.res.Configuration
import android.view.ViewGroup
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.XposedPrefs

class StatusBarTilesColumn : YukiBaseHooker() {
    override fun onHook() {

        var isVertical = true
        val columnUnexpandedVertical = prefs(XposedPrefs).getInt("tile_unexpanded_columns_vertical",6)
        val columnUnexpandedHorizontal = prefs(XposedPrefs).getInt("tile_unexpanded_columns_horizontal",6)
        val columnExpandedVertical = prefs(XposedPrefs).getInt("tile_expanded_columns_vertical",4)
        val columnExpandedHorizontal = prefs(XposedPrefs).getInt("tile_expanded_columns_horizontal",6)

        //未展开列数
        findClass("com.android.systemui.qs.QuickQSPanel").hook {
            injectMember {
                method {
                    name = "getNumQuickTiles"
                }
                afterHook {
                    result = if (isVertical){
                        columnUnexpandedVertical
                    }else{
                        columnUnexpandedHorizontal
                    }
                }
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
                    if (viewGroup.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                        isVertical = true
                        field { name = "mColumns" }.get(instance).set(columnExpandedVertical)
                    } else {
                        isVertical = false
                        field { name = "mColumns" }.get(instance).set(columnExpandedHorizontal)
                    }
                    viewGroup.requestLayout()
                }
            }
        }
    }
}