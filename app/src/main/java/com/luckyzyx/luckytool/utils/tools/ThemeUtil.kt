@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.tools

import android.content.Context
import com.google.android.material.color.DynamicColors

class ThemeUtil(context: Context) {

    private val supportDynamicColor = DynamicColors.isDynamicColorAvailable()
    private val useDynamicColor = context.getBoolean(SettingsPrefs,"use_dynamic_color",false)
    private val followSystem = context.getBoolean(SettingsPrefs,"theme_follow_system",true)

    fun isDynamicColor() : Boolean{
        return supportDynamicColor && useDynamicColor
    }

    fun isFollowSystem() : Boolean{
        return supportDynamicColor && followSystem
    }
}