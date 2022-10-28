package com.luckyzyx.luckytool.hook.apps.launcher

import android.widget.Button
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveRecentTaskListClearButton : YukiBaseHooker() {
    override fun onHook() {
        //Source OplusClearAllPanelView -> inflate
        findClass("com.oplus.quickstep.views.OplusClearAllPanelView").hook {
            injectMember {
                method {
                    name = "inflateIfNeeded"
                }
                afterHook {
                    field {
                        name = "mClearAllBtn"
                    }.get(instance).cast<Button>()?.isVisible = false
                }
            }
        }
    }
}