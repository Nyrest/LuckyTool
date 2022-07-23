package com.luckyzyx.colorosext.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.luckyzyx.colorosext.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFragment()
    }

    private fun initFragment() {
        val tabs = findViewById<TabLayout>(R.id.tabs)
        tabs.addTab(tabs.newTab().setText("系统框架"), 0, true)
        tabs.addTab(tabs.newTab().setText("系统界面"), 1, false)
        tabs.addTab(tabs.newTab().setText("系统其他"), 2, false)
        tabs.addTab(tabs.newTab().setText("三方APP"), 3, false)
    }


}