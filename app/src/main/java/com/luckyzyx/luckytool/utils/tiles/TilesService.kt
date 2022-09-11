package com.luckyzyx.luckytool.utils.tiles

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.luckyzyx.luckytool.utils.tools.ShellUtils
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
class ShowFPS : TileService() {
    override fun onClick() {
        super.onClick()
        val tile = qsTile
        when(tile.state){
            Tile.STATE_INACTIVE -> {
                showFPS(true)
                tile.state = Tile.STATE_ACTIVE
            }
            Tile.STATE_ACTIVE -> {
                showFPS(false)
                tile.state = Tile.STATE_INACTIVE
            }
            Tile.STATE_UNAVAILABLE -> {}
        }
        tile.updateTile()
    }
    private fun showFPS(showFPS: Boolean){
        ShellUtils.execCommand("su -c service call SurfaceFlinger 1034 i32 ${if (showFPS) 1 else 0}", true)
    }
}