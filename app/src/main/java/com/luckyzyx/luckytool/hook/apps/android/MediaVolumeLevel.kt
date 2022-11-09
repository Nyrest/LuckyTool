package com.luckyzyx.luckytool.hook.apps.android

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class MediaVolumeLevel : YukiBaseHooker() {
    override fun onHook() {
        //Source AudioService
        findClass("com.android.server.audio.AudioService").hook {
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook {
                    field {
                        name = "MAX_STREAM_VOLUME"
                    }.get(instance).cast<IntArray>()?.set(3, prefs(XposedPrefs).getInt("media_volume_level", 0))
                }
            }
        }
    }
}