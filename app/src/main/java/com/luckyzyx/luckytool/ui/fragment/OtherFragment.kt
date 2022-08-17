package com.luckyzyx.luckytool.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentOtherBinding
import com.luckyzyx.luckytool.utils.ShellUtils
import com.luckyzyx.luckytool.utils.jumpBatteryInfo
import com.luckyzyx.luckytool.utils.jumpEngineermode
import com.luckyzyx.luckytool.utils.jumpRunningApp

@Obfuscate
class OtherFragment : Fragment() {

    private lateinit var binding: FragmentOtherBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOtherBinding.inflate(inflater)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.quickEntryTitle.text = getString(R.string.quick_entry)
        binding.quickEntrySummary.text = getString(R.string.quick_entry_summer)
        binding.quickEntry.setOnClickListener {
            val quicklist = arrayListOf<String>()
            quicklist.add(getString(R.string.engineering_mode))
            quicklist.add(getString(R.string.charging_test))
            quicklist.add(getString(R.string.process_management))
            quicklist.add(getString(R.string.system_interface_adjustment))
            quicklist.add(getString(R.string.feedback_toolbox))
            quicklist.add(getString(R.string.developer_option))
            quicklist.add(getString(R.string.game_assistant_page))
            MaterialAlertDialogBuilder(requireActivity())
                .setCancelable(true)
                .setItems(quicklist.toTypedArray()) { _, which ->
                    when(which){
                        0 -> { jumpEngineermode()
                        }
                        1 -> { jumpBatteryInfo()
                        }
                        2 -> { jumpRunningApp(requireActivity()) }
                        3 -> { ShellUtils.execCommand("am start -n com.android.systemui/.DemoMode", true) }
                        4 -> { ShellUtils.execCommand("am start -n com.oplus.logkit/.activity.MainActivity", true) }
                        5 -> { ShellUtils.execCommand("am start -a com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS", true) }
                        6 -> { ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true) }
                    }
                }
                .show()
        }
        binding.displayFps.apply {
            text = getString(R.string.display_refresh_rate_cap)
        }.setOnCheckedChangeListener { buttonView, isChecked ->
            if (buttonView.isPressed) {
                ShellUtils.execCommand("su -c service call SurfaceFlinger 1034 i32 ${if (isChecked) 1 else 0}", true)
            }
        }
    }
}