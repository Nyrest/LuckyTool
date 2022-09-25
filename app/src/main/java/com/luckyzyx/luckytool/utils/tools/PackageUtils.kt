@file:Suppress("DEPRECATION", "unused")

package com.luckyzyx.luckytool.utils.tools

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.ResolveInfoFlags
import android.content.pm.ResolveInfo

class PackageUtils(private val packageManager: PackageManager) {
    fun getPackageInfo(packName: String, flag: Int): PackageInfo {
        if (SDK < 33) return packageManager.getPackageInfo(packName, flag)
        return packageManager.getPackageInfo(packName, PackageManager.PackageInfoFlags.of(flag.toLong()))
    }

    fun getApplicationInfo(packName: String, flag: Int): ApplicationInfo {
        if (SDK < 33) return packageManager.getApplicationInfo(packName, flag)
        return packageManager.getApplicationInfo(packName, PackageManager.ApplicationInfoFlags.of(flag.toLong()))
    }

    fun getInstalledPackages(flag: Int): MutableList<PackageInfo> {
        if (SDK < 33) return packageManager.getInstalledPackages(flag)
        return packageManager.getInstalledPackages(PackageManager.PackageInfoFlags.of(flag.toLong()))
    }

    fun getInstalledApplications(flag: Int): MutableList<ApplicationInfo> {
        if (SDK < 33) return packageManager.getInstalledApplications(flag)
        return packageManager.getInstalledApplications(PackageManager.ApplicationInfoFlags.of(flag.toLong()))
    }

    fun resolveActivity(intent: Intent,flag: Int): ResolveInfo? {
        if (SDK < 33) return packageManager.resolveActivity(intent,flag)
        return packageManager.resolveActivity(intent,ResolveInfoFlags.of(flag.toLong()))
    }
}

