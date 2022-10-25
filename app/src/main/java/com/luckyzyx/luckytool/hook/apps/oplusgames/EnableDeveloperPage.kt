package com.luckyzyx.luckytool.hook.apps.oplusgames

import android.app.Activity
import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.type.android.BundleClass
import com.highcapable.yukihookapi.hook.type.android.IntentClass
import com.highcapable.yukihookapi.hook.type.android.LayoutInflaterClass
import com.highcapable.yukihookapi.hook.type.java.IntType
import com.highcapable.yukihookapi.hook.type.java.StringType

class EnableDeveloperPage : YukiBaseHooker() {
    override fun onHook() {
        //Source GameDevelopOptionsActivity
        searchClass {
            from("business.compact.activity").absolute()
            field {
                type = StringType
            }.count(1)
            method {
                emptyParam()
            }.count(5..7)
            method {
                emptyParam()
                returnType = IntType
            }.count(2)
            method {
                name = "onCreate"
                param(BundleClass)
            }.count(1)
            method {
                param(LayoutInflaterClass)
            }.count(1..2)
            method {
                param(IntentClass)
            }.count(1)
        }.get()?.hook {
            injectMember {
                method {
                    name = "onCreate"
                    param(BundleClass)
                }
                beforeHook {
                    instance<Activity>().intent.putExtra("gameDevelopOptions","GameDevelopOptionsActivity")
                }
            }
        }
    }
}