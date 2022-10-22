package com.luckyzyx.luckytool.hook.apps.systemui

import android.view.Gravity
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.dp

class LockScreenCentered : YukiBaseHooker() {
    override fun onHook() {
        //Source RedHorizontalSingleClockView
        findClass("com.oplusos.systemui.keyguard.clock.RedHorizontalSingleClockView").hook {
            injectMember {
                method {
                    name = "onFinishInflate"
                }
                afterHook {
                    instance<LinearLayout>().setPadding(0, 20.dp, 0, 0)

                    field { name = "mTvWeek" }.get(instance).cast<TextView>()?.apply {
                        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }

                    (field { name = "mTvColon" }.get(instance).cast<TextView>()?.parent as RelativeLayout).apply {
                        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }

                    field { name = "mTvDate" }.get(instance).cast<TextView>()?.apply {
                        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }

                    field { name = "mTvLunarCalendar" }.get(instance).cast<TextView>()?.apply {
                        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }

                    field { name = "mTvExtraContent" }.get(instance).cast<TextView>()?.apply {
                        layoutParams = LinearLayout.LayoutParams(layoutParams).apply {
                            gravity = Gravity.CENTER_HORIZONTAL
                        }
                    }
                }
            }
        }
    }
}