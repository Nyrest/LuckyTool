package com.luckyzyx.luckytool.hook.apps.settings

import android.content.Context
import android.provider.Settings
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.factory.MembersType
import com.highcapable.yukihookapi.hook.factory.field
import com.highcapable.yukihookapi.hook.type.java.IntType
import java.util.*
import kotlin.math.max
import kotlin.math.min

@Suppress("LocalVariableName")
class DisableDPIRebootRecovery : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusDensityPreference
        findClass("com.oplus.settings.widget.preference.OplusDensityPreference").hook {
            var context: Context? = null
            injectMember {
                allMembers(MembersType.CONSTRUCTOR)
                afterHook {
                    context = args(0).cast<Context>()
                }
            }
            injectMember {
                method {
                    name = "onPreferenceChange"
                    paramCount = 2
                }
                afterHook {
                    val obj = args(1).cast<Any>()
                    val moduleType = "com.oplus.backup.sdk.common.utils.ModuleType".toClass()
                    val TYPE_WEATHER = moduleType.field {
                        name = "TYPE_WEATHER"
                        type = IntType
                    }.get().int()
                    val displayMetrics = context!!.applicationContext.resources.displayMetrics
                    val min = min(displayMetrics.widthPixels, displayMetrics.heightPixels) * 160 / max(obj.toString().toInt(), TYPE_WEATHER)
                    val max = max(min, 120)
                    Settings.Secure.putString(context!!.contentResolver, "display_density_forced", max.toString())
                }
            }
        }
    }
}