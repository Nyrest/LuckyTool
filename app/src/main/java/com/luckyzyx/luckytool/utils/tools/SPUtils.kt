@file:Suppress("KDocUnresolvedReference", "unused", "MemberVisibilityCanBePrivate")

package com.luckyzyx.luckytool.utils.tools

import android.content.Context

fun Context.putString(PrefsName: String?, key: String?, value: String?): Boolean {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putString(key, value)
    return editor.commit()
}

fun Context.putStringSet(PrefsName: String?, key: String?, value: Set<String?>?): Boolean {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putStringSet(key, value)
    return editor.commit()
}

fun Context.getString(PrefsName: String?, key: String?): String? {
    return getString(PrefsName, key, null)
}

fun Context.getStringSet(PrefsName: String?, key: String?): Set<String>? {
    return getStringSet(PrefsName, key, null)
}

fun Context.getString(PrefsName: String?, key: String?, defaultValue: String?): String? {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    return prefs.getString(key, defaultValue)
}

fun Context.getStringSet(PrefsName: String?, key: String?, defaultValue: Set<String?>?): Set<String>? {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    return prefs.getStringSet(key, defaultValue)
}

fun Context.putInt(PrefsName: String?, key: String?, value: Int): Boolean {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putInt(key, value)
    return editor.commit()
}

fun Context.getInt(PrefsName: String?, key: String?): Int {
    return getInt(PrefsName, key, -1)
}

fun Context.getInt(PrefsName: String?, key: String?, defaultValue: Int): Int {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    return prefs.getInt(key, defaultValue)
}

fun Context.putLong(PrefsName: String?, key: String?, value: Long): Boolean {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putLong(key, value)
    return editor.commit()
}

fun Context.getLong(PrefsName: String?, key: String?): Long {
    return getLong(PrefsName, key, -1)
}

fun Context.getLong(PrefsName: String?, key: String?, defaultValue: Long): Long {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    return prefs.getLong(key, defaultValue)
}

fun Context.putFloat(PrefsName: String?, key: String?, value: Float): Boolean {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putFloat(key, value)
    return editor.commit()
}

fun Context.getFloat(PrefsName: String?, key: String?): Float {
    return getFloat(PrefsName, key, -1f)
}

fun Context.getFloat(PrefsName: String?, key: String?, defaultValue: Float): Float {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    return prefs.getFloat(key, defaultValue)
}

fun Context.putBoolean(PrefsName: String?, key: String?, value: Boolean): Boolean {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putBoolean(key, value)
    return editor.commit()
}

fun Context.getBoolean(PrefsName: String?, key: String?): Boolean {
    return getBoolean(PrefsName, key, false)
}

fun Context.getBoolean(PrefsName: String?, key: String?, defaultValue: Boolean): Boolean {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    return prefs.getBoolean(key, defaultValue)
}

fun Context.clear(PrefsName: String?) {
    val prefs = getSharedPreferences(PrefsName, Context.MODE_PRIVATE)
    prefs.edit().clear().commit()
}

fun Context.clearAll(vararg Prefs: String?) {
    Prefs.forEach {
        val prefs = getSharedPreferences(it, Context.MODE_PRIVATE)
        prefs.edit().clear().commit()
    }
}