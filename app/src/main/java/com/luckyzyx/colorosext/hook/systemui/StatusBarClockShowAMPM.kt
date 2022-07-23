package com.luckyzyx.tools.hook.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.IntType

class StatusBarClockShowAMPM : YukiBaseHooker() {
    private val PrefsFile = "XposedSettings"
    override fun onHook() {
        findClass("com.android.systemui.statusbar.policy.Clock").hook {
            injectMember {
                method {
                    name = "getSmallTime"
                }
                beforeHook {
                    field {
                        name = "mAmPmStyle"
                        type = IntType
                    }.get(instance).set(prefs(PrefsFile).getString("statusbar_clock_show_ampm","2").toInt())
                }
            }
        }
//        findClass("com.oplusos.systemui.qs.widget.OplusQSClock").hook {
//            injectMember {
//                method {
//                    name = "setTypetFaceWithFontVariation"
//                }
//                beforeHook {
//                    field {
//                        name = "FONT_VARIATION_DEFAULT_VALUE"
//                    }.get().set("'wght' 10")
//                }
//            }
//        }


    }
}