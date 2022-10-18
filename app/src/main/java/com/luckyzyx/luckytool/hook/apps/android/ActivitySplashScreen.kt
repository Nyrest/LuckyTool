package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.SDK
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class ActivitySplashScreen : YukiBaseHooker() {
    override fun onHook() {
        //Source StartingSurfaceController
        findClass("com.android.server.wm.StartingSurfaceController").hook {
            injectMember {
                method {
                    name = "showStartingWindow"
                    paramCount = 5
                }
                if (prefs(XposedPrefs).getBoolean("remove_app_splash_screen", false)) replaceTo(null)
            }
        }


        return
        @Suppress("UNREACHABLE_CODE")

        findClass("com.android.server.wm.ActivityRecord").hook {
            //强制显示遮罩
            injectMember {
                method {
                    name = "validateStartingWindowTheme"
                    paramCount(3)
                }
                beforeHook {
//                    val packName = args(1).string()
//                    resultTrue()
                }
            }
            //关闭SplashScreen
            injectMember {
                method {
                    name = "addStartingWindow"
                    paramCount(
                        when (SDK) {
                            33 -> 10
                            else -> 15
                        }
                    )
                }
//                replaceToFalse()
            }

            // 热启动时生成启动遮罩
            injectMember {
                method {
                    name = "getStartingWindowType"
                    paramCount(
                        when (SDK) {
                            33 -> 7
                            else -> 6
                        }
                    )
                }
                beforeHook {
//                    result = 2
                }
            }
        }
    }
}