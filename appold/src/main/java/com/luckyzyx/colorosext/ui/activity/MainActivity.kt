package com.luckyzyx.colorosext.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.luckyzyx.colorosext.R
import com.luckyzyx.colorosext.ui.fragment.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNavigationView()
    }

    private fun initBottomNavigationView(){
        val homeFragment = HomeFragment()
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.nav_item_home -> {
                    switchFragment(homeFragment)
                }
                R.id.nav_item_xposed -> Toast.makeText(this,"xposed",Toast.LENGTH_SHORT).show()
                R.id.nav_item_magisk -> Toast.makeText(this,"magisk",Toast.LENGTH_SHORT).show()
            }
            true
        }
        //设置默认选中item
        bottomNavigationView.selectedItemId = R.id.nav_item_home
        //设置选中显示label
        bottomNavigationView.labelVisibilityMode = BottomNavigationView.LABEL_VISIBILITY_SELECTED
    }

    private fun switchFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        fragmentTransaction.replace(R.id.bottomNavigation, fragment).commitNow()
    }
}