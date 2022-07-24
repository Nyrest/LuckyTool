package com.luckyzyx.colorosext.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.luckyzyx.colorosext.R;
import com.luckyzyx.colorosext.ui.fragment.HomeFragment;
import com.luckyzyx.colorosext.ui.fragment.MagiskFragment;
import com.luckyzyx.colorosext.ui.fragment.XposedFragment;

public class MainActivity extends AppCompatActivity {

    private final HomeFragment homeFragment = new HomeFragment();
    private final XposedFragment xposedFragment = new XposedFragment();
    private final MagiskFragment magiskFragment = new MagiskFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomNavigationView();
    }

    @SuppressLint("NonConstantResourceId")
    private void initBottomNavigationView() {


        switchFragment(homeFragment);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.nav_item_home);
        bottomNavigationView.setLabelVisibilityMode(BottomNavigationView.LABEL_VISIBILITY_SELECTED);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_item_home:
                    switchFragment(homeFragment);
                    break;
                case R.id.nav_item_xposed:
                    switchFragment(xposedFragment);
                    break;
                case R.id.nav_item_magisk:
                    switchFragment(magiskFragment);
                    break;
            }
            return true;
        });
    }

    private void switchFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commitNow();
    }

    @SuppressWarnings("CommentedOutCode")
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            Fragment current = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//            if (current instanceof HomeFragment) finishAndRemoveTask();
//        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getBackStackEntryCount() ==1) {
            finish();
        }else {
            getSupportFragmentManager().popBackStack();
        }
    }
}