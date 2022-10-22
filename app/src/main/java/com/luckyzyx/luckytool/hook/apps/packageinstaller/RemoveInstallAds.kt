package com.luckyzyx.luckytool.hook.apps.packageinstaller

import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.MessageClass

class RemoveInstallAds : YukiBaseHooker() {
    private val suggestLayout = arrayOfNulls<LinearLayout>(3)
    private val suggestLayoutARelativeLayout = arrayOfNulls<RelativeLayout>(1)
    override fun onHook() {
        //Source InstallAppProgress
        findClass("com.android.packageinstaller.oplus.InstallAppProgress").hook {
            injectMember {
                method {
                    name = "initView"
                }
                afterHook {
                    suggestLayout[0] = field { name = "mSuggestLayoutA" }.get(instance).cast<LinearLayout>()
                    suggestLayout[1] = field { name = "mSuggestLayoutB" }.get(instance).cast<LinearLayout>()
                    suggestLayout[2] = field { name = "mSuggestLayoutC" }.get(instance).cast<LinearLayout>()
                    suggestLayoutARelativeLayout[0] = field { name = "mSuggestLayoutATitle" }.get(instance).cast<RelativeLayout>()
                }
            }
        }
        //Source InstallAppProgress
        findClass("com.android.packageinstaller.oplus.InstallAppProgress$1").hook {
            injectMember {
                method {
                    name = "handleMessage"
                    param(MessageClass)
                }
                afterHook {
                    suggestLayout[0]?.isVisible = false
                    suggestLayout[1]?.isVisible = false
                    suggestLayout[2]?.isVisible = false
                    suggestLayoutARelativeLayout[0]?.isVisible = false
                }
            }
        }
    }
}