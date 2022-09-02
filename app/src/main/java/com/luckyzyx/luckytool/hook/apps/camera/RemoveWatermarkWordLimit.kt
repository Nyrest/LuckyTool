package com.luckyzyx.luckytool.hook.apps.camera

import android.util.ArraySet
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.CharSequenceType
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class RemoveWatermarkWordLimit : YukiBaseHooker() {
    override fun onHook() {
        // Source CameraSubSettingFragment
        // Log camera_namelength_outofrange
        val appSet = prefs(XposedPrefs).getStringSet(packageName, ArraySet()).toTypedArray()
        val clazz = when(appSet[2]){
            "8d5b992","38e5b1a" -> "com.oplus.camera.ui.menu.setting.p.7"
            "c7732c4" -> "com.oplus.camera.ui.menu.setting.p.5"
            else -> "com.oplus.camera.setting.j.5"
        }
        clazz.clazz.hook {
            injectMember {
                method {
                    paramCount = 6
                    returnType = CharSequenceType
                }
                replaceTo(null)
            }
        }
    }
}