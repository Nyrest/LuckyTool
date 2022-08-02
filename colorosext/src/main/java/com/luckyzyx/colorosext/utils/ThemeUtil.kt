@file:Suppress("unused")

package com.luckyzyx.colorosext.utils

import android.content.Context
import com.google.android.material.color.DynamicColors
import com.luckyzyx.colorosext.R
import rikka.core.util.ResourceUtils

class ThemeUtil(private val context: Context) {

    private val supportDynamicColor = DynamicColors.isDynamicColorAvailable()
    private val useDynamicColor = SPUtils.getBoolean(context, SettingsPrefs,"use_dynamic_color",false)
    private val followSystem = SPUtils.getBoolean(context, SettingsPrefs,"theme_follow_system",true)

    fun isDynamicColor() : Boolean{
        return supportDynamicColor && useDynamicColor
    }

    fun isFollowSystem() : Boolean{
        return supportDynamicColor && followSystem
    }

    fun getNightRes(): Int {
        return if (ResourceUtils.isNightMode(context.resources.configuration))
            R.style.Theme_Luckyzyx
        else R.style.Theme_Luckyzyx
    }

    fun getColorRes(): Int {
        return when(SPUtils.getString(context, SettingsPrefs,"theme_color","SAKURA")){
                "SAKURA" -> R.style.Theme_Luckyzyx_sakura
                "MATERIAL_RED" -> R.style.Theme_Luckyzyx_material_red
                "MATERIAL_PINK" -> R.style.Theme_Luckyzyx_material_pink
                "MATERIAL_PURPLE" -> R.style.Theme_Luckyzyx_material_purple
                "MATERIAL_DEEP_PURPLE" -> R.style.Theme_Luckyzyx_material_deep_purple
                "MATERIAL_INDIGO" -> R.style.Theme_Luckyzyx_material_indigo
                "MATERIAL_BLUE" -> R.style.Theme_Luckyzyx_material_blue
                "MATERIAL_LIGHT_BLUE" -> R.style.Theme_Luckyzyx_material_light_blue
                "MATERIAL_CYAN" -> R.style.Theme_Luckyzyx_material_cyan
                "MATERIAL_TEAL" -> R.style.Theme_Luckyzyx_material_teal
                "MATERIAL_GREEN" -> R.style.Theme_Luckyzyx_material_green
                "MATERIAL_LIGHT_GREEN" ->  R.style.Theme_Luckyzyx_material_light_green
                "MATERIAL_LIME" -> R.style.Theme_Luckyzyx_material_lime
                "MATERIAL_YELLOW" -> R.style.Theme_Luckyzyx_material_yellow
                "MATERIAL_AMBER" -> R.style.Theme_Luckyzyx_material_amber
                "MATERIAL_ORANGE" -> R.style.Theme_Luckyzyx_material_orange
                "MATERIAL_DEEP_ORANGE" -> R.style.Theme_Luckyzyx_material_deep_orange
                "MATERIAL_BROWN" -> R.style.Theme_Luckyzyx_material_brown
                "MATERIAL_BLUE_GREY" -> R.style.Theme_Luckyzyx_material_blue_grey
                else -> R.style.Theme_Luckyzyx_sakura
        }
    }
}