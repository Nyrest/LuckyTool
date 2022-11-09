package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.defined.VagueType
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class MediaVolumeLevel : YukiBaseHooker() {
    override fun onHook() {
        findClass("android.os.SystemProperties").hook {
            injectMember {
                method {
                    name = "getInt"
                    param(StringType, VagueType)
                    returnType = IntType
                }
                afterHook {
                    if (args(0).cast<String>() == "ro.config.media_vol_steps") {
                        result = prefs(XposedPrefs).getInt("media_volume_level", 0)
                    }
                }
            }
        }
    }
}