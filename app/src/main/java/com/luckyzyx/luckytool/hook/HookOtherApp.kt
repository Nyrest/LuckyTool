package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.otherapp.HookAlphaBackupPro
import com.luckyzyx.luckytool.hook.apps.otherapp.HookMoreAnime
import com.luckyzyx.luckytool.hook.apps.otherapp.HookGSVirtualMachine

class HookOtherApp : YukiBaseHooker(){
    override fun onHook() {
        //好多动漫
        loadApp("com.east2d.everyimage",HookMoreAnime())

        //Alpha Backup Pro
        loadApp("com.ruet_cse_1503050.ragib.appbackup.pro",HookAlphaBackupPro())

        //光速虚拟机
        loadApp("com.vphonegaga.titan",HookGSVirtualMachine())
    }
}
