package com.luckyzyx.colorosext.ui.application

import androidx.appcompat.app.AppCompatDelegate
import com.highcapable.yukihookapi.hook.xposed.application.ModuleApplication

class MyApplication : ModuleApplication(){
    override fun onCreate() {
        super.onCreate()
        //跟随系统改变深色模式
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}