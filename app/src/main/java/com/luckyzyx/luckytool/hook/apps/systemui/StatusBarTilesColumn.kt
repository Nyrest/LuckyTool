package com.luckyzyx.luckytool.hook.apps.systemui

import android.content.res.Configuration
import android.view.ViewGroup
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class StatusBarTilesColumn : YukiBaseHooker() {
    override fun onHook() {

        var isVertical = true
        val columnUnexpandedVertical = prefs(XposedPrefs).getInt("tile_unexpanded_columns_vertical",6)
        val columnUnexpandedHorizontal = prefs(XposedPrefs).getInt("tile_unexpanded_columns_horizontal",6)
        val columnExpandedVertical = prefs(XposedPrefs).getInt("tile_expanded_columns_vertical",4)
        val columnExpandedHorizontal = prefs(XposedPrefs).getInt("tile_expanded_columns_horizontal",6)

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

        findClass("com.android.systemui.qs.TileLayout").hook {
            injectMember {
                method {
                    name = "updateColumns"
                }
                afterHook {
                    val viewGroup = instance<ViewGroup>()
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

class StatusBarTilesColumnV13 : YukiBaseHooker() {
    override fun onHook() {

        val columnUnexpandedVerticalC13 = prefs(XposedPrefs).getInt("tile_unexpanded_columns_vertical_c13",5)
        val rowExpandedVerticalC13 = prefs(XposedPrefs).getInt("tile_expanded_rows_vertical_c13",3)
        val columnExpandedVerticalC13 = prefs(XposedPrefs).getInt("tile_expanded_columns_vertical_c13",4)
        val columnHorizontal = prefs(XposedPrefs).getInt("tile_columns_horizontal_c13",4)

        findClass("com.android.systemui.qs.QuickQSPanel").hook {
            injectMember {
                method {
                    name = "getNumQuickTiles"
                }
                replaceTo(columnUnexpandedVerticalC13)
            }
        }

        findClass("com.android.systemui.qs.TileLayout").hook {
            injectMember {
                method {
                    name = "updateMaxRows"
                }
                beforeHook {
                    field { name = "mMaxAllowedRows" }.get(instance).set(rowExpandedVerticalC13)
                }
            }
            injectMember {
                method {
                    name = "updateColumns"
                }
                afterHook {
                    instance<ViewGroup>().apply {
                        if (this.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
                            field { name = "mColumns" }.get(instance).set(columnExpandedVerticalC13)
                        } else {
                            field { name = "mColumns" }.get(instance).set(columnHorizontal)
                        }
                        requestLayout()
                    }
                }
            }
        }
    }
}