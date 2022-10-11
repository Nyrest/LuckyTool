package com.luckyzyx.luckytool.hook

import com.highcapable.yukihookapi.hook.entity.YukiBaseHooker
import com.luckyzyx.luckytool.hook.apps.otherapp.HookAlphaBackupPro
import com.luckyzyx.luckytool.hook.apps.otherapp.HookMoreAnime
import com.luckyzyx.luckytool.hook.apps.otherapp.HookGSVirtualMachine
import com.luckyzyx.luckytool.utils.tools.XposedPrefs

class HookOtherApp : YukiBaseHooker(){
    override fun onHook() {
        //好多动漫
        loadApp("com.east2d.everyimage",HookMoreAnime())

        //Alpha Backup Pro
        if (prefs(XposedPrefs).getBoolean("remove_check_license",false)){
            loadApp("com.ruet_cse_1503050.ragib.appbackup.pro",HookAlphaBackupPro())
        }

        //光速虚拟机
        if (prefs(XposedPrefs).getBoolean("enable_gs_vip_function",false)){
            loadApp("com.vphonegaga.titan",HookGSVirtualMachine())
        }
    }
}
