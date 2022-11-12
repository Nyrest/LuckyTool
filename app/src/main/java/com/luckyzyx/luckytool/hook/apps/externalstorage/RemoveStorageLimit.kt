package com.luckyzyx.luckytool.hook.apps.externalstorage

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class RemoveStorageLimit : YukiBaseHooker(){
    override fun onHook() {
        //Source ExternalStorageProvider
        findClass("com.android.externalstorage.ExternalStorageProvider").hook {
            injectMember {
                method {
                    name = "shouldBlockFromTree"
                }
                replaceToFalse()
            }
        }
    }
}