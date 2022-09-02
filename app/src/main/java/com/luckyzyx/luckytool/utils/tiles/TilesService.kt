package com.luckyzyx.luckytool.utils.tiles

import android.service.quicksettings.TileService
import com.luckyzyx.luckytool.utils.tools.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.tools.jumpRunningApp

class ChargingTest : TileService() {

    override fun onClick() {
        super.onClick()
        jumpBatteryInfo(applicationContext)
    }
}
class ProcessManager : TileService() {

    override fun onClick() {
        super.onClick()
        jumpRunningApp(applicationContext)
    }
}
class Test : TileService() {

    override fun onClick() {
        super.onClick()
        jumpRunningApp(applicationContext)
    }
}