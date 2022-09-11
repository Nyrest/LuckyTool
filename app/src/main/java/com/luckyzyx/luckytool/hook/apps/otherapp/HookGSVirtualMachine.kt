package com.luckyzyx.luckytool.hook.apps.otherapp

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookGSVirtualMachine : YukiBaseHooker() {
    override fun onHook() {
//        去更新
        findClass("com.vphonegaga.titan.beans.AppUpdateBean").hook {
            injectMember {
                method {
                    name = "isForceUpdate"
                }
                replaceToFalse()
            }
            injectMember {
                method {
                    name = "isLatest"
                }
                replaceToTrue()
            }
        }
        findClass("com.vphonegaga.titan.user.User").hook {
            injectMember {
                method {
                    name = "isVip"
                }

                if (prefs(XposedPrefs).getBoolean("enable_gs_vip_function",false)) replaceToTrue()
            }
        }
        findClass("com.vphonegaga.titan.user.User.Builder").hook {
            injectMember {
                method {
                    name = "isVip"
                }
                beforeHook {
                    if (prefs(XposedPrefs).getBoolean("enable_gs_vip_function",false)){
                        args(0).setTrue()
                    }
                }
            }
        }
    }
}