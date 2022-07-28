package com.luckyzyx.colorosext.ui.refactor

import android.content.Context
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.luckyzyx.colorosext.R

class MyPreference(context: Context): Preference(context) {

    init {
        widgetLayoutResource = R.layout.preference_scope
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        val textView = holder.findViewById(R.id.ceshi)
    }
}