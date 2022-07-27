@file:Suppress("unused")

package com.luckyzyx.colorosext.utils

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object MoshiUtil {

    abstract class MoshiTypeReference<T> // 自定义的类，用来包装泛型

    val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    inline fun <reified T> toJson(input: T, indent: String = ""): String {
        try {
            val jsonAdapter = moshi.adapter<T>(getGenericType<T>())
            return jsonAdapter.indent(indent).toJson(input)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    inline fun <reified T> fromJson(jsonStr: String): T? {
        try {
            val jsonAdapter = moshi.adapter<T>(getGenericType<T>())
            return jsonAdapter.fromJson(jsonStr)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    inline fun <reified T> getGenericType(): Type {
        return object :
            MoshiTypeReference<T>() {}::class.java
            .genericSuperclass
            .let { it as ParameterizedType }
            .actualTypeArguments
            .first()
    }
}