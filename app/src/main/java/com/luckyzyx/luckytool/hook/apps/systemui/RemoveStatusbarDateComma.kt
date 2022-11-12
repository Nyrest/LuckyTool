package com.luckyzyx.luckytool.hook.apps.systemui

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStatusbarDateComma : YukiBaseHooker() {
    override fun onHook() {
        //cn_comma
        resources().hook {
            injectResource {
                conditions {
                    name = "cn_comma"
                    string()
                }
                replaceTo(" ")
            }
        }
    }
}