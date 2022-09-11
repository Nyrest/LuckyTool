package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class LockScreenCentered : YukiBaseHooker() {
    override fun onHook() {
        //Source RedHorizontalSingleClockView
        findClass("com.oplusos.systemui.keyguard.clock.RedHorizontalSingleClockView").hook {
            injectMember {
                method {
                    name = "onFinishInflate"
                }
                afterHook {
                    val container = instance as LinearLayout
                    container.setPadding(0,50,0,0)

                    val week = field { name = "mTvWeek" }.get(instance).cast<TextView>()!!
                    val weekParams = LinearLayout.LayoutParams(week.layoutParams).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    week.layoutParams = weekParams

                    val clock = field { name = "mTvColon" }.get(instance).cast<TextView>()?.parent as RelativeLayout
                    val clockParams = LinearLayout.LayoutParams(clock.layoutParams).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    clock.layoutParams = clockParams

                    val date = field { name = "mTvDate" }.get(instance).cast<TextView>()!!
                    val dateParams = LinearLayout.LayoutParams(date.layoutParams).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    date.layoutParams = dateParams

                    val calendar = field { name = "mTvLunarCalendar" }.get(instance).cast<TextView>()!!
                    val calendarParams = LinearLayout.LayoutParams(calendar.layoutParams).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    calendar.layoutParams = calendarParams

                    val extra = field { name = "mTvExtraContent" }.get(instance).cast<TextView>()!!
                    val extraParams = LinearLayout.LayoutParams(extra.layoutParams).apply {
                        gravity = Gravity.CENTER_HORIZONTAL
                    }
                    extra.layoutParams = extraParams
                }
            }
        }
    }
}