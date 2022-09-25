package com.luckyzyx.luckytool.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textview.MaterialTextView
import com.joom.paranoid.Obfuscate
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentOtherBinding
import com.luckyzyx.luckytool.utils.tools.*

@Obfuscate
class OtherFragment : Fragment() {

    private lateinit var binding: FragmentOtherBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOtherBinding.inflate(inflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.quickEntryTitle.text = getString(R.string.quick_entry)
        binding.quickEntrySummary.text = getString(R.string.quick_entry_summary)
        binding.quickEntry.setOnClickListener {
            val quicklist = arrayListOf<String>()
            quicklist.add(getString(R.string.engineering_mode))
            quicklist.add(getString(R.string.charging_test))
            quicklist.add(getString(R.string.battery_health))
            quicklist.add(getString(R.string.process_manager))
            quicklist.add(getString(R.string.system_interface_adjustment))
            quicklist.add(getString(R.string.feedback_toolbox))
            quicklist.add(getString(R.string.developer_option))
            quicklist.add(getString(R.string.game_assistant_page))
            MaterialAlertDialogBuilder(requireActivity())
                .setCancelable(true)
                .setItems(quicklist.toTypedArray()) { _, which ->
                    when (which) {
                        0 -> jumpEngineermode(requireActivity())
                        1 -> jumpBatteryInfo(requireActivity())
                        2 -> {
                            if (SDK < 33) {
                                requireActivity().toast(getString(R.string.battery_health_toast))
                                return@setItems
                            }
                            ShellUtils.execCommand("am start -n com.oplus.battery/com.oplus.powermanager.fuelgaue.BatteryHealthActivity", true)
                        }
                        3 -> safeOf(default = requireActivity().toast(getString(R.string.system_not_support))) {
                            jumpRunningApp(requireActivity())
                        }
                        4 -> ShellUtils.execCommand("am start -n com.android.systemui/.DemoMode", true)
                        5 -> ShellUtils.execCommand("am start -n com.oplus.logkit/.activity.MainActivity", true)
                        6 -> ShellUtils.execCommand("am start -a com.android.settings.APPLICATION_DEVELOPMENT_SETTINGS", true)
                        7 -> ShellUtils.execCommand("am start -n com.oplus.games/business.compact.activity.GameBoxCoverActivity", true)
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

        binding.remoteAdbDebugTitle.text = getString(R.string.remote_adb_debug_title)
        binding.remoteAdbDebugSummary.text = getString(R.string.remote_adb_debug_summary)
        binding.remoteAdbDebug.setOnClickListener {
            val getPort = ShellUtils.execCommand("getprop service.adb.tcp.port", true, true).successMsg
            val getIP = ShellUtils.execCommand("ifconfig wlan0 | grep 'inet addr' | awk '{ print $2}' | awk -F: '{print $2}' 2>/dev/null", true, true).successMsg.let {
                if (it == "") "IP" else it
            }
            val adbDialog = MaterialAlertDialogBuilder(requireActivity()).apply {
                setCancelable(true)
                setView(R.layout.layout_adb_dialog)
            }.show()

            adbDialog.findViewById<TextInputLayout>(R.id.adb_port_layout)?.apply {
                hint = context.getString(R.string.adb_port)
                isHintEnabled = true
                isHintAnimationEnabled = true
                isCounterEnabled = true
                counterMaxLength = 6
            }
            val adbPort = adbDialog.findViewById<TextInputEditText>(R.id.adb_port)?.apply {
                inputType = EditorInfo.TYPE_CLASS_NUMBER
                setText(requireActivity().getString(OtherPrefs,"adb_port","6666"))
            }
            val adbTv = adbDialog.findViewById<MaterialTextView>(R.id.adb_tv)?.apply {
                gravity = Gravity.CENTER
                if (!(getPort == "" || getPort.toInt() == -1)){
                    text = "adb connect $getIP:$getPort"
                }
            }

            adbDialog.findViewById<SwitchMaterial>(R.id.adb_switch)?.apply {
                text = context.getString(R.string.enable_remote_adb_debugging)
                isChecked = !(getPort == "" || getPort.toInt() == -1)
                setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked){
                        val port = adbPort?.text.toString()
                        if (port == "") {
                            this.isChecked = false
                            adbTv?.text = context.getString(R.string.adb_debug_port_cannot_null)
                            return@setOnCheckedChangeListener
                        }
                        val commands = arrayOf(
                            "setprop service.adb.tcp.port '$port'",
                            "stop adbd",
                            "killall -9 adbd 2>/dev/null",
                            "start adbd"
                        )
                        ShellUtils.execCommand(commands,true)
                        requireActivity().putString(OtherPrefs,"adb_port",port)
                        adbTv?.text = "adb connect $getIP:$port"
                    }else{
                        val commands = arrayOf(
                            "setprop service.adb.tcp.port -1",
                            "stop adbd",
                            "killall -9 adbd 2>/dev/null",
                            "start adbd",
                            "setprop service.adb.tcp.port ''"
                        )
                        ShellUtils.execCommand(commands,true)
                        adbTv?.text = null
                    }
                }
            }
        }
    }
}
