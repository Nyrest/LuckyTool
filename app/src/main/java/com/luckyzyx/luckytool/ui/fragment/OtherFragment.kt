package com.luckyzyx.luckytool.ui.fragment

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.luckyzyx.luckytool.R
import com.luckyzyx.luckytool.databinding.FragmentOtherBinding

class OtherFragment : Fragment() {

    private lateinit var binding: FragmentOtherBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentOtherBinding.inflate(inflater)
        initToolbar()
        return binding.root
    }

    private fun initToolbar(){
        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.nav_other)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.magiskInfo.apply {
            gravity = Gravity.CENTER
            text = "!!页面待开发!!"
        }
    }
}