@file:Suppress("unused")

package com.luckyzyx.luckytool.utils.tools

import com.simple.spiderman.SpiderMan
import com.squareup.moshi.Moshi

object MoshiUtil {

    //KotlinJsonAdapterFactory 会增大apk体积
    //addLast(KotlinJsonAdapterFactory())
    val moshi: Moshi = Moshi.Builder().build()

    /**
     * T 转换 Json
     */
    inline fun <reified T> toJson(input: T, indent: String = ""): String {
        try {
            val jsonAdapter = moshi.adapter(T::class.java)
            return jsonAdapter.indent(indent).toJson(input)
        } catch (e: Exception) {
            e.printStackTrace()
            SpiderMan.show(e)
        }
        return ""
    }

    /**
     * String 转换 T
     */
    inline fun <reified T> fromJson(jsonStr: String): T? {
        try {
            val jsonAdapter = moshi.adapter(T::class.java)
            return jsonAdapter.fromJson(jsonStr)
        } catch (e: Exception) {
            e.printStackTrace()
            SpiderMan.show(e)
        }
        return null
    }
}