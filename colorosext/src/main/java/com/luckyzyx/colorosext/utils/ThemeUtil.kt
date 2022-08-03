@file:Suppress("unused")

package com.luckyzyx.colorosext.utils

import android.content.Context
import com.google.android.material.color.DynamicColors

class ThemeUtil(context: Context) {

    private val supportDynamicColor = DynamicColors.isDynamicColorAvailable()
    private val useDynamicColor = SPUtils.getBoolean(context, SettingsPrefs,"use_dynamic_color",false)
    private val followSystem = SPUtils.getBoolean(context, SettingsPrefs,"theme_follow_system",true)

    fun isDynamicColor() : Boolean{
        return supportDynamicColor && useDynamicColor
    }

    fun isFollowSystem() : Boolean{
        return supportDynamicColor && followSystem
    }
}