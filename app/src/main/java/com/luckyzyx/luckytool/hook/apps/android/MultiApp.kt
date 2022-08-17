package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.luckyzyx.luckytool.utils.XposedPrefs

class MultiApp : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplus.multiapp.OplusMultiAppConfig").hook {
            injectMember {
                method {
                    name = "getAllowedPkgList"
                    returnType = ListClass
                }
                beforeHook {
                    if (prefs(XposedPrefs).getBoolean("multi_app_enable",false)){
                        val appList = ArrayList<String>()
                        for (i in prefs(XposedPrefs).getStringSet("enabledMulti",HashSet())){
                            appList.add(i)
                        }
                        field {
                            name = "mAllowedPkgList"
                            type = ListClass
                        }.get(instance).set(appList)
                    }
                }
            }.onNoSuchMemberFailure {
                loggerD(msg = "NoSuchMember -> getAllowedPkgList")
            }
        }.onHookClassNotFoundFailure {
            loggerD(msg = "ClassNotFound -> OplusMultiAppConfig")
        }
    }
}