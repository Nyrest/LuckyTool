package com.luckyzyx.tools.hook.themestore

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker

class UnlockThemeStoreVip : YukiBaseHooker() {
    override fun onHook() {
        findClass("com.oppo.cdo.card.theme.dto.vip.VipUserDto").hook {
            injectMember {
                method {
                    name = "getVipStatus"
                }
                replaceTo(1)
            }
            injectMember {
                method {
                    name = "getVipDays"
                }
                replaceTo(999)
            }
        }
        findClass("com.oppo.cdo.theme.domain.dto.response.PublishProductItemDto").hook {
            injectMember {
                method {
                    name = "getIsVipAvailable"
                }
                replaceTo(1)
            }
        }
    }
}