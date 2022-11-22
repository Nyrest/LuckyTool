package com.luckyzyx.luckytool.hook.apps.oplusgames

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.highcapable.yukihookapi.hook.log.loggerD
import com.highcapable.yukihookapi.hook.type.java.ArrayListClass
import com.highcapable.yukihookapi.hook.type.java.BooleanType
import com.highcapable.yukihookapi.hook.type.java.ListClass
import com.highcapable.yukihookapi.hook.type.java.StringType

class EnableSupportCompetitionMode : YukiBaseHooker() {
    override fun onHook() {
        //Source CompetitionModeManager
        //Search isSupportCompetitionMode
        searchClass {
            //714,715
            from("od", "rd").absolute()
            field { type = ListClass }.count(1)
            method {
                emptyParam()
                returnType = ListClass
            }.count(2)
            method {
                param(StringType, ArrayListClass)
            }.count(1)
            method {
                emptyParam()
                returnType = BooleanType
            }.count(6)
        }.get()?.hook {
            injectMember {
                method {
                    emptyParam()
                    returnType = BooleanType
                    order().index(2)
                }
                replaceToTrue()
            }
        } ?: loggerD(msg = "$packageName\nError -> EnableSupportCompetitionMode")
    }
}