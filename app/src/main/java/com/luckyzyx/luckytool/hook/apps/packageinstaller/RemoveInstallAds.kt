package com.luckyzyx.luckytool.hook.apps.packageinstaller

import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
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
                    suggestLayout[0] = field { name = "mSuggestLayoutA" }.get(instance).self as LinearLayout
                    suggestLayout[1] = field { name = "mSuggestLayoutB" }.get(instance).self as LinearLayout
                    suggestLayout[2] = field { name = "mSuggestLayoutC" }.get(instance).self as LinearLayout
                    suggestLayoutARelativeLayout[0] = field { name = "mSuggestLayoutATitle" }.get(instance).self as RelativeLayout
                }
            }.onNoSuchMemberFailure {
                loggerD(msg = "NoSuchMember->InstallAppProgress->initView")
            }
        }.onHookClassNotFoundFailure {
            loggerD(msg = "ClassNotFound->InstallAppProgress")
        }
        //Source InstallAppProgress
        findClass("com.android.packageinstaller.oplus.InstallAppProgress$1").hook {
            injectMember {
                method {
                    name = "handleMessage"
                    param(MessageClass)
                }
                afterHook {
                    suggestLayout[0]?.visibility = View.GONE
                    suggestLayout[1]?.visibility = View.GONE
                    suggestLayout[2]?.visibility = View.GONE
                    suggestLayoutARelativeLayout[0]?.visibility = View.GONE
                }
            }.onNoSuchMemberFailure {
                loggerD(msg = "NoSuchMember->InstallAppProgress$1->handleMessage")
            }
        }.onHookClassNotFoundFailure {
            loggerD(msg = "ClassNotFound->InstallAppProgress\$1")
        }
    }
}