package com.luckyzyx.colorosext.ui.refactor

import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment

typealias OnBackPressedTypeAlias = () -> Unit

/**
 * 解决 Fragment 中 OnBackPressed 事件, 默认结束当前Fragment依附的Activity
 * @param type true:结束当前Activity，false：响应callback回调
 */
fun Fragment.setOnHandleBackPressed(type: Boolean = true, callback: OnBackPressedTypeAlias? = null) {
    requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (type) {
                    requireActivity().finish()
                } else {
                    callback?.invoke()
                }
            }
        })
}
