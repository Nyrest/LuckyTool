package com.luckyzyx.luckytool.hook.apps.systemui

import android.media.AudioManager
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.current

class DisableHeadphoneHighVolumeWarning : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oplusos.systemui.volume.VolumeDialogImplEx").hook {
            injectMember {
                constructor {
                    paramCount = 1
                }
                afterHook {
                    field {
                        name = "mAudioManager"
                    }.get(instance).cast<AudioManager>()?.current {
                        method {
                            name = "disableSafeMediaVolume"
                        }.call()
                    }
                }
            }
        }
    }
}