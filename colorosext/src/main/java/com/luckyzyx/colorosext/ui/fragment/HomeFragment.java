package com.luckyzyx.colorosext.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.luckyzyx.colorosext.R;
import com.luckyzyx.colorosext.utils.Prefs;
import com.luckyzyx.colorosext.utils.ShellUtils;

import java.util.Objects;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        Objects.requireNonNull(activity).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.add(0, 1, 0, "重启").setIcon(R.drawable.ic_baseline_refresh_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        menu.add(0, 2, 0, "设置").setIcon(R.drawable.ic_baseline_settings_24).setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                refreshmode(requireActivity());
                break;
            case 2:
//                startActivity(new Intent(requireActivity(), new SettingsFragment()));
                switchParentFragment(new SettingsFragment());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void switchParentFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_container,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private static void refreshmode(Context context) {
        final String[] list = {"重启系统","关闭系统","Recovery","BootLoader"};
            new MaterialAlertDialogBuilder(context)
                    .setCancelable(true)
                    .setItems(list, (dialog, which) -> {
                        switch (list[which]) {
                            case "重启系统":
                                ShellUtils.execCommand("reboot", true);
                                break;
                            case "关闭系统":
                                ShellUtils.execCommand("reboot -p", true);
                                break;
                            case "Recovery":
                                ShellUtils.execCommand("reboot recovery", true);
                                break;
                            case "BootLoader":
                                ShellUtils.execCommand("reboot bootloader", true);
                                break;
                        }
                    })
                    .show();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            getPreferenceManager().setSharedPreferencesName(Prefs.SettingsPreference);
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
        }
    }
}