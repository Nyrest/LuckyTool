package com.luckyzyx.colorosext.hook.otherapp

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.java.BooleanType

class HookMoreAnime : YukiBaseHooker() {
    private val prefsFile = "XposedSettings"
    override fun onHook() {
        //跳过启动广告页
        if (prefs(prefsFile).getBoolean("skip_startup_page", false)) {
            findClass(name = "com.east2d.haoduo.ui.activity.SplashActivity").hook {
                injectMember {
                    method {
                        name = "isAdOpen"
                        returnType = BooleanType
                    }
                    replaceToFalse()
                }
            }
        }
        //VIP 下载原图
        if(prefs(prefsFile).getBoolean("vip_download", false)) {
            findClass(name = "com.east2d.haoduo.mvp.browerimages.FunctionImageMainActivity").hook {
                injectMember {
                    method {
                        name = "isVip"
                        returnType = BooleanType
                    }
                    replaceToTrue()
                }
            }
        }
    }
}