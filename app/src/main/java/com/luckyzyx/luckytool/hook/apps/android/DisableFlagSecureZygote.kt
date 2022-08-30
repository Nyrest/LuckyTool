package com.luckyzyx.luckytool.hook.apps.android

import android.view.SurfaceView
import android.view.Window
import android.view.WindowManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class DisableFlagSecureZygote : YukiBaseHooker() {
    override fun onHook() {
        findClass("android.view.WindowManagerGlobal").hook {
            injectMember {
                method {
                    name = "addView"
                }
                beforeHook {
                    if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)){
                        val params = args[1] as WindowManager.LayoutParams
                        params.flags = params.flags and WindowManager.LayoutParams.FLAG_SECURE.inv()
                    }
                }
            }
            injectMember {
                method {
                    name = "updateViewLayout"
                }
                beforeHook {
                    if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)){
                        val params = args[1] as WindowManager.LayoutParams
                        params.flags = params.flags and WindowManager.LayoutParams.FLAG_SECURE.inv()
                    }
                }
            }
        }

        Window::class.java.hook {
            injectMember {
                method {
                    name = "setFlags"
                }
                beforeHook {
                    if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)){
                        var flags: Int = args[0] as Int
                        flags = flags and WindowManager.LayoutParams.FLAG_SECURE.inv()
                        args(0).set(flags)
                    }
                }
            }
        }

        SurfaceView::class.java.hook {
            injectMember {
                method {
                    name = "setSecure"
                }
                beforeHook {
                    if (prefs(XposedPrefs).getBoolean("disable_flag_secure",false)) args(0).setFalse()
                }
            }
        }
    }
}