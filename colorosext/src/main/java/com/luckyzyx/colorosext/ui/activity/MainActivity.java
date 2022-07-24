package com.luckyzyx.colorosext.ui.activity;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luckyzyx.colorosext.R;
import com.luckyzyx.colorosext.ui.fragment.HomeFragment;
import com.luckyzyx.colorosext.ui.fragment.MagiskFragment;
import com.luckyzyx.colorosext.ui.fragment.XposedFragment;
import com.luckyzyx.colorosext.utils.Prefs;
import com.luckyzyx.colorosext.utils.SPUtils;
import com.luckyzyx.colorosext.utils.ShellUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkTheme(this);
        setContentView(R.layout.activity_main);

        initMaterialToolbar();
        initBottomNavigationView();
    }

    private void initMaterialToolbar(){
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    void checkTheme(Context context){
        String theme_color = SPUtils.getString(context,Prefs.SettingsPreference(),"theme_color","system");
        boolean use_md3 = SPUtils.getBoolean(context, Prefs.SettingsPreference(),"use_md3",false);
        switch (theme_color){
            case "light": AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case "dark": AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case "system": AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
        if (use_md3){
            context.setTheme(R.style.Theme_ColorOSExt_MD3);
        }else {
            context.setTheme(R.style.Theme_ColorOSExt);
        }
    }

    @SuppressWarnings("SameParameterValue")
    boolean isDarkMode(Context context, boolean isSystem){
        if (isSystem){
            UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
            return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
        }else {
            int uiMode = context.getResources().getConfiguration().uiMode;
            return (uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 0, "重启").setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 2, 0, "设置").setIcon(R.drawable.ic_baseline_settings_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == 1) refreshmode(this);
        if (item.getItemId() == 2) switchFragment(new HomeFragment.SettingsFragment(),true);
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("NonConstantResourceId")
    private void initBottomNavigationView() {
        switchFragment(new HomeFragment(),false);
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.nav_item_home);
        bottomNavigationView.setLabelVisibilityMode(BottomNavigationView.LABEL_VISIBILITY_SELECTED);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_item_home:
                    switchFragment(new HomeFragment(),false);
                    break;
                case R.id.nav_item_xposed:
                    switchFragment(new XposedFragment(),false);
                    break;
                case R.id.nav_item_magisk:
                    switchFragment(new MagiskFragment(),false);
                    break;
            }
            return true;
        });
    }

    public void switchFragment(Fragment fragment,boolean backStack){
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment current = fragmentManager.findFragmentById(R.id.fragment_container);
        if (current != null && current.getClass().equals(fragment.getClass())) return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (backStack){
            fragmentTransaction.setReorderingAllowed(true);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else {
            fragmentTransaction.commitNow();
        }
    }

    private void refreshmode(Context context) {
        final String[] list = {"重启系统","关闭系统","Recovery","BootLoader"};
        new MaterialAlertDialogBuilder(context)
                .setCancelable(true)
                .setItems(list, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            ShellUtils.execCommand("reboot", true);
                            break;
                        case 1:
                            ShellUtils.execCommand("reboot -p", true);
                            break;
                        case 2:
                            ShellUtils.execCommand("reboot recovery", true);
                            break;
                        case 3:
                            ShellUtils.execCommand("reboot bootloader", true);
                            break;
                    }
                })
                .show();
    }
}