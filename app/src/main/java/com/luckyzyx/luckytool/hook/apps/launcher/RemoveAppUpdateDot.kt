package com.luckyzyx.luckytool.hook.apps.launcher

import android.widget.TextView
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveAppUpdateDot : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusBubbleTextView
        findClass(name =  "com.android.launcher3.OplusBubbleTextView").hook {
            injectMember {
                method {
                    name = "applyLabel"
                    paramCount = 3
                }
                beforeHook {
                    //Source ItemInfo
                    val field = "com.android.launcher3.model.data.ItemInfo".clazz.getDeclaredField("title")
                    field.isAccessible = true
                    (instance as TextView).text = field[args[0]] as CharSequence
                    resultNull()
                }
            }
        }
    }
}